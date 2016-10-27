package com.lawsofnature.rpc

import java.sql.Timestamp
import javax.inject.Inject

import Ice.Current
import RpcMember.{MemberCarrier, RegisterResponse, _MemberEndpointDisp}
import com.lawsofnature.repo.{MemberRepository, TmMemberIdentityRow, TmMemberRow}
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by fangzhongwei on 2016/10/11.
  */
class MemberEndpointImpl @Inject()(memberRepository: MemberRepository) extends _MemberEndpointDisp {

  var logger = LoggerFactory.getLogger(this.getClass)

  implicit val timeout = (30 seconds)

  override def register(memberCarrier: MemberCarrier, current: Current): RegisterResponse = {
    try {
      val ids: Seq[Long] = Await.result(memberRepository.getNextMemberId(), timeout)
      val memberId: Long = ids(0)
      val identity = if (memberCarrier.pid == 1) memberCarrier.mobile else memberCarrier.email
      val tmMemberRow: TmMemberRow = TmMemberRow(memberId, memberCarrier.username, 1, memberCarrier.pwd, new Timestamp(System.currentTimeMillis()))
      val memberIdentityRow: TmMemberIdentityRow = TmMemberIdentityRow(0, memberId, identity, memberCarrier.pid.toByte, new Timestamp(System.currentTimeMillis()))
      Await.result(memberRepository.createMember(tmMemberRow, memberIdentityRow), timeout)
      new RegisterResponse(true, "0", "注册成功")
    } catch {
      case ex: MySQLIntegrityConstraintViolationException =>
        logger.error("register", ex)
        new RegisterResponse(false, "1", "用户名已存在")
      case ex: Exception =>
        logger.error("register", ex)
        new RegisterResponse(false, "1", "注册失败")
    }
  }

  override def getMemberByMemberId(memberId: Long, current: Current): MemberCarrier = {
    //    val result: Option[Member] = Await.result(memberRepository.getMemberById(memberId.toInt), 30 seconds)
    //    result match {
    //      case Some(member) => new MemberCarrier(member.id.get, member.deviceType, member.fingerPrint, member.username, member.pid, member.mobile, member.email, "")
    //      case None => null
    //    }
    null
  }

  override def getMemberByUsername(username: String, current: Current): MemberCarrier = {
    //    val result: Option[Member] = Await.result(memberRepository.getMemberByUsername(username), 30 seconds)
    //    result match {
    //      case Some(member) => new MemberCarrier(member.id.get, member.deviceType, member.fingerPrint, member.username, member.pid, member.mobile, member.email, "")
    //      case None => null
    //    }
    null
  }

  override def getMemberByEmail(email: String, current: Current): MemberCarrier = {
    //    val result: Option[Member] = Await.result(memberRepository.getMemberByEmail(email), 30 seconds)
    //    result match {
    //      case Some(member) => new MemberCarrier(member.id.get, member.deviceType, member.fingerPrint, member.username, member.pid, member.mobile, member.email, "")
    //      case None => null
    //    }
    null
  }

  override def getMemberByMobile(mobile: String, current: Current): MemberCarrier = {
    //    val result: Option[Member] = Await.result(memberRepository.getMemberByMobile(mobile), 30 seconds)
    //    result match {
    //      case Some(member) => new MemberCarrier(member.id.get, member.deviceType, member.fingerPrint, member.username, member.pid, member.mobile, member.email, "")
    //      case None => null
    //    }
    null
  }

  override def getMemberById(id: Int, current: Current): MemberCarrier = {
    //    val result: Option[Member] = Await.result(memberRepository.getMemberById(id), 30 seconds)
    //    result match {
    //      case Some(member) => new MemberCarrier(member.id.get, member.deviceType, member.fingerPrint, member.username, member.pid, member.mobile, member.email, "")
    //      case None => null
    //    }
    null
  }
}
