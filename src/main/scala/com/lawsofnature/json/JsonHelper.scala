package com.lawsofnature.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.lawsofnature.repo.{Bank, Member}
import spray.json.DefaultJsonProtocol

trait JsonHelper extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val bankFormat = jsonFormat2(Bank.apply)
  implicit val memberFormat = jsonFormat8(Member.apply)

}
