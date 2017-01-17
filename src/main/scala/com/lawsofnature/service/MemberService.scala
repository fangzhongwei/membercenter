package com.lawsofnature.service

import java.nio.charset.StandardCharsets
import java.sql.Timestamp
import javax.inject.Inject

import RpcEd.DecryptResponse
import RpcMember.{BaseResponse, MemberResponse}
import com.lawsofnature.account.client.AccountClientService
import com.lawsofnature.common.exception.{ErrorCode, ServiceException}
import com.lawsofnature.common.helper.MaskHelper
import com.lawsofnature.common.rabbitmq.RabbitmqProducerTemplate
import com.lawsofnature.edcenter.client.EdClientService
import com.lawsofnature.membercenter.domain.Member
import com.lawsofnature.repo.MemberRepository
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.StandardPasswordEncoder

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

/**
  * Created by fangzhongwei on 2016/11/22.
  */
trait MemberService {
  def updateMemberStatus(traceId: String, memberId: Long, status: Int): BaseResponse

  def updateNickName(traceId: String, memberId: Long, nickName: String = null): BaseResponse

  def register(traceId: String, mobileTicket: String): BaseResponse

  def getMemberById(traceId: String, memberId: Long): MemberResponse

  def getMemberByMobile(traceId: String, mobileTicket: String): MemberResponse
}

class MemberServiceImpl @Inject()(rabbitmqProducerTemplate: RabbitmqProducerTemplate, edClientService: EdClientService, memberRepository: MemberRepository, accountClientService:AccountClientService) extends MemberService {
  private[this] val logger = LoggerFactory.getLogger(this.getClass)
  private[this] val passwordEncoder: StandardPasswordEncoder = new StandardPasswordEncoder(ConfigFactory.load().getString("password.salt"))

  override def register(traceId: String, mobileTicket: String): BaseResponse = {
    val memberId: Long = memberRepository.getNextMemberId()
    val decryptResponse: DecryptResponse = edClientService.decrypt(traceId, mobileTicket)
    decryptResponse.code match {
      case "0" =>
        val mobile: String = MaskHelper.maskMobile(decryptResponse.raw)
        val timestamp: Timestamp = new Timestamp(System.currentTimeMillis())
        accountClientService.createAccount(traceId, memberId)
        memberRepository.createMember(Member(memberId, mobile, mobileTicket, 0.toByte, "", "", timestamp, timestamp))
        new BaseResponse("0")
      case _ => new BaseResponse(decryptResponse.code)
    }
  }

  def encodePassword(pwd: String): String = {
    passwordEncoder.encode(pwd)
  }

  override def getMemberById(traceId: String, memberId: Long): MemberResponse = {
    memberRepository.getMemberById(memberId) match {
      case Some(m) =>
        new MemberResponse("0", m.memberId, m.mobile, m.mobileTicket, m.status, m.nickName, m.password, m.gmtCreate.getTime, m.gmtUpdate.getTime)
      case None => throw ServiceException.make(ErrorCode.EC_UC_MEMBER_NOT_EXISTS)
    }
  }

  override def getMemberByMobile(traceId: String, mobileTicket: String): MemberResponse = {
    memberRepository.getMemberByMobileTicket(mobileTicket) match {
      case Some(m) =>
        new MemberResponse("0", m.memberId, m.mobile, m.mobileTicket, m.status, m.nickName, m.password, m.gmtCreate.getTime, m.gmtUpdate.getTime)
      case None => throw ServiceException.make(ErrorCode.EC_UC_MEMBER_NOT_EXISTS)
    }
  }

  override def updateNickName(traceId: String, memberId: Long, nickName: String): BaseResponse = {
    memberRepository.updateNickName(memberId, nickName)
    new BaseResponse("0")
  }

  override def updateMemberStatus(traceId: String, memberId: Long, status: Int): BaseResponse = {
    memberRepository.updateMemberStatus(memberId, status.toByte)
    new BaseResponse("0")
  }
}
