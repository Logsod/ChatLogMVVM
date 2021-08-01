package com.local.chatlog1.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatRoom(
    val roomId: String,
    val user1: ChatUser,
    val user2: ChatUser,
    val createdOn: Long
) : Parcelable {
    constructor() : this("", ChatUser(), ChatUser(), 0)
}
