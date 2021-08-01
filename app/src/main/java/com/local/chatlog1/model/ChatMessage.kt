package com.local.chatlog1.model

data class ChatMessage(val fromUid: String, val message: String, val timestamp: Long) {
    constructor() : this("", "", 0)
}