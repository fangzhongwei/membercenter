package com.lawsofnature.connection

import slick.jdbc.MySQLProfile

/**
  * Created by satendra on 16/3/16.
  */
trait MySQLDBImpl extends DBComponent {
  val profile = MySQLProfile

  import profile.api._

  val db: Database = MySqlDB.connectionPool
}

private[connection] object MySqlDB {
  import MySQLProfile.api._

  val connectionPool = Database.forConfig("mysql")
}