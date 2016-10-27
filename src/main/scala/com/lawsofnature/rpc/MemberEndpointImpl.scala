package com.lawsofnature.rpc

import java.sql.Timestamp
import javax.inject.Inject

import Ice.Current
import RpcMember.{MemberCarrier, RegisterResponse, _MemberEndpointDisp}
import com.lawsofnature.repo.{MemberRepository, TmMemberRow}
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by fangzhongwei on 2016/10/11.
  */
class MemberEndpointImpl @Inject()(memberRepository: MemberRepository) extends _MemberEndpointDisp {

  var logger = LoggerFactory.getLogger(this.getClass)

  override def register(memberCarrier: MemberCarrier, current: Current): RegisterResponse = {
//        val result: Int = Await.ready(memberRepository.createMember(TmMemberRow(None, memberCarrier.username, 1, memberCarrier.pwd, new Timestamp(System.currentTimeMillis()), None)))
//        logger.info(new StringBuilder("insert result:").append(result).toString())
//        if (result > 0) {
//          new RegisterResponse(true, "0", "注册成功")
//        } else {
//          new RegisterResponse(false, "1", "注册失败")
//        }


    //    case class TmMemberRow(memberId: Long, username: String, status: Byte, password: String, gmtCreate: java.sql.Timestamp, gmtUpdate: Option[java.sql.Timestamp] = None)
//
    memberRepository.createMember(TmMemberRow(0, memberCarrier.username, 1, memberCarrier.pwd, new Timestamp(System.currentTimeMillis()), None)) onComplete {
      case Success(id) => null
      case Failure(ex) => null
    }

    null
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
