package com.lawsofnature.service

import java.sql.Timestamp
import javax.inject.Inject

import RpcEd.DecryptResponse
import com.lawsofnature.account.client.AccountClientService
import com.lawsofnature.common.exception.{ErrorCode, ServiceException}
import com.lawsofnature.common.helper.MaskHelper
import com.lawsofnature.common.rabbitmq.RabbitmqProducerTemplate
import com.lawsofnature.edcenter.client.EdClientService
import com.lawsofnature.membercenter.domain.Member
import com.lawsofnature.repo.MemberRepository
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.StandardPasswordEncoder
import thrift.{MemberBaseResponse, MemberResponse}

/**
  * Created by fangzhongwei on 2016/11/22.
  */
trait MemberService {
  def updateMemberStatus(traceId: String, memberId: Long, status: Int): MemberBaseResponse

  def updateNickName(traceId: String, memberId: Long, nickName: String = null): MemberBaseResponse

  def register(traceId: String, mobileTicket: String): MemberBaseResponse

  def getMemberById(traceId: String, memberId: Long): MemberResponse

  def getMemberByMobile(traceId: String, mobileTicket: String): MemberResponse
}

class MemberServiceImpl @Inject()(rabbitmqProducerTemplate: RabbitmqProducerTemplate, edClientService: EdClientService, memberRepository: MemberRepository, accountClientService: AccountClientService) extends MemberService {
  private[this] val logger = LoggerFactory.getLogger(this.getClass)
  private[this] val passwordEncoder: StandardPasswordEncoder = new StandardPasswordEncoder(ConfigFactory.load().getString("password.salt"))

  override def register(traceId: String, mobileTicket: String): MemberBaseResponse = {
    val memberId: Long = memberRepository.getNextMemberId()
    val decryptResponse: DecryptResponse = edClientService.decrypt(traceId, mobileTicket)
    decryptResponse.code match {
      case "0" =>
        val mobile: String = MaskHelper.maskMobile(decryptResponse.raw)
        val timestamp: Timestamp = new Timestamp(System.currentTimeMillis())
        accountClientService.createAccount(traceId, memberId)
        memberRepository.createMember(Member(memberId, mobile, mobileTicket, 0.toByte, "", "", timestamp, timestamp))
        MemberBaseResponse("0")
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
