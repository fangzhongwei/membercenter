package com.lawsofnature.service

import java.util

import Ice.ObjectImpl
import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Guice}
import com.jxjxgo.scrooge.thrift.template.{ScroogeThriftServerTemplate, ScroogeThriftServerTemplateImpl}
import com.lawsofnatrue.common.ice._
import com.lawsofnature.account.client.{AccountClientService, AccountClientServiceImpl}
import com.lawsofnature.common.rabbitmq.{RabbitmqProducerTemplate, RabbitmqProducerTemplateImpl}
import com.lawsofnature.edcenter.client.{EdClientService, EdClientServiceImpl}
import com.lawsofnature.repo._
import com.lawsofnature.rpc.MemberEndpointImpl
import com.twitter.util.Future
import org.slf4j.LoggerFactory
import thrift.MemberEndpoint

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
      bind(classOf[IceServerTemplate]).to(classOf[IceServerTemplateImpl]).asEagerSingleton()
      bind(classOf[IcePrxFactory]).to(classOf[IcePrxFactoryImpl]).asEagerSingleton()
      bind(classOf[EdClientService]).to(classOf[EdClientServiceImpl]).asEagerSingleton()
      bind(classOf[RabbitmqProducerTemplate]).to(classOf[RabbitmqProducerTemplateImpl]).asEagerSingleton()
      bind(classOf[AccountClientService]).to(classOf[AccountClientServiceImpl]).asEagerSingleton()
    }
  })

  injector.getInstance(classOf[RabbitmqProducerTemplate]).connect
  injector.getInstance(classOf[EdClientService]).initClient
  injector.getInstance(classOf[IceServerTemplate]).startServer
}
