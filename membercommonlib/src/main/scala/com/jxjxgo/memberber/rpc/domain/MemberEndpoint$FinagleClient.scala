/**
 * Generated by Scrooge
 *   version: 4.5.0
 *   rev: 014664de600267b36809bbc85225e26aec286216
 *   built at: 20160203-205352
 */
package com.jxjxgo.memberber.rpc.domain

import com.twitter.finagle.SourcedException
import com.twitter.finagle.{service => ctfs}
import com.twitter.finagle.stats.{NullStatsReceiver, StatsReceiver}
import com.twitter.finagle.thrift.{Protocols, ThriftClientRequest}
import com.twitter.scrooge.{ThriftStruct, ThriftStructCodec}
import com.twitter.util.{Future, Return, Throw, Throwables}
import java.nio.ByteBuffer
import java.util.Arrays
import org.apache.thrift.protocol._
import org.apache.thrift.TApplicationException
import org.apache.thrift.transport.{TMemoryBuffer, TMemoryInputTransport}
import scala.collection.{Map, Set}
import scala.language.higherKinds


@javax.annotation.Generated(value = Array("com.twitter.scrooge.Compiler"))
class MemberEndpoint$FinagleClient(
    val service: com.twitter.finagle.Service[ThriftClientRequest, Array[Byte]],
    val protocolFactory: TProtocolFactory,
    val serviceName: String,
    stats: StatsReceiver,
    responseClassifier: ctfs.ResponseClassifier)
  extends MemberEndpoint[Future] {

  def this(
    service: com.twitter.finagle.Service[ThriftClientRequest, Array[Byte]],
    protocolFactory: TProtocolFactory = Protocols.binaryFactory(),
    serviceName: String = "MemberEndpoint",
    stats: StatsReceiver = NullStatsReceiver
  ) = this(
    service,
    protocolFactory,
    serviceName,
    stats,
    ctfs.ResponseClassifier.Default
  )

  import MemberEndpoint._

  protected def encodeRequest(name: String, args: ThriftStruct) = {
    val buf = new TMemoryBuffer(512)
    val oprot = protocolFactory.getProtocol(buf)

    oprot.writeMessageBegin(new TMessage(name, TMessageType.CALL, 0))
    args.write(oprot)
    oprot.writeMessageEnd()

    val bytes = Arrays.copyOfRange(buf.getArray, 0, buf.length)
    new ThriftClientRequest(bytes, false)
  }

  protected def decodeResponse[T <: ThriftStruct](resBytes: Array[Byte], codec: ThriftStructCodec[T]) = {
    val iprot = protocolFactory.getProtocol(new TMemoryInputTransport(resBytes))
    val msg = iprot.readMessageBegin()
    try {
      if (msg.`type` == TMessageType.EXCEPTION) {
        val exception = TApplicationException.read(iprot) match {
          case sourced: SourcedException =>
            if (serviceName != "") sourced.serviceName = serviceName
            sourced
          case e => e
        }
        throw exception
      } else {
        codec.decode(iprot)
      }
    } finally {
      iprot.readMessageEnd()
    }
  }

  protected def missingResult(name: String) = {
    new TApplicationException(
      TApplicationException.MISSING_RESULT,
      name + " failed: unknown result"
    )
  }

  protected def setServiceName(ex: Throwable): Throwable =
    if (this.serviceName == "") ex
    else {
      ex match {
        case se: SourcedException =>
          se.serviceName = this.serviceName
          se
        case _ => ex
      }
    }

  // ----- end boilerplate.

  private[this] val scopedStats = if (serviceName != "") stats.scope(serviceName) else stats
  private[this] object __stats_register {
    val RequestsCounter = scopedStats.scope("register").counter("requests")
    val SuccessCounter = scopedStats.scope("register").counter("success")
    val FailuresCounter = scopedStats.scope("register").counter("failures")
    val FailuresScope = scopedStats.scope("register").scope("failures")
  }
  
  def register(traceId: String, mobileTicket: String, deviceType: Int): Future[com.jxjxgo.memberber.rpc.domain.MemberBaseResponse] = {
    __stats_register.RequestsCounter.incr()
    val inputArgs = Register.Args(traceId, mobileTicket, deviceType)
    val replyDeserializer: Array[Byte] => _root_.com.twitter.util.Try[com.jxjxgo.memberber.rpc.domain.MemberBaseResponse] =
      response => {
        val result = decodeResponse(response, Register.Result)
        val exception: Throwable =
        null
  
        if (result.success.isDefined)
          _root_.com.twitter.util.Return(result.success.get)
        else if (exception != null)
          _root_.com.twitter.util.Throw(exception)
        else
          _root_.com.twitter.util.Throw(missingResult("register"))
      }
  
    val serdeCtx = new _root_.com.twitter.finagle.thrift.DeserializeCtx[com.jxjxgo.memberber.rpc.domain.MemberBaseResponse](inputArgs, replyDeserializer)
    _root_.com.twitter.finagle.context.Contexts.local.let(
      _root_.com.twitter.finagle.thrift.DeserializeCtx.Key,
      serdeCtx
    ) {
      val serialized = encodeRequest("register", inputArgs)
      this.service(serialized).flatMap { response =>
        Future.const(serdeCtx.deserialize(response))
      }.respond { response =>
        val responseClass = responseClassifier.applyOrElse(
          ctfs.ReqRep(inputArgs, response),
          ctfs.ResponseClassifier.Default)
        responseClass match {
          case ctfs.ResponseClass.Successful(_) =>
            __stats_register.SuccessCounter.incr()
          case ctfs.ResponseClass.Failed(_) =>
            __stats_register.FailuresCounter.incr()
            response match {
              case Throw(ex) =>
                setServiceName(ex)
                __stats_register.FailuresScope.counter(Throwables.mkString(ex): _*).incr()
              case _ =>
            }
        }
      }
    }
  }
  private[this] object __stats_getMemberById {
    val RequestsCounter = scopedStats.scope("getMemberById").counter("requests")
    val SuccessCounter = scopedStats.scope("getMemberById").counter("success")
    val FailuresCounter = scopedStats.scope("getMemberById").counter("failures")
    val FailuresScope = scopedStats.scope("getMemberById").scope("failures")
  }
  
  def getMemberById(traceId: String, memberId: Long): Future[com.jxjxgo.memberber.rpc.domain.MemberResponse] = {
    __stats_getMemberById.RequestsCounter.incr()
    val inputArgs = GetMemberById.Args(traceId, memberId)
    val replyDeserializer: Array[Byte] => _root_.com.twitter.util.Try[com.jxjxgo.memberber.rpc.domain.MemberResponse] =
      response => {
        val result = decodeResponse(response, GetMemberById.Result)
        val exception: Throwable =
        null
  
        if (result.success.isDefined)
          _root_.com.twitter.util.Return(result.success.get)
        else if (exception != null)
          _root_.com.twitter.util.Throw(exception)
        else
          _root_.com.twitter.util.Throw(missingResult("getMemberById"))
      }
  
    val serdeCtx = new _root_.com.twitter.finagle.thrift.DeserializeCtx[com.jxjxgo.memberber.rpc.domain.MemberResponse](inputArgs, replyDeserializer)
    _root_.com.twitter.finagle.context.Contexts.local.let(
      _root_.com.twitter.finagle.thrift.DeserializeCtx.Key,
      serdeCtx
    ) {
      val serialized = encodeRequest("getMemberById", inputArgs)
      this.service(serialized).flatMap { response =>
        Future.const(serdeCtx.deserialize(response))
      }.respond { response =>
        val responseClass = responseClassifier.applyOrElse(
          ctfs.ReqRep(inputArgs, response),
          ctfs.ResponseClassifier.Default)
        responseClass match {
          case ctfs.ResponseClass.Successful(_) =>
            __stats_getMemberById.SuccessCounter.incr()
          case ctfs.ResponseClass.Failed(_) =>
            __stats_getMemberById.FailuresCounter.incr()
            response match {
              case Throw(ex) =>
                setServiceName(ex)
                __stats_getMemberById.FailuresScope.counter(Throwables.mkString(ex): _*).incr()
              case _ =>
            }
        }
      }
    }
  }
  private[this] object __stats_getMemberByMobile {
    val RequestsCounter = scopedStats.scope("getMemberByMobile").counter("requests")
    val SuccessCounter = scopedStats.scope("getMemberByMobile").counter("success")
    val FailuresCounter = scopedStats.scope("getMemberByMobile").counter("failures")
    val FailuresScope = scopedStats.scope("getMemberByMobile").scope("failures")
  }
  
  def getMemberByMobile(traceId: String, mobileTicket: String): Future[com.jxjxgo.memberber.rpc.domain.MemberResponse] = {
    __stats_getMemberByMobile.RequestsCounter.incr()
    val inputArgs = GetMemberByMobile.Args(traceId, mobileTicket)
    val replyDeserializer: Array[Byte] => _root_.com.twitter.util.Try[com.jxjxgo.memberber.rpc.domain.MemberResponse] =
      response => {
        val result = decodeResponse(response, GetMemberByMobile.Result)
        val exception: Throwable =
        null
  
        if (result.success.isDefined)
          _root_.com.twitter.util.Return(result.success.get)
        else if (exception != null)
          _root_.com.twitter.util.Throw(exception)
        else
          _root_.com.twitter.util.Throw(missingResult("getMemberByMobile"))
      }
  
    val serdeCtx = new _root_.com.twitter.finagle.thrift.DeserializeCtx[com.jxjxgo.memberber.rpc.domain.MemberResponse](inputArgs, replyDeserializer)
    _root_.com.twitter.finagle.context.Contexts.local.let(
      _root_.com.twitter.finagle.thrift.DeserializeCtx.Key,
      serdeCtx
    ) {
      val serialized = encodeRequest("getMemberByMobile", inputArgs)
      this.service(serialized).flatMap { response =>
        Future.const(serdeCtx.deserialize(response))
      }.respond { response =>
        val responseClass = responseClassifier.applyOrElse(
          ctfs.ReqRep(inputArgs, response),
          ctfs.ResponseClassifier.Default)
        responseClass match {
          case ctfs.ResponseClass.Successful(_) =>
            __stats_getMemberByMobile.SuccessCounter.incr()
          case ctfs.ResponseClass.Failed(_) =>
            __stats_getMemberByMobile.FailuresCounter.incr()
            response match {
              case Throw(ex) =>
                setServiceName(ex)
                __stats_getMemberByMobile.FailuresScope.counter(Throwables.mkString(ex): _*).incr()
              case _ =>
            }
        }
      }
    }
  }
  private[this] object __stats_updateMemberStatus {
    val RequestsCounter = scopedStats.scope("updateMemberStatus").counter("requests")
    val SuccessCounter = scopedStats.scope("updateMemberStatus").counter("success")
    val FailuresCounter = scopedStats.scope("updateMemberStatus").counter("failures")
    val FailuresScope = scopedStats.scope("updateMemberStatus").scope("failures")
  }
  
  def updateMemberStatus(traceId: String, memberId: Long, status: Int): Future[com.jxjxgo.memberber.rpc.domain.MemberBaseResponse] = {
    __stats_updateMemberStatus.RequestsCounter.incr()
    val inputArgs = UpdateMemberStatus.Args(traceId, memberId, status)
    val replyDeserializer: Array[Byte] => _root_.com.twitter.util.Try[com.jxjxgo.memberber.rpc.domain.MemberBaseResponse] =
      response => {
        val result = decodeResponse(response, UpdateMemberStatus.Result)
        val exception: Throwable =
        null
  
        if (result.success.isDefined)
          _root_.com.twitter.util.Return(result.success.get)
        else if (exception != null)
          _root_.com.twitter.util.Throw(exception)
        else
          _root_.com.twitter.util.Throw(missingResult("updateMemberStatus"))
      }
  
    val serdeCtx = new _root_.com.twitter.finagle.thrift.DeserializeCtx[com.jxjxgo.memberber.rpc.domain.MemberBaseResponse](inputArgs, replyDeserializer)
    _root_.com.twitter.finagle.context.Contexts.local.let(
      _root_.com.twitter.finagle.thrift.DeserializeCtx.Key,
      serdeCtx
    ) {
      val serialized = encodeRequest("updateMemberStatus", inputArgs)
      this.service(serialized).flatMap { response =>
        Future.const(serdeCtx.deserialize(response))
      }.respond { response =>
        val responseClass = responseClassifier.applyOrElse(
          ctfs.ReqRep(inputArgs, response),
          ctfs.ResponseClassifier.Default)
        responseClass match {
          case ctfs.ResponseClass.Successful(_) =>
            __stats_updateMemberStatus.SuccessCounter.incr()
          case ctfs.ResponseClass.Failed(_) =>
            __stats_updateMemberStatus.FailuresCounter.incr()
            response match {
              case Throw(ex) =>
                setServiceName(ex)
                __stats_updateMemberStatus.FailuresScope.counter(Throwables.mkString(ex): _*).incr()
              case _ =>
            }
        }
      }
    }
  }
  private[this] object __stats_updateNickName {
    val RequestsCounter = scopedStats.scope("updateNickName").counter("requests")
    val SuccessCounter = scopedStats.scope("updateNickName").counter("success")
    val FailuresCounter = scopedStats.scope("updateNickName").counter("failures")
    val FailuresScope = scopedStats.scope("updateNickName").scope("failures")
  }
  
  def updateNickName(traceId: String, memberId: Long, nickName: String): Future[com.jxjxgo.memberber.rpc.domain.MemberBaseResponse] = {
    __stats_updateNickName.RequestsCounter.incr()
    val inputArgs = UpdateNickName.Args(traceId, memberId, nickName)
    val replyDeserializer: Array[Byte] => _root_.com.twitter.util.Try[com.jxjxgo.memberber.rpc.domain.MemberBaseResponse] =
      response => {
        val result = decodeResponse(response, UpdateNickName.Result)
        val exception: Throwable =
        null
  
        if (result.success.isDefined)
          _root_.com.twitter.util.Return(result.success.get)
        else if (exception != null)
          _root_.com.twitter.util.Throw(exception)
        else
          _root_.com.twitter.util.Throw(missingResult("updateNickName"))
      }
  
    val serdeCtx = new _root_.com.twitter.finagle.thrift.DeserializeCtx[com.jxjxgo.memberber.rpc.domain.MemberBaseResponse](inputArgs, replyDeserializer)
    _root_.com.twitter.finagle.context.Contexts.local.let(
      _root_.com.twitter.finagle.thrift.DeserializeCtx.Key,
      serdeCtx
    ) {
      val serialized = encodeRequest("updateNickName", inputArgs)
      this.service(serialized).flatMap { response =>
        Future.const(serdeCtx.deserialize(response))
      }.respond { response =>
        val responseClass = responseClassifier.applyOrElse(
          ctfs.ReqRep(inputArgs, response),
          ctfs.ResponseClassifier.Default)
        responseClass match {
          case ctfs.ResponseClass.Successful(_) =>
            __stats_updateNickName.SuccessCounter.incr()
          case ctfs.ResponseClass.Failed(_) =>
            __stats_updateNickName.FailuresCounter.incr()
            response match {
              case Throw(ex) =>
                setServiceName(ex)
                __stats_updateNickName.FailuresScope.counter(Throwables.mkString(ex): _*).incr()
              case _ =>
            }
        }
      }
    }
  }
}
