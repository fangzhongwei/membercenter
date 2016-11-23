package com.lawsofnature.repo

import com.lawsofnature.connection.{DBComponent, MySQLDBImpl}

import scala.concurrent.{Awaitable, Future}
import scala.concurrent.ExecutionContext.Implicits.global

trait MemberRepository extends Tables {
  this: DBComponent =>


  import profile.api._

  protected def TmMemberAutoInc = TmMember returning TmMember.map(_.memberId)
  protected def TmMemberIdentityAutoInc = TmMemberIdentity returning TmMemberIdentity.map(_.memberId)

  def createMember(member: TmMemberRow, memberIdentityRowUsername: TmMemberIdentityRow, memberIdentityRow: TmMemberIdentityRow, memberReg: TmMemberRegRow): Future[Unit] = {
    val a = (for {
      _ <- TmMember += member
      _ <- TmMemberIdentity += memberIdentityRowUsername
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

  def getMemberIdentity(identity: String): Future[Option[TmMemberIdentityRow]] = db.run {
    TmMemberIdentity.filter( r => r.identity === identity).result.headOption
  }

  def getMemberIdentitiesByMemberId(memberId: Long): Future[Seq[TmMemberIdentityRow]] = db.run {
    TmMemberIdentity.filter( r => r.memberId).result
  }

  def getNextMemberId():Future[Seq[(Long)]]={
    val tab = System.currentTimeMillis() % 3
    val sequenceName = "member_id_" + tab
    db.run(sql"""select nextval($sequenceName)""".as[(Long)])
  }
}

class MemberRepositoryImpl extends MemberRepository with MySQLDBImpl