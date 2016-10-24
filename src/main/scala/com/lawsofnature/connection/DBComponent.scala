package com.lawsofnature.connection


import slick.driver.JdbcProfile

trait DBComponent {
  import driver.api._

  val driver: JdbcProfile

  val db: Database
}
