package com.lawsofnature.service

import com.lawsofnature.repo._
import RpcMember._MemberEndpointDisp
import com.google.inject.{AbstractModule, Guice}
import com.lawsofnature.rpc.{MemberEndpointImpl, MemberServer, MemberServerImpl}
import org.slf4j.LoggerFactory


object HttpService extends App {
  var logger = LoggerFactory.getLogger(this.getClass)

  private val injector = Guice.createInjector(new AbstractModule() {
    override def configure() {
      bind(classOf[MemberRepository]).to(classOf[MemberRepositoryImpl]).asEagerSingleton()
      bind(classOf[_MemberEndpointDisp]).to(classOf[MemberEndpointImpl]).asEagerSingleton()
      bind(classOf[MemberServer]).to(classOf[MemberServerImpl]).asEagerSingleton()
    }
  })

  injector.getInstance(classOf[MemberServer]).initServer
}
