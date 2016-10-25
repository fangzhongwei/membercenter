package com.lawsofnature.connection


import slick.driver.JdbcProfile

trait DBComponent {

  val profile: JdbcProfile

  import profile.api._

  val db: Database
}
