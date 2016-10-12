package com.lawsofnature.service

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.lawsofnature.repo._
import RpcMember._MemberEndpointDisp
import com.google.inject.{AbstractModule, Guice}
import com.lawsofnature.rpc.{MemberEndpointImpl, MemberServer, MemberServerImpl}
import org.slf4j.LoggerFactory


object HttpService extends App {
  var logger = LoggerFactory.getLogger(this.getClass)

  private val injector = Guice.createInjector(new AbstractModule() {
    override def configure() {
      bind(classOf[BankRepository]).to(classOf[BankRepositoryImpl])
      bind(classOf[MemberRepository]).to(classOf[MemberRepositoryImpl])
      bind(classOf[_MemberEndpointDisp]).to(classOf[MemberEndpointImpl])
      bind(classOf[MemberServer]).to(classOf[MemberServerImpl])
    }
  })

  private val memberServer = injector.getInstance(classOf[MemberServer])

  new Thread(new Runnable {
    override def run(): Unit =
      memberServer.initServer
  }).start()

//  private val bankService = injector.getInstance(classOf[Routes])

  implicit val system: ActorSystem = ActorSystem()

  implicit val materializer = ActorMaterializer()

  implicit val dispatcher = system.dispatcher

  private val repositoryImpl: BankRepositoryImpl = new BankRepositoryImpl
  private val memberRepositoryImpl: MemberRepositoryImpl = new MemberRepositoryImpl
  repositoryImpl.ddl.onComplete {
    _ =>
      repositoryImpl.createBank(Bank(None,"SBI"))
      repositoryImpl.createBank(Bank(None,"PNB"))
      repositoryImpl.createBank(Bank(None,"RBS"))
//      Http().bindAndHandle(bankService.bankRoutes, "localhost", 9000)
      memberRepositoryImpl.ddl
      logger.info("httpserver start up at port 9000")
  }

}
