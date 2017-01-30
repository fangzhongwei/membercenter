package com.lawsofnature.service

import java.util

import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Guice}
import com.jxjxgo.scrooge.thrift.template.{ScroogeThriftServerTemplate, ScroogeThriftServerTemplateImpl}
import com.lawsofnatrue.common.ice._
import com.lawsofnature.account.client.{AccountClientService, AccountClientServiceImpl}
import com.lawsofnature.repo._
import com.lawsofnature.rpc.MemberEndpointImpl
import com.twitter.finagle.Thrift
import com.twitter.util.Future
import org.slf4j.LoggerFactory
import thrift.{EdServiceEndpoint, MemberEndpoint}

object RpcService extends App {
  private[this] var logger = LoggerFactory.getLogger(this.getClass)

  private val injector = Guice.createInjector(new AbstractModule() {
    override def configure() {
      val map: util.HashMap[String, String] = ConfigHelper.configMap
      Names.bindProperties(binder(), map)
      bind(classOf[MemberRepository]).to(classOf[MemberRepositoryImpl]).asEagerSingleton()
      bind(classOf[MemberService]).to(classOf[MemberServiceImpl]).asEagerSingleton()
      bind(classOf[MemberEndpoint[Future]]).to(classOf[MemberEndpointImpl]).asEagerSingleton()
      bind(classOf[ScroogeThriftServerTemplate]).to(classOf[ScroogeThriftServerTemplateImpl[MemberEndpoint[Future]]]).asEagerSingleton()
      bind(classOf[IcePrxFactory]).to(classOf[IcePrxFactoryImpl]).asEagerSingleton()
      bind(classOf[EdServiceEndpoint[Future]]).toInstance(Thrift.client.newIface[EdServiceEndpoint[Future]]("127.0.0.1:8080"))
      bind(classOf[AccountClientService]).to(classOf[AccountClientServiceImpl]).asEagerSingleton()
    }
  })

  injector.getInstance(classOf[AccountClientService]).initClient
  injector.getInstance(classOf[ScroogeThriftServerTemplate]).init
}
