package com.lawsofnature.repo


import com.lawsofnature.connection.{DBComponent, MySQLDBImpl}

import scala.concurrent.Future


trait MemberRepository extends MemberTable {
  this: DBComponent =>

  import driver.api._

  def createMember(member: Member): Future[Int] = db.run {
    memberTableAutoInc += member
  }

  def updateMember(member: Member): Future[Int] = db.run {
    memberTableQuery.filter(_.id === member.id.get).update(member)
  }

  def getMemberById(id: Int): Future[Option[Member]] = db.run {
    memberTableQuery.filter(_.id === id).result.headOption
  }

  def getMemberByUsername(username: String): Future[Option[Member]] = db.run {
    memberTableQuery.filter(_.username === username).result.headOption
  }

  def getMemberByMobile(mobile: String): Future[Option[Member]] = db.run {
    memberTableQuery.filter(_.mobile === mobile).result.headOption
  }

  def getMemberByEmail(email: String): Future[Option[Member]] = db.run {
    memberTableQuery.filter(_.email === email).result.headOption
  }

  def ddl = db.run {
    memberTableQuery.schema.create
  }
}

trait MemberTable {
  this: DBComponent =>

  import driver.api._

  class MemberTable(tag: Tag) extends Table[Member](tag, "tm_member") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val deviceType = column[Int]("device_type")
    val fingerPrint = column[String]("finger_print")
    val username = column[String]("username")
    val pid = column[Int]("pid")
    val mobile = column[String]("mobile")
    val email = column[String]("email")
    val pwd = column[String]("pwd")

    def * = (id.?, deviceType, fingerPrint, username, pid, mobile, email, pwd) <> (Member.tupled, Member.unapply)
  }

  protected val memberTableQuery = TableQuery[MemberTable]

  protected def memberTableAutoInc = memberTableQuery returning memberTableQuery.map(_.id)
}

class MemberRepositoryImpl extends MemberRepository with MySQLDBImpl

case class Member(id: Option[Int] = None, deviceType: Int, fingerPrint: String, username: String, pid: Int, mobile: String, email: String, pwd: String)
