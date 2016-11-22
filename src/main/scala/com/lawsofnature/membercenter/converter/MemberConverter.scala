package com.lawsofnature.membercenter.converter

import RpcMember.MemberIdentity
import com.lawsofnature.repo.TmMemberIdentityRow

/**
  * Created by fangzhongwei on 2016/11/22.
  */
object MemberConverter {
  def convert(tmMemberIdentityRows: Seq[TmMemberIdentityRow]): Array[MemberIdentity] = {
    val array: Array[MemberIdentity] = new Array[MemberIdentity](tmMemberIdentityRows.length)
    var memberIdentity: MemberIdentity = null
    var tmMemberIdentityRow: TmMemberIdentityRow = null
    for (i <- 0 to array.length - 1) {
      tmMemberIdentityRow = tmMemberIdentityRows(i)
      memberIdentity = new MemberIdentity(tmMemberIdentityRow.id, tmMemberIdentityRow.memberId, tmMemberIdentityRow.identity, tmMemberIdentityRow.pid, tmMemberIdentityRow.gmtCreate.getTime)
    }
    array
  }
}
