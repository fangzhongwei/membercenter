package com.lawsofnature.repo

import com.lawsofnature.connection.{DBComponent, MySQLDBImpl}

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables extends MySQLDBImpl {
  this: DBComponent =>

  import profile.api._

  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = TmMember.schema ++ TmMemberIdentity.schema

  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table TmMember
    *
    * @param memberId  Database column member_id SqlType(BIGINT), AutoInc, PrimaryKey
    * @param username  Database column username SqlType(VARCHAR), Length(64,true)
    * @param status    Database column status SqlType(TINYINT)
    * @param password  Database column password SqlType(VARCHAR), Length(256,true)
    * @param gmtCreate Database column gmt_create SqlType(TIMESTAMP)
    * @param gmtUpdate Database column gmt_update SqlType(TIMESTAMP), Default(None) */
  case class TmMemberRow(memberId: Long, username: String, status: Byte, password: String, gmtCreate: java.sql.Timestamp, gmtUpdate: Option[java.sql.Timestamp] = None)

  /** GetResult implicit for fetching TmMemberRow objects using plain SQL queries */
  implicit def GetResultTmMemberRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Byte], e3: GR[java.sql.Timestamp], e4: GR[Option[java.sql.Timestamp]]): GR[TmMemberRow] = GR {
    prs => import prs._
      TmMemberRow.tupled((<<[Long], <<[String], <<[Byte], <<[String], <<[java.sql.Timestamp], <<?[java.sql.Timestamp]))
  }

  /** Table description of table tm_member. Objects of this class serve as prototypes for rows in queries. */
  class TmMember(_tableTag: Tag) extends Table[TmMemberRow](_tableTag, "tm_member") {
    def * = (memberId, username, status, password, gmtCreate, gmtUpdate) <> (TmMemberRow.tupled, TmMemberRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberId), Rep.Some(username), Rep.Some(status), Rep.Some(password), Rep.Some(gmtCreate), gmtUpdate).shaped.<>({ r => import r._; _1.map(_ => TmMemberRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column member_id SqlType(BIGINT), AutoInc, PrimaryKey */
    val memberId: Rep[Long] = column[Long]("member_id", O.AutoInc, O.PrimaryKey)
    /** Database column username SqlType(VARCHAR), Length(64,true) */
    val username: Rep[String] = column[String]("username", O.Length(64, varying = true))
    /** Database column status SqlType(TINYINT) */
    val status: Rep[Byte] = column[Byte]("status")
    /** Database column password SqlType(VARCHAR), Length(256,true) */
    val password: Rep[String] = column[String]("password", O.Length(256, varying = true))
    /** Database column gmt_create SqlType(TIMESTAMP) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
    /** Database column gmt_update SqlType(TIMESTAMP), Default(None) */
    val gmtUpdate: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("gmt_update", O.Default(None))

    /** Index over (username) (database name uq_tm_u) */
    val index1 = index("uq_tm_u", username)
  }

  /** Collection-like TableQuery object for table TmMember */
  lazy val TmMember = new TableQuery(tag => new TmMember(tag))

  /** Entity class storing rows of table TmMemberIdentity
    *
    * @param id        Database column id SqlType(INT), PrimaryKey
    * @param memberId  Database column member_id SqlType(INT)
    * @param identity  Database column identity SqlType(VARCHAR), Length(64,true)
    * @param pid       Database column pid SqlType(TINYINT)
    * @param gmtCreate Database column gmt_create SqlType(TIMESTAMP) */
  case class TmMemberIdentityRow(id: Int, memberId: Int, identity: String, pid: Byte, gmtCreate: java.sql.Timestamp)

  /** GetResult implicit for fetching TmMemberIdentityRow objects using plain SQL queries */
  implicit def GetResultTmMemberIdentityRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Byte], e3: GR[java.sql.Timestamp]): GR[TmMemberIdentityRow] = GR {
    prs => import prs._
      TmMemberIdentityRow.tupled((<<[Int], <<[Int], <<[String], <<[Byte], <<[java.sql.Timestamp]))
  }

  /** Table description of table tm_member_identity. Objects of this class serve as prototypes for rows in queries. */
  class TmMemberIdentity(_tableTag: Tag) extends Table[TmMemberIdentityRow](_tableTag, "tm_member_identity") {
    def * = (id, memberId, identity, pid, gmtCreate) <> (TmMemberIdentityRow.tupled, TmMemberIdentityRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(memberId), Rep.Some(identity), Rep.Some(pid), Rep.Some(gmtCreate)).shaped.<>({ r => import r._; _1.map(_ => TmMemberIdentityRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column member_id SqlType(INT) */
    val memberId: Rep[Int] = column[Int]("member_id")
    /** Database column identity SqlType(VARCHAR), Length(64,true) */
    val identity: Rep[String] = column[String]("identity", O.Length(64, varying = true))
    /** Database column pid SqlType(TINYINT) */
    val pid: Rep[Byte] = column[Byte]("pid")
    /** Database column gmt_create SqlType(TIMESTAMP) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")

    /** Index over (identity,pid) (database name uq_tmi_ip) */
    val index1 = index("uq_tmi_ip", (identity, pid))
  }

  /** Collection-like TableQuery object for table TmMemberIdentity */
  lazy val TmMemberIdentity = new TableQuery(tag => new TmMemberIdentity(tag))

  protected def TmMemberIdentityAutoInc = TmMemberIdentity returning TmMemberIdentity.map(_.memberId)
}