package com.lawsofnature.connection

import slick.driver.MySQLDriver

/**
  * Created by satendra on 16/3/16.
  */
trait MySQLDBImpl extends DBComponent {
  val profile = MySQLDriver

  import profile.api._

  val db: Database = MySqlDB.connectionPool
}

private[connection] object MySqlDB {
  import slick.driver.MySQLDriver.api._

  val connectionPool = Database.forConfig("mysql")
}