package com.lawsofnature.membercenter.generate

/**
  * Created by fangzhongwei on 2016/10/24.
  */
object GenerateUtil extends App{
  slick.codegen.SourceCodeGenerator.main(
//    Array(slickDriver, jdbcDriver, url, outputFolder, pkg, user, password)
//      Array("slick.jdbc.MySQLProfile", "com.mysql.jdbc.Driver", "jdbc:mysql://localhost/edcenter", "edcenter", "com.lawsofnature.edcenter.repo", "root", "123456")
      Array("slick.jdbc.MySQLProfile", "com.mysql.jdbc.Driver", "jdbc:mysql://localhost/member", "member", "com.lawsofnature.repo", "root", "123456")
//      Array("slick.jdbc.MySQLProfile", "com.mysql.jdbc.Driver", "jdbc:mysql://localhost/account", "account", "com.lawsofnature.account.repo", "root", "123456")
  )
}
