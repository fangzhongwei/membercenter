package com.jxjxgo.service

import java.util

import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Guice, TypeLiteral}
import com.jxjxgo.account.rpc.domain.AccountEndpoint
import com.jxjxgo.common.helper.ConfigHelper
import com.jxjxgo.edcenter.rpc.domain.EdServiceEndpoint
import com.jxjxgo.repo.{MemberRepository, MemberRepositoryImpl}
import com.jxjxgo.rpc.MemberEndpointImpl
import com.jxjxgo.scrooge.thrift.template.{ScroogeThriftServerTemplate, ScroogeThriftServerTemplateImpl}
import com.twitter.finagle.Thrift
import com.twitter.scrooge.ThriftService
import com.twitter.util.Future
import com.typesafe.config.{Config, ConfigFactory}

object RpcService extends App {
  private val injector = Guice.createInjector(new AbstractModule() {
    override def configure() {
      val map: util.HashMap[String, String] = ConfigHelper.configMap
      Names.bindProperties(binder(), map)

      val config: Config = ConfigFactory.load()
      bind(new TypeLiteral[EdServiceEndpoint[Future]](){}).toInstance(Thrift.client.newIface[EdServiceEndpoint[Future]](config.getString("edcenter.thrift.host.port")))
      bind(new TypeLiteral[AccountEndpoint[Future]](){}).toInstance(Thrift.client.newIface[AccountEndpoint[Future]](config.getString("account.thrift.host.port")))
      bind(classOf[MemberRepository]).to(classOf[MemberRepositoryImpl]).asEagerSingleton()
      bind(classOf[MemberService]).to(classOf[MemberServiceImpl]).asEagerSingleton()
      bind(classOf[ThriftService]).to(classOf[MemberEndpointImpl]).asEagerSingleton()
      bind(classOf[ScroogeThriftServerTemplate]).to(classOf[ScroogeThriftServerTemplateImpl]).asEagerSingleton()

    }
  })

  injector.getInstance(classOf[ScroogeThriftServerTemplate]).init
}
