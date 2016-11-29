package com.lawsofnature.repo

import com.lawsofnature.connection.MySQLDBImpl

/** Entity class storing rows of table TmMember
  *  @param memberId Database column member_id SqlType(BIGINT), PrimaryKey
  *  @param username Database column username SqlType(VARCHAR), Length(64,true)
  *  @param status Database column status SqlType(TINYINT)
  *  @param password Database column password SqlType(VARCHAR), Length(256,true)
  *  @param gmtCreate Database column gmt_create SqlType(TIMESTAMP)
  *  @param gmtUpdate Database column gmt_update SqlType(TIMESTAMP), Default(None) */
case class TmMemberRow(memberId: Long, username: String, status: Byte, password: String, gmtCreate: java.sql.Timestamp, gmtUpdate: Option[java.sql.Timestamp] = None)

/** Entity class storing rows of table TmMemberIdentity
  *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
  *  @param memberId Database column member_id SqlType(BIGINT)
  *  @param identity Database column identity SqlType(VARCHAR), Length(64,true)
  *  @param pid Database column pid SqlType(TINYINT)
  *  @param gmtCreate Database column gmt_create SqlType(TIMESTAMP) */
case class TmMemberIdentityRow(id: Long, memberId: Long, identity: String, pid: Byte, gmtCreate: java.sql.Timestamp)

/** Entity class storing rows of table TmMemberReg
  *  @param memberId Database column member_id SqlType(BIGINT), PrimaryKey
  *  @param regDeviceType Database column reg_device_type SqlType(TINYINT)
  *  @param regDeviceIdentity Database column reg_device_identity SqlType(VARCHAR), Length(128,true)
  *  @param regIp Database column reg_ip SqlType(BIGINT)
  *  @param regLat Database column reg_lat SqlType(VARCHAR), Length(32,true)
  *  @param regLng Database column reg_lng SqlType(VARCHAR), Length(32,true)
  *  @param country Database column country SqlType(VARCHAR), Length(16,true), Default(None)
  *  @param province Database column province SqlType(VARCHAR), Length(16,true), Default(None)
  *  @param city Database column city SqlType(VARCHAR), Length(16,true), Default(None)
  *  @param county Database column county SqlType(VARCHAR), Length(16,true), Default(None)
  *  @param address Database column address SqlType(VARCHAR), Length(255,true), Default(None)
  *  @param gmtCreate Database column gmt_create SqlType(TIMESTAMP) */
case class TmMemberRegRow(memberId: Long, regDeviceType: Byte, regDeviceIdentity: String, regIp: Long, regLat: String, regLng: String, country: Option[String] = None, province: Option[String] = None, city: Option[String] = None, county: Option[String] = None, address: Option[String] = None, gmtCreate: java.sql.Timestamp)

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables extends MySQLDBImpl{
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = TmMember.schema ++ TmMemberIdentity.schema ++ TmMemberReg.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** GetResult implicit for fetching TmMemberRow objects using plain SQL queries */
  implicit def GetResultTmMemberRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Byte], e3: GR[java.sql.Timestamp], e4: GR[Option[java.sql.Timestamp]]): GR[TmMemberRow] = GR{
    prs => import prs._
      TmMemberRow.tupled((<<[Long], <<[String], <<[Byte], <<[String], <<[java.sql.Timestamp], <<?[java.sql.Timestamp]))
  }
  /** Table description of table tm_member. Objects of this class serve as prototypes for rows in queries. */
  class TmMember(_tableTag: Tag) extends profile.api.Table[TmMemberRow](_tableTag, "tm_member") {
    def * = (memberId, username, status, password, gmtCreate, gmtUpdate) <> (TmMemberRow.tupled, TmMemberRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberId), Rep.Some(username), Rep.Some(status), Rep.Some(password), Rep.Some(gmtCreate), gmtUpdate).shaped.<>({r=>import r._; _1.map(_=> TmMemberRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column member_id SqlType(BIGINT), PrimaryKey */
    val memberId: Rep[Long] = column[Long]("member_id", O.PrimaryKey)
    /** Database column username SqlType(VARCHAR), Length(64,true) */
    val username: Rep[String] = column[String]("username", O.Length(64,varying=true))
    /** Database column status SqlType(TINYINT) */
    val status: Rep[Byte] = column[Byte]("status")
    /** Database column password SqlType(VARCHAR), Length(256,true) */
    val password: Rep[String] = column[String]("password", O.Length(256,varying=true))
    /** Database column gmt_create SqlType(TIMESTAMP) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
    /** Database column gmt_update SqlType(TIMESTAMP), Default(None) */
    val gmtUpdate: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("gmt_update", O.Default(None))

    /** Uniqueness Index over (username) (database name uq_tm_u) */
    val index1 = index("uq_tm_u", username, unique=true)
  }
  /** Collection-like TableQuery object for table TmMember */
  lazy val TmMember = new TableQuery(tag => new TmMember(tag))

  /** GetResult implicit for fetching TmMemberIdentityRow objects using plain SQL queries */
  implicit def GetResultTmMemberIdentityRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Byte], e3: GR[java.sql.Timestamp]): GR[TmMemberIdentityRow] = GR{
    prs => import prs._
      TmMemberIdentityRow.tupled((<<[Long], <<[Long], <<[String], <<[Byte], <<[java.sql.Timestamp]))
  }
  /** Table description of table tm_member_identity. Objects of this class serve as prototypes for rows in queries. */
  class TmMemberIdentity(_tableTag: Tag) extends profile.api.Table[TmMemberIdentityRow](_tableTag, "tm_member_identity") {
    def * = (id, memberId, identity, pid, gmtCreate) <> (TmMemberIdentityRow.tupled, TmMemberIdentityRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(memberId), Rep.Some(identity), Rep.Some(pid), Rep.Some(gmtCreate)).shaped.<>({r=>import r._; _1.map(_=> TmMemberIdentityRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column member_id SqlType(BIGINT) */
    val memberId: Rep[Long] = column[Long]("member_id")
    /** Database column identity SqlType(VARCHAR), Length(64,true) */
    val identity: Rep[String] = column[String]("identity", O.Length(64,varying=true))
    /** Database column pid SqlType(TINYINT) */
    val pid: Rep[Byte] = column[Byte]("pid")
    /** Database column gmt_create SqlType(TIMESTAMP) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")

    /** Uniqueness Index over (identity) (database name uq_tmi_ip) */
    val index1 = index("uq_tmi_ip", identity, unique=true)
    /** Index over (memberId) (database name uq_tmi_m) */
    val index2 = index("uq_tmi_m", memberId)
  }
  /** Collection-like TableQuery object for table TmMemberIdentity */
  lazy val TmMemberIdentity = new TableQuery(tag => new TmMemberIdentity(tag))

 /** GetResult implicit for fetching TmMemberRegRow objects using plain SQL queries */
  implicit def GetResultTmMemberRegRow(implicit e0: GR[Long], e1: GR[Byte], e2: GR[String], e3: GR[Option[String]], e4: GR[java.sql.Timestamp]): GR[TmMemberRegRow] = GR{
    prs => import prs._
      TmMemberRegRow.tupled((<<[Long], <<[Byte], <<[String], <<[Long], <<[String], <<[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table tm_member_reg. Objects of this class serve as prototypes for rows in queries. */
  class TmMemberReg(_tableTag: Tag) extends profile.api.Table[TmMemberRegRow](_tableTag, "tm_member_reg") {
    def * = (memberId, regDeviceType, regDeviceIdentity, regIp, regLat, regLng, country, province, city, county, address, gmtCreate) <> (TmMemberRegRow.tupled, TmMemberRegRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberId), Rep.Some(regDeviceType), Rep.Some(regDeviceIdentity), Rep.Some(regIp), Rep.Some(regLat), Rep.Some(regLng), country, province, city, county, address, Rep.Some(gmtCreate)).shaped.<>({r=>import r._; _1.map(_=> TmMemberRegRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7, _8, _9, _10, _11, _12.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column member_id SqlType(BIGINT), PrimaryKey */
    val memberId: Rep[Long] = column[Long]("member_id", O.PrimaryKey)
    /** Database column reg_device_type SqlType(TINYINT) */
    val regDeviceType: Rep[Byte] = column[Byte]("reg_device_type")
    /** Database column reg_device_identity SqlType(VARCHAR), Length(128,true) */
    val regDeviceIdentity: Rep[String] = column[String]("reg_device_identity", O.Length(128,varying=true))
    /** Database column reg_ip SqlType(BIGINT) */
    val regIp: Rep[Long] = column[Long]("reg_ip")
    /** Database column reg_lat SqlType(VARCHAR), Length(32,true) */
    val regLat: Rep[String] = column[String]("reg_lat", O.Length(32,varying=true))
    /** Database column reg_lng SqlType(VARCHAR), Length(32,true) */
    val regLng: Rep[String] = column[String]("reg_lng", O.Length(32,varying=true))
    /** Database column country SqlType(VARCHAR), Length(16,true), Default(None) */
    val country: Rep[Option[String]] = column[Option[String]]("country", O.Length(16,varying=true), O.Default(None))
    /** Database column province SqlType(VARCHAR), Length(16,true), Default(None) */
    val province: Rep[Option[String]] = column[Option[String]]("province", O.Length(16,varying=true), O.Default(None))
    /** Database column city SqlType(VARCHAR), Length(16,true), Default(None) */
    val city: Rep[Option[String]] = column[Option[String]]("city", O.Length(16,varying=true), O.Default(None))
    /** Database column county SqlType(VARCHAR), Length(16,true), Default(None) */
    val county: Rep[Option[String]] = column[Option[String]]("county", O.Length(16,varying=true), O.Default(None))
    /** Database column address SqlType(VARCHAR), Length(255,true), Default(None) */
    val address: Rep[Option[String]] = column[Option[String]]("address", O.Length(255,varying=true), O.Default(None))
    /** Database column gmt_create SqlType(TIMESTAMP) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
  }
  /** Collection-like TableQuery object for table TmMemberReg */
  lazy val TmMemberReg = new TableQuery(tag => new TmMemberReg(tag))

}