package com.lawsofnature.rpc

import javax.inject.Inject

import RpcMember._MemberEndpointDisp
import com.lawsofnatrue.common.ice.IceServerTemplate
import com.lawsofnatrue.common.ice.IceServerTemplateImpl
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

/**
  * Created by fangzhongwei on 2016/10/9.
  */
class MemberServerImpl @Inject()(iceServerTemplate: IceServerTemplate) extends MemberServer {
  var logger = LoggerFactory.getLogger(this.getClass)

  var server:IceServerTemplateImpl = _

  override def initServer: Unit = {
    iceServerTemplate.startServer
  }
}