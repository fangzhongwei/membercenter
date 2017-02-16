package com.jxjxgo.repo

import com.jxjxgo.membercenter.domain.Member
import com.jxjxgo.mysql.connection.DBComponent

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait MemberRepository extends Tables {
  this: DBComponent =>

  import profile.api._

  implicit def memberToRaw(m: Member): TmMemberRow = {
    TmMemberRow(m.memberId, m.mobile, m.mobileTicket, m.status, m.nickName, m.password, m.gmtCreate, m.gmtUpdate)
  }

  implicit def rawToMember(m: TmMemberRow): Member = {
    Member(m.memberId, m.mobile, m.mobileTicket, m.status, m.nickName, m.password, m.gmtCreate, m.gmtUpdate)
  }

  def createMember(member: Member): Future[Int] = db.run {
    TmMember += member
  }

  def getMemberById(id: Long): Option[Member] = {
    Await.result(db.run {
      TmMember.filter(_.memberId === id).result.headOption
    }, Duration.Inf) match {
      case Some(row) => Some(row)
      case None => None
    }
  }

  def getMemberByMobileTicket(mobileTicket: String): Option[Member] = {
    Await.result(db.run {
      TmMember.filter(_.mobileTicket === mobileTicket).result.headOption
    }, Duration.Inf) match {
      case Some(row) => Some(row)
      case None => None
    }
  }

  def getPassword(memberId: Long): Future[Option[String]] = db.run {
    sql"""select password from tm_member where member_id = $memberId""".as[String].headOption
  }

  def getNextMemberId(): Long = {
    Await.result(db.run(sql"""select nextval('seq_member_id')""".as[(Long)]), Duration.Inf).head
  }

  def updateNickName(memberId: Long, nickName: String) = db.run {
    TmMember.filter(_.memberId === memberId).map(m => (m.nickName, m.status)).update(nickName, 99)
  }

  def updateMemberStatus(memberId: Long, status: Byte) = db.run {
    TmMember.filter(_.memberId === memberId).map(m => (m.status)).update(status)
  }
}

class MemberRepositoryImpl extends MemberRepository with DBComponent