package com.lawsofnature.rpc

import java.sql.Timestamp
import javax.inject.Inject

import Ice.Current
import RpcMember.{BaseResponse, MemberRegisterRequest, MemberResponse, _MemberEndpointDisp}
import com.lawsofnature.common.exception.{ServiceErrorCode, ServiceException}
import com.lawsofnature.common.rabbitmq.RabbitmqProducerTemplate
import com.lawsofnature.membercenter.helper.IPv4Util
import com.lawsofnature.repo.{MemberRepository, TmMemberIdentityRow, TmMemberRegRow, TmMemberRow}
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.StandardPasswordEncoder

import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by fangzhongwei on 2016/10/11.
  */
class MemberEndpointImpl @Inject()(memberRepository: MemberRepository, rabbitmqProducerTemplate: RabbitmqProducerTemplate) extends _MemberEndpointDisp {
  var logger = LoggerFactory.getLogger(this.getClass)

  private val passwordEncoder: StandardPasswordEncoder = new StandardPasswordEncoder(ConfigFactory.load().getString("password.salt"))

  implicit val timeout = (90 seconds)

  override def getMemberByIdentity(traceId: String, identity: String, pid: Int, current: Current): MemberResponse = null

  override def register(traceId: String, request: MemberRegisterRequest, current: Current): BaseResponse = {
    try {
      assert(traceId != null, "trace id is null !")
      logger.info("traceId:{}, request:{}, current:{}", traceId, request, current)

      checkIdentity(traceId, request, current)
      val memberId: Long = generateMemberId

      val identity = request.identity
      val gmtCreate: Timestamp = new Timestamp(System.currentTimeMillis())
      val tmMemberRow: TmMemberRow = TmMemberRow(memberId, request.username, 1, encodePassword(request.pwd), gmtCreate)
      val memberIdentityRowUsername: TmMemberIdentityRow = TmMemberIdentityRow(0, memberId, request.username, 0.toByte, gmtCreate)
      val memberIdentityRow: TmMemberIdentityRow = TmMemberIdentityRow(0, memberId, identity, request.pid.toByte, gmtCreate)
      val tmMemberRegRow: TmMemberRegRow = TmMemberRegRow(memberId, IPv4Util.ipToLong(request.ip), request.lat, request.lng, request.deviceType.toByte, request.deviceIdentity, gmtCreate)
      Await.result(memberRepository.createMember(tmMemberRow, memberIdentityRowUsername, memberIdentityRow, tmMemberRegRow), timeout)

      produceCreateAccountMessage(memberId)

      new BaseResponse(true, 0)
    } catch {
      case ex: ServiceException =>
        logger.error(traceId, ex)
        new BaseResponse(false, ex.serviceErrorCode.id)
      case ex: Exception =>
        logger.error(traceId, ex)
        new BaseResponse(false, ServiceErrorCode.EC_SYSTEM_ERROR.id)
    }
  }

  def encodePassword(pwd:String):String = {
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

  def checkIdentity(traceId: String, request: MemberRegisterRequest, current: Current): Unit = {
    val usernameMemberResponse: MemberResponse = getMemberByIdentity(traceId, request.username, 0, current)
    if (usernameMemberResponse.success) throw new ServiceException(ServiceErrorCode.EC_UC_USERNAME_TOKEN)
    val identityMemberResponse: MemberResponse = getMemberByIdentity(traceId, request.identity, request.pid, current)
    if (identityMemberResponse.success) {
      request.pid match {
        case 1 => throw new ServiceException(ServiceErrorCode.EC_UC_MOBILE_TOKEN)
        case 2 => throw new ServiceException(ServiceErrorCode.EC_UC_EMAIL_TOKEN)
      }
    }
  }

  override def getMemberByMemberId(traceId: String, memberId: Long, current: Current): MemberResponse = null
}
