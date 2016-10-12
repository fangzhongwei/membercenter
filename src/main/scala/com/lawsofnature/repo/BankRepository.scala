package com.lawsofnature.repo


import com.lawsofnature.connection.{DBComponent, MySQLDBImpl}

import scala.concurrent.Future


trait BankRepository extends BankTable {
  this: DBComponent =>

  import driver.api._

  def createBank(bank: Bank): Future[Int] = db.run {
    bankTableAutoInc += bank
  }

  def updateBank(bank: Bank): Future[Int] = db.run {
    bankTableQuery.filter(_.id === bank.id.get).update(bank)
  }

  def getBankById(id: Int): Future[Option[Bank]] = db.run {
    bankTableQuery.filter(_.id === id).result.headOption
  }

  def getAllBanks(): Future[List[Bank]] = db.run {
    bankTableQuery.to[List].result
  }

  def deleteBank(id: Int): Future[Int] = db.run {
    bankTableQuery.filter(_.id === id).delete
  }

  def ddl = db.run {
    bankTableQuery.schema.create
  }

}

trait BankTable {
  this: DBComponent =>

  import driver.api._

  class BankTable(tag: Tag) extends Table[Bank](tag, "bank") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val name = column[String]("name")

    def * = (id.?, name) <> (Bank.tupled, Bank.unapply)

  }

  protected val bankTableQuery = TableQuery[BankTable]

  protected def bankTableAutoInc = bankTableQuery returning bankTableQuery.map(_.id)

}

//for demo(connected to H2 in memory database )
//class BankRepositoryImpl extends BankRepository with H2DBImpl

//use this for production
class BankRepositoryImpl extends BankRepository with MySQLDBImpl

case class Bank( id: Option[Int] = None, name: String)
