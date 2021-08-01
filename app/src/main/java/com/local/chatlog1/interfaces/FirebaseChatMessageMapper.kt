package com.local.chatlog1.interfaces

import com.local.chatlog1.model.ChatMessage
import com.local.chatlog1.model.DatabaseChatMessage

interface FirebaseChatMessageMapper {
    fun fromEntity(entity: DatabaseChatMessage): ChatMessage
    fun fromData(data: ChatMessage): DatabaseChatMessage
}