package com.lawsofnature.service

import java.sql.Timestamp
import javax.inject.Inject

import RpcMember.{MemberIdentityExistsResponse, _}
import com.lawsofnature.common.exception.{ErrorCode, ServiceException}
import com.lawsofnature.common.rabbitmq.RabbitmqProducerTemplate
import com.lawsofnature.membercenter.converter.MemberConverter
import com.lawsofnature.membercenter.helper.IPv4Util
import com.lawsofnature.repo.{MemberRepository, TmMemberIdentityRow, TmMemberRegRow, TmMemberRow}
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.StandardPasswordEncoder

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}

/**
  * Created by fangzhongwei on 2016/11/22.
  */
trait MemberService {
  def isMemberIdentityExists(traceId: String, identity: String): MemberIdentityExistsResponse

  def checkPassword(traceId: String, memberId: Long, password: String): BaseResponse

  def register(traceId: String, request: MemberRegisterRequest): BaseResponse

  def getMemberByIdentity(traceId: String, identity: String): MemberResponse

  def getMemberByMemberId(traceId: String, memberId: Long): MemberResponse
}

class MemberServiceImpl @Inject()(memberRepository: MemberRepository, rabbitmqProducerTemplate: RabbitmqProducerTemplate) extends MemberService {
  var logger = LoggerFactory.getLogger(this.getClass)

  private val passwordEncoder: StandardPasswordEncoder = new StandardPasswordEncoder(ConfigFactory.load().getString("password.salt"))

  implicit val timeout = (90 seconds)

  override def register(traceId: String, request: MemberRegisterRequest): BaseResponse = {
    try {
      assert(traceId != null, "trace id is null !")

      checkIdentity(traceId, request)
      val memberId: Long = generateMemberId

      val identity = request.identity
      val gmtCreate: Timestamp = new Timestamp(System.currentTimeMillis())
      val tmMemberRow: TmMemberRow = TmMemberRow(memberId, request.username, 1, encodePassword(request.pwd), gmtCreate)
      val memberIdentityRowUsername: TmMemberIdentityRow = TmMemberIdentityRow(0, memberId, request.username, 0.toByte, gmtCreate)
      val memberIdentityRow: TmMemberIdentityRow = TmMemberIdentityRow(0, memberId, identity, request.pid.toByte, gmtCreate)
      val tmMemberRegRow: TmMemberRegRow = TmMemberRegRow(memberId, request.deviceType.toByte, request.deviceIdentity, IPv4Util.ipToLong(request.ip), request.lat, request.lng,
        Some(request.country), Some(request.province), Some(request.city), Some(request.county), Some(request.address), gmtCreate)
      Await.result(memberRepository.createMember(tmMemberRow, memberIdentityRowUsername, memberIdentityRow, tmMemberRegRow), timeout)

      produceCreateAccountMessage(memberId)

      new BaseResponse(true, 0)
    } catch {
      case ex: ServiceException =>
        logger.error(traceId, ex)
        new BaseResponse(false, ex.getErrorCode.getCode)
      case ex: Exception =>
        logger.error(traceId, ex)
        new BaseResponse(false, ErrorCode.EC_SYSTEM_ERROR.getCode)
    }
  }

  def encodePassword(pwd: String): String = {
    passwordEncoder.encode(pwd)
  }

  def produceCreateAccountMessage(memberId: Long): Future[Unit] = {
    val promise: Promise[Unit] = Promise[Unit]
    Future {
      val config: Config = ConfigFactory.load()
      rabbitmqProducerTemplate.send(config.getString("account.mq.exchange"),
        config.getString("account.mq.exchangeType"),
        config.getString("account.mq.queue"),
        config.getString("account.mq.routingKey"),
        memberId.toString.getBytes("UTF-8"))
      promise.success()
    }
    promise.future
  }

  def generateMemberId: Long = {
    val ids: Seq[Long] = Await.result(memberRepository.getNextMemberId(), timeout)
    val memberId: Long = ids(0)
    memberId
  }

  def checkIdentity(traceId: String, request: MemberRegisterRequest): Unit = {
    if (isMemberIdentityExists(traceId, request.username).exists) throw ServiceException.make(ErrorCode.EC_UC_USERNAME_TOKEN)
    val memberIdentityExistsResponse: MemberIdentityExistsResponse = isMemberIdentityExists(traceId, request.identity)
    if (memberIdentityExistsResponse.exists) {
      request.pid match {
        case 1 => throw ServiceException.make(ErrorCode.EC_UC_MOBILE_TOKEN)
        case 2 => throw ServiceException.make(ErrorCode.EC_UC_EMAIL_TOKEN)
      }
    }
  }

  override def getMemberByIdentity(traceId: String, identity: String): MemberResponse = {
    val millis: Long = System.currentTimeMillis()
    val memberIdentity: Option[TmMemberIdentityRow] = Await.result(memberRepository.getMemberIdentity(identity), timeout)
    memberIdentity match {
      case Some(mi) =>
        val response: MemberResponse = getMemberByMemberId(traceId, mi.memberId)
        logger.info("find member cost:" + (System.currentTimeMillis() - millis))
        response
      case None =>
        noMemberResponse
    }
  }

  override def getMemberByMemberId(traceId: String, memberId: Long): MemberResponse = {
    val tmMemberRow: Option[TmMemberRow] = Await.result(memberRepository.getMemberById(memberId), timeout)
    tmMemberRow match {
      case Some(m) =>
        val memberIdentityRows: Seq[TmMemberIdentityRow] = Await.result(memberRepository.getMemberIdentitiesByMemberId(memberId), timeout)
        new MemberResponse(true, 0, m.memberId, m.username, m.status.toInt, m.gmtCreate.getTime, MemberConverter.convert(memberIdentityRows))
      case None =>
        noMemberResponse
    }
  }

  def noMemberResponse: MemberResponse = {
    val response: MemberResponse = new MemberResponse()
    response.success = false
    response.code = ErrorCode.EC_UC_MEMBER_NOT_EXISTS.getCode
    response
  }

  override def checkPassword(traceId: String, memberId: Long, password: String): BaseResponse = {
    val dbPassword: Option[String] = Await.result(memberRepository.getPassword(memberId), timeout)
    dbPassword match {
      case Some(s) => passwordEncoder.matches(password, s) match {
        case true => new BaseResponse(true, 0)
        case false => new BaseResponse(false, ErrorCode.EC_UC_MEMBER_INVALID_USERNAME_OR_PWD.getCode)
      }
      case None => new BaseResponse(false, ErrorCode.EC_SYSTEM_ERROR.getCode)
    }
  }

  override def isMemberIdentityExists(traceId: String, identity: String): MemberIdentityExistsResponse = {
    val millis: Long = System.currentTimeMillis()
    val memberIdentity: Option[TmMemberIdentityRow] = Await.result(memberRepository.getMemberIdentity(identity), timeout)
    memberIdentity match {
      case Some(mi) =>
        logger.info("find member identity cost:" + (System.currentTimeMillis() - millis))
        new MemberIdentityExistsResponse(true, 0, true)
      case None =>
        new MemberIdentityExistsResponse(true, 0, false)
    }
  }
}
