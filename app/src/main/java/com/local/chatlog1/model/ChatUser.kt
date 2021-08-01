package com.local.chatlog1.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatUser(val uid: String, val userName: String, val userImageUri: String) : Parcelable {
    constructor() : this("","", "")
}
