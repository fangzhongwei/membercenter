package com.lawsofnature.service

import java.util

import Ice.ObjectImpl
import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Guice}
import com.lawsofnatrue.common.ice.{ConfigHelper, IceServerTemplate, IceServerTemplateImpl}
import com.lawsofnature.repo._
import com.lawsofnature.rpc.MemberEndpointImpl
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory


object RpcService extends App {
  var logger = LoggerFactory.getLogger(this.getClass)

  private val injector = Guice.createInjector(new AbstractModule() {
    override def configure() {
      val map: util.HashMap[String, String] = ConfigHelper.configMap
      Names.bindProperties(binder(), map)
      bind(classOf[MemberRepository]).to(classOf[MemberRepositoryImpl]).asEagerSingleton()
      bind(classOf[ObjectImpl]).to(classOf[MemberEndpointImpl]).asEagerSingleton()
      bind(classOf[IceServerTemplate]).to(classOf[IceServerTemplateImpl]).asEagerSingleton()
    }
  })

  injector.getInstance(classOf[IceServerTemplate]).startServer
}
