package com.lawsofnature.service

import java.util

import Ice.ObjectImpl
import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Guice}
import com.lawsofnatrue.common.ice.{IceServerTemplate, IceServerTemplateImpl}
import com.lawsofnature.repo._
import com.lawsofnature.rpc.{MemberEndpointImpl, MemberServer, MemberServerImpl}
import com.typesafe.config.{Config, ConfigFactory, ConfigRenderOptions}
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._

object RpcService extends App {
  var logger = LoggerFactory.getLogger(this.getClass)

  private val injector = Guice.createInjector(new AbstractModule() {
    override def configure() {
      val map: util.HashMap[String, String] = configMap
      Names.bindProperties(binder(), map)
      bind(classOf[MemberRepository]).to(classOf[MemberRepositoryImpl]).asEagerSingleton()
      bind(classOf[ObjectImpl]).to(classOf[MemberEndpointImpl]).asEagerSingleton()
      bind(classOf[MemberServer]).to(classOf[MemberServerImpl]).asEagerSingleton()
      bind(classOf[IceServerTemplate]).to(classOf[IceServerTemplateImpl]).asEagerSingleton()
    }
  })

  def configMap: util.HashMap[String, String] = {
    val config: Config = ConfigFactory.load()
    val map: util.HashMap[String, String] = new java.util.HashMap[String, String]()
    for (entry <- config.entrySet()) {
      val value: String = entry.getValue.render()
      if (value.length > 2) {

        map.put(entry.getKey, value.substring(1, value.length - 1))
        println("key:" + entry.getKey + ",value:" + value.substring(1, value.length - 1) + "")
      }
    }
    map
  }

  injector.getInstance(classOf[MemberServer]).initServer
}
