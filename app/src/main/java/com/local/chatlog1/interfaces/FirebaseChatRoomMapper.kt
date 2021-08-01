package com.local.chatlog1.interfaces

import com.local.chatlog1.model.ChatRoom
import com.local.chatlog1.model.DatabaseChatRoom

interface FirebaseChatRoomMapper {
    fun fromEntity(entity: DatabaseChatRoom): ChatRoom
    fun fromData(data: ChatRoom): DatabaseChatRoom
}