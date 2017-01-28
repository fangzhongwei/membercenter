package com.lawsofnature.rpc

import javax.inject.Inject

import com.lawsofnature.common.exception.{ErrorCode, ServiceException}
import com.lawsofnature.service.MemberService
import com.twitter.util.Future
import org.slf4j.LoggerFactory
import thrift.{MemberBaseResponse, MemberEndpoint, MemberResponse}

/**
  * Created by fangzhongwei on 2016/10/11.
  */
class MemberEndpointImpl @Inject()(memberService: MemberService) extends MemberEndpoint[Future] {
  private[this] val logger = LoggerFactory.getLogger(this.getClass)

  override def register(traceId: String, mobileTicket: String): Future[MemberBaseResponse] = {
    try {
      Future.value(memberService.register(traceId, mobileTicket))
    } catch {
      case ex: ServiceException =>
        logger.error(traceId, ex)
        Future.value(MemberBaseResponse(ex.getErrorCode.getCode))
      case ex: Exception =>
        logger.error(traceId, ex)
        Future.value(MemberBaseResponse(ErrorCode.EC_SYSTEM_ERROR.getCode))
    }
  }

  override def getMemberByMobile(traceId: String, mobileTicket: String): Future[MemberResponse] = {
    try {
      Future.value(memberService.getMemberByMobile(traceId, mobileTicket))
    } catch {
      case ex: ServiceException =>
        logger.error(traceId, ex)
        Future.value(errorMemberResponse(ex.getErrorCode))
      case ex: Exception =>
        logger.error(traceId, ex)
        Future.value(errorMemberResponse(ErrorCode.EC_SYSTEM_ERROR))
    }
  }

  def errorMemberResponse(errorCode: ErrorCode): MemberResponse = {
    MemberResponse(code = errorCode.getCode)
  }

  override def getMemberById(traceId: String, memberId: Long): Future[MemberResponse] = {
    try {
      Future.value(memberService.getMemberById(traceId, memberId))
    } catch {
      case ex: ServiceException =>
        logger.error(traceId, ex)
        Future.value(errorMemberResponse(ex.getErrorCode))
      case ex: Exception =>
        logger.error(traceId, ex)
        Future.value(errorMemberResponse(ErrorCode.EC_SYSTEM_ERROR))
    }
  }

  override def updateNickName(traceId: String, memberId: Long, s1: String): Future[MemberBaseResponse] = {
    try {
      Future.value(memberService.updateNickName(traceId, memberId))
    } catch {
      case ex: ServiceException =>
        logger.error(traceId, ex)
        Future.value(MemberBaseResponse(ex.getErrorCode.getCode))
      case ex: Exception =>
        logger.error(traceId, ex)
        Future.value(MemberBaseResponse(ErrorCode.EC_SYSTEM_ERROR.getCode))
    }
  }

  override def updateMemberStatus(traceId: String, memberId: Long, status: Int): Future[MemberBaseResponse] = {
    try {
      Future.value(memberService.updateMemberStatus(traceId, memberId, status))
    } catch {
      case ex: ServiceException =>
        logger.error(traceId, ex)
        Future.value(MemberBaseResponse(ex.getErrorCode.getCode))
      case ex: Exception =>
        logger.error(traceId, ex)
        Future.value(MemberBaseResponse(ErrorCode.EC_SYSTEM_ERROR.getCode))
    }
  }
}
