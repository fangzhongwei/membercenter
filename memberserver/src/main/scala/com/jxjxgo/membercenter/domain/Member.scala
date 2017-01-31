package com.jxjxgo.membercenter.domain

import java.sql.Timestamp

/**
  * Created by fangzhongwei on 2017/1/4.
  */
case class Member(memberId: Long, mobile: String, mobileTicket: String, status: Byte, nickName: String, password: String, gmtCreate: Timestamp, gmtUpdate: Timestamp)
