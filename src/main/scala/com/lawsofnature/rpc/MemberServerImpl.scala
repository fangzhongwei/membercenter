package com.lawsofnature.rpc

import javax.inject.Inject

import RpcMember._MemberEndpointDisp
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

/**
  * Created by fangzhongwei on 2016/10/9.
  */
class MemberServerImpl @Inject()(memberEndpoint: _MemberEndpointDisp) extends MemberServer {
  var logger = LoggerFactory.getLogger(this.getClass)

  var status: Int = 0
  // Communicator实例，是ice run time的主句柄
  var ic: Ice.Communicator = _

  override def initServer: Unit = {
    try {
      val conf = ConfigFactory.load()
      logger.info("init ice run time... ice.server.member config:" + conf.getAnyRef("ice.server.member"))

      val argsArray = Array[String](
        String.format("%s/Locator:tcp -h %s -p %s", conf.getString("ice.server.member.grid.name"), conf.getString("ice.server.member.host"), conf.getInt("ice.server.member.port").toString),
        String.format("--Ice.ThreadPool.Server.Size=%s", conf.getInt("ice.server.member.threadpool.server.size").toString),
        String.format("--Ice.ThreadPool.Server.SizeMax=%s", conf.getInt("ice.server.member.threadpool.server.sizemax").toString),
        String.format("--Ice.ThreadPool.Server.SizeWarn=%s", conf.getInt("ice.server.member.threadpool.server.sizewarn").toString))

      ic = Ice.Util.initialize(argsArray)

      val adapter: Ice.ObjectAdapter = ic.createObjectAdapterWithEndpoints(conf.getString("ice.server.member.adapter.name"), String.format("default -p %s", conf.getInt("ice.server.member.proxy.port").toString))

      adapter.add(memberEndpoint, Ice.Util.stringToIdentity(conf.getString("ice.server.member.proxy.name")))

      adapter.activate()

      logger.info("adapter active , waiting for request...")
      //这个方法挂起发出调用的线程，直到服务器实现终止为止。或我们自己发出一个调用关闭。
      ic.waitForShutdown()
    } catch {
      case e: Ice.LocalException =>
        logger.error("ice", e)
        status = 1
      case e: Exception =>
        logger.error("ice", e)
        status = 1
    } finally {
      if (ic != null) {
        ic.destroy()
      }
    }
    System.exit(status)
  }
}
