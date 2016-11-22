package com.lawsofnature.rpc

import javax.inject.Inject

import Ice.Current
import RpcMember._
import com.lawsofnature.service.MemberService
import org.slf4j.LoggerFactory

/**
  * Created by fangzhongwei on 2016/10/11.
  */
class MemberEndpointImpl @Inject()(memberService: MemberService) extends _MemberEndpointDisp {
  var logger = LoggerFactory.getLogger(this.getClass)

  override def register(traceId: String, request: MemberRegisterRequest, current: Current): BaseResponse = {
    logger.info("register request, traceId:{}, request:{}, current:{}", traceId, request, current)
    memberService.register(traceId, request)
  }

  override def getMemberByIdentity(traceId: String, identity: String, pid: Int, current: Current): MemberResponse = {
    logger.info("getMemberByIdentity request, traceId:{}, identity:{}, pid:{}, current:{}", traceId, identity, pid, current)
    memberService.getMemberByIdentity(traceId, identity, pid)
  }

  override def getMemberByMemberId(traceId: String, memberId: Long, current: Current): MemberResponse = {
    logger.info("traceId:{}, memberId:{}, current:{}", traceId, memberId, current)
    memberService.getMemberByMemberId(traceId, memberId)
  }
}
