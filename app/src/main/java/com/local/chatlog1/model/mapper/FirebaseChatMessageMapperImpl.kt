package com.local.chatlog1.model.mapper

import com.local.chatlog1.interfaces.FirebaseChatMessageMapper
import com.local.chatlog1.model.ChatMessage
import com.local.chatlog1.model.DatabaseChatMessage

class FirebaseChatMessageMapperImpl() : FirebaseChatMessageMapper {
    override fun fromEntity(entity: DatabaseChatMessage): ChatMessage {
        return ChatMessage(
            fromUid = entity.fromUid, message = entity.message, timestamp = entity.timestamp
        )
    }

    override fun fromData(data: ChatMessage): DatabaseChatMessage {
        return DatabaseChatMessage(
            fromUid = data.fromUid,
            message = data.message,
            timestamp = data.timestamp
        )
    }
}