package com.lawsofnature.repo

import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}


class BankRepositoryTest extends FunSuite with BankRepository with TestH2DBImpl with ScalaFutures {

  test("create member") {
    val memberRepository:MemberRepositoryImpl = new MemberRepositoryImpl()
    memberRepository.createMember(Member(None, 1,"f", "u",2,"m","e", "p"))
    println(1234)
  }

//  implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))
//
//  test("Add new bank ") {
//    val response = createBank(Bank("ICICI bank"))
//    whenReady(response) { bankId =>
//      assert(bankId === 4)
//    }
//  }
//
//  test("Update  SBI bank  ") {
//    val response = updateBank(Bank("SBI Bank", Some(1)))
//    whenReady(response) { res =>
//      assert(res === 1)
//    }
//  }
//
//  test("Delete SBI bank  ") {
//    val response = deleteBank(2)
//    whenReady(response) { res =>
//      assert(res === 1)
//    }
//  }
//
//  test("Get bank list") {
//    val bankList = getAllBanks()
//    whenReady(bankList) { result =>
//      assert(result === List(Bank("SBI bank", Some(1)), Bank("PNB bank", Some(2)),Bank("RBS bank", Some(3))))
//    }
//  }

}