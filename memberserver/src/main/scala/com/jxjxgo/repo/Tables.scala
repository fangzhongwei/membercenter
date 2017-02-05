package com.jxjxgo.repo

import com.jxjxgo.mysql.connection.MySQLDBImpl


/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables extends MySQLDBImpl {

  import profile.api._

  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** Entity class storing rows of table TmMember
    *
    * @param memberId     Database column member_id SqlType(BIGINT), PrimaryKey
    * @param mobile       Database column mobile SqlType(VARCHAR), Length(11,true)
    * @param mobileTicket Database column mobile_ticket SqlType(VARCHAR), Length(16,true), Default()
    * @param status       Database column status SqlType(TINYINT)
    * @param nickName     Database column nick_name SqlType(VARCHAR), Length(32,true)
    * @param password     Database column password SqlType(VARCHAR), Length(128,true)
    * @param gmtCreate    Database column gmt_create SqlType(TIMESTAMP)
    * @param gmtUpdate    Database column gmt_update SqlType(TIMESTAMP) */
  case class TmMemberRow(memberId: Long, mobile: String, mobileTicket: String = "", status: Byte, nickName: String, password: String, gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)

  /** GetResult implicit for fetching TmMemberRow objects using plain SQL queries */
  implicit def GetResultTmMemberRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Byte], e3: GR[java.sql.Timestamp]): GR[TmMemberRow] = GR {
    prs => import prs._
      TmMemberRow.tupled((<<[Long], <<[String], <<[String], <<[Byte], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }

  /** Table description of table tm_member. Objects of this class serve as prototypes for rows in queries. */
  class TmMember(_tableTag: Tag) extends profile.api.Table[TmMemberRow](_tableTag, Some("member"), "tm_member") {
    def * = (memberId, mobile, mobileTicket, status, nickName, password, gmtCreate, gmtUpdate) <> (TmMemberRow.tupled, TmMemberRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberId), Rep.Some(mobile), Rep.Some(mobileTicket), Rep.Some(status), Rep.Some(nickName), Rep.Some(password), Rep.Some(gmtCreate), Rep.Some(gmtUpdate)).shaped.<>({ r => import r._; _1.map(_ => TmMemberRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column member_id SqlType(BIGINT), PrimaryKey */
    val memberId: Rep[Long] = column[Long]("member_id", O.PrimaryKey)
    /** Database column mobile SqlType(VARCHAR), Length(11,true) */
    val mobile: Rep[String] = column[String]("mobile", O.Length(11, varying = true))
    /** Database column mobile_ticket SqlType(VARCHAR), Length(16,true), Default() */
    val mobileTicket: Rep[String] = column[String]("mobile_ticket", O.Length(16, varying = true), O.Default(""))
    /** Database column status SqlType(TINYINT) */
    val status: Rep[Byte] = column[Byte]("status")
    /** Database column nick_name SqlType(VARCHAR), Length(32,true) */
    val nickName: Rep[String] = column[String]("nick_name", O.Length(32, varying = true))
    /** Database column password SqlType(VARCHAR), Length(128,true) */
    val password: Rep[String] = column[String]("password", O.Length(128, varying = true))
    /** Database column gmt_create SqlType(TIMESTAMP) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
    /** Database column gmt_update SqlType(TIMESTAMP) */
    val gmtUpdate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_update")

    /** Uniqueness Index over (mobileTicket) (database name uq_tm_u) */
    val index1 = index("uq_tm_u", mobileTicket, unique = true)
  }

  /** Collection-like TableQuery object for table TmMember */
  lazy val TmMember = new TableQuery(tag => new TmMember(tag))

}
