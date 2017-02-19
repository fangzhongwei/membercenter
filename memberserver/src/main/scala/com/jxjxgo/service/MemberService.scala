package com.jxjxgo.service

import java.sql.Timestamp
import javax.inject.Inject

import com.jxjxgo.account.rpc.domain.{AccountBaseResponse, AccountEndpoint}
import com.jxjxgo.common.helper.MaskHelper
import com.jxjxgo.edcenter.rpc.domain.{DecryptResponse, EdServiceEndpoint}
import com.jxjxgo.membercenter.domain.Member
import com.jxjxgo.repo.MemberRepository
import com.jxjxgo.common.exception.{ErrorCode, ServiceException}
import com.twitter.util.{Await, Future}
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.StandardPasswordEncoder
import com.jxjxgo.memberber.rpc.domain.{MemberBaseResponse, MemberResponse}

/**
  * Created by fangzhongwei on 2016/11/22.
  */
trait MemberService {
  def updateMemberStatus(traceId: String, memberId: Long, status: Int): MemberBaseResponse

  def updateNickName(traceId: String, memberId: Long, nickName: String): MemberBaseResponse

  def register(traceId: String, mobileTicket: String, deviceType: Int ): MemberBaseResponse

  def getMemberById(traceId: String, memberId: Long): MemberResponse

  def getMemberByMobile(traceId: String, mobileTicket: String): MemberResponse
}

class MemberServiceImpl @Inject()(edServiceEndpoint: EdServiceEndpoint[Future], memberRepository: MemberRepository, accountEndpoint: AccountEndpoint[Future]) extends MemberService {
  private[this] val logger = LoggerFactory.getLogger(this.getClass)
  private[this] val passwordEncoder: StandardPasswordEncoder = new StandardPasswordEncoder(ConfigFactory.load().getString("password.salt"))

  override def register(traceId: String, mobileTicket: String, deviceType: Int): MemberBaseResponse = {
    val memberId: Long = memberRepository.getNextMemberId()
    val decryptResponse: DecryptResponse = Await.result(edServiceEndpoint.decrypt(traceId, mobileTicket))
    decryptResponse.code match {
      case "0" =>
        val mobile: String = MaskHelper.maskMobile(decryptResponse.raw)
        val timestamp: Timestamp = new Timestamp(System.currentTimeMillis())
        val accountBaseResponse: AccountBaseResponse = Await.result(accountEndpoint.createAccount(traceId, memberId, deviceType))
        accountBaseResponse.code match {
          case "0" =>
            memberRepository.createMember(Member(memberId, mobile, mobileTicket, 0.toByte, "", "", timestamp, timestamp))
            MemberBaseResponse("0")
          case _ => throw ServiceException.make(ErrorCode.get(accountBaseResponse.code))
        }
      case _ => MemberBaseResponse(decryptResponse.code)
    }
  }

  def encodePassword(pwd: String): String = {
    passwordEncoder.encode(pwd)
  }

  override def getMemberById(traceId: String, memberId: Long): MemberResponse = {
    memberRepository.getMemberById(memberId) match {
      case Some(m) =>
        MemberResponse("0", m.memberId, m.mobile, m.mobileTicket, m.status, m.nickName, m.password, m.gmtCreate.getTime, m.gmtUpdate.getTime)
      case None => throw ServiceException.make(ErrorCode.EC_UC_MEMBER_NOT_EXISTS)
    }
  }

  override def getMemberByMobile(traceId: String, mobileTicket: String): MemberResponse = {
    memberRepository.getMemberByMobileTicket(mobileTicket) match {
      case Some(m) =>
        MemberResponse("0", m.memberId, m.mobile, m.mobileTicket, m.status, m.nickName, m.password, m.gmtCreate.getTime, m.gmtUpdate.getTime)
      case None => throw ServiceException.make(ErrorCode.EC_UC_MEMBER_NOT_EXISTS)
    }
  }

  override def updateNickName(traceId: String, memberId: Long, nickName: String): MemberBaseResponse = {
    memberRepository.updateNickName(memberId, nickName)
    MemberBaseResponse("0")
  }

  override def updateMemberStatus(traceId: String, memberId: Long, status: Int): MemberBaseResponse = {
    memberRepository.updateMemberStatus(memberId, status.toByte)
    MemberBaseResponse("0")
  }
}
