package com.local.chatlog1.model

data class DatabaseChatUser(val uid : String, val userName: String, val userImageUri: String) {
    constructor() : this( "","", "")
}