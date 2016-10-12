package com.lawsofnature.rpc

import javax.inject.Inject

import RpcMember._MemberEndpointDisp
import org.slf4j.LoggerFactory

/**
  * Created by fangzhongwei on 2016/10/9.
  */
class MemberServerImpl @Inject()(obj: _MemberEndpointDisp) extends MemberServer {
  var logger = LoggerFactory.getLogger(this.getClass)

  var status: Int = 0
  // Communicator实例，是ice run time的主句柄
  var ic: Ice.Communicator = null

  override def initServer: Unit = {
    try {
      // 调用Ice.Util.Initialize()初始化Ice run time
      logger.info("初始化ice run time...")

      val argsArray = Array[String]("DemoIceGrid/Locator:tcp -h 192.168.181.130 -p 4061", "--Ice.ThreadPool.Server.Size=10", "--Ice.ThreadPool.Server.SizeMax=1000", "--Ice.ThreadPool.Server.SizeWarn=800")

      ic = Ice.Util.initialize(argsArray)

      // 创建一个对象适配器，传入适配器名字和在10000端口处接收来的请求
      logger.info("创建对象适配器，监听端口10001...")
      val adapter: Ice.ObjectAdapter = ic.createObjectAdapterWithEndpoints("MemberAdapter", "default -p 10001")

      // 实例化一个PrinterI对象，为Printer接口创建一个servant
      logger.info("为接口创建servant...")

      // 调用适配器的add,告诉它有一个新的servant,传递的参数是刚才的servant,这里的“SimplePrinter”是Servant的名字
      logger.info("对象适配器加入servant...")
      adapter.add(obj, Ice.Util.stringToIdentity("Member"))

      //调用适配器的activate()方法，激活适配器。被激活后，服务器开始处理来自客户的请求。
      logger.info("激活适配器，服务器等待处理请求...")
      adapter.activate()

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
