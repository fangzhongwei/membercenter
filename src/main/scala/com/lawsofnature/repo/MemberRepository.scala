package com.lawsofnature.repo

import com.lawsofnature.connection.{DBComponent, MySQLDBImpl}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait MemberRepository extends Tables {
  this: DBComponent =>

  import profile.api._

  protected def TmMemberAutoInc = TmMember returning TmMember.map(_.memberId)

  protected def TmMemberIdentityAutoInc = TmMemberIdentity returning TmMemberIdentity.map(_.memberId)

  def createMember(member: TmMemberRow, memberIdentityRow: TmMemberIdentityRow, memberReg: TmMemberRegRow): Future[Unit] = {
    val a = (for {
      _ <- TmMember += member
      _ <- TmMemberIdentity += memberIdentityRow
      _ <- TmMemberReg += memberReg
    } yield ()).transactionally
    db.run(a)
  }

  def getMemberById(id: Long): Future[Option[TmMemberRow]] = db.run {
    TmMember.filter(_.memberId === id).result.headOption
  }

  def getMemberByUsername(username: String): Future[Option[TmMemberRow]] = db.run {
    TmMember.filter(_.username === username).result.headOption
  }

  def getMemberIdentityByTicket(ticket: String): Future[Option[TmMemberIdentityRow]] = db.run {
    TmMemberIdentity.filter(r => r.identityTicket === ticket).result.headOption
  }

  def getMemberIdentitiesByMemberId(memberId: Long): Future[Seq[TmMemberIdentityRow]] = db.run {
    TmMemberIdentity.filter(r => r.memberId === memberId).result
  }

  def getPassword(memberId: Long): Future[Option[String]] = db.run {
    sql"""select password from tm_member where member_id = $memberId""".as[String].headOption
  }

  var index = -1

  def getNextMemberId(): Future[Seq[(Long)]] = {
    index = index + 1
    val sequenceName = "member_id_" + index % 3
    db.run(sql"""select nextval($sequenceName)""".as[(Long)])
  }
}

class MemberRepositoryImpl extends MemberRepository with MySQLDBImpl