package com.lawsofnature.rpc

import javax.inject.Inject

import Ice.Current
import RpcMember.{BaseResponse, _}
import com.lawsofnature.common.exception.{ErrorCode, ServiceException}
import com.lawsofnature.service.MemberService
import org.slf4j.LoggerFactory

/**
  * Created by fangzhongwei on 2016/10/11.
  */
class MemberEndpointImpl @Inject()(memberService: MemberService) extends _MemberEndpointDisp {
  var logger = LoggerFactory.getLogger(this.getClass)

  override def register(traceId: String, mobileTicket: String, current: Current): BaseResponse = {
    try {
      memberService.register(traceId, mobileTicket)
    } catch {
      case ex: ServiceException =>
        logger.error(traceId, ex)
        new BaseResponse(ex.getErrorCode.getCode)
      case ex: Exception =>
        logger.error(traceId, ex)
        new BaseResponse(ErrorCode.EC_SYSTEM_ERROR.getCode)
    }
  }

  override def getMemberByMobile(traceId: String, mobileTicket: String, current: Current): MemberResponse = {
    try {
      memberService.getMemberByMobile(traceId, mobileTicket)
    } catch {
      case ex: ServiceException =>
        logger.error(traceId, ex)
        errorMemberResponse(ex.getErrorCode)
      case ex: Exception =>
        logger.error(traceId, ex)
        errorMemberResponse(ErrorCode.EC_SYSTEM_ERROR)
    }
  }

  def errorMemberResponse(errorCode: ErrorCode): MemberResponse = {
    val response: MemberResponse = new MemberResponse()
    response.code = errorCode.getCode
    response
  }

  override def getMemberById(traceId: String, memberId: Long, current: Current): MemberResponse = {
    try {
      memberService.getMemberById(traceId, memberId)
    } catch {
      case ex: ServiceException =>
        logger.error(traceId, ex)
        errorMemberResponse(ex.getErrorCode)
      case ex: Exception =>
        logger.error(traceId, ex)
        errorMemberResponse(ErrorCode.EC_SYSTEM_ERROR)
    }
  }
}
