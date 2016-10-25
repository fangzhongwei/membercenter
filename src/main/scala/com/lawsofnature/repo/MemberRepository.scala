package com.lawsofnature.repo

import com.lawsofnature.connection.{DBComponent, MySQLDBImpl}

import scala.concurrent.Future

trait MemberRepository extends Tables {
  this: DBComponent =>

  import profile.api._

  protected def TmMemberAutoInc = TmMember returning TmMember.map(_.memberId)

  def createMember(member: TmMemberRow): Future[Long] = db.run {
    TmMemberAutoInc += member
  }

  def getMemberById(id: Long): Future[Option[TmMemberRow]] = db.run {
    TmMember.filter(_.memberId === id).result.headOption
  }

  def getMemberByUsername(username: String): Future[Option[TmMemberRow]] = db.run {
    TmMember.filter(_.username === username).result.headOption
  }
}

class MemberRepositoryImpl extends MemberRepository with MySQLDBImpl