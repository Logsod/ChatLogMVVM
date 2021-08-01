package com.local.chatlog1.interfaces

import com.local.chatlog1.model.ChatUser
import com.local.chatlog1.model.DatabaseChatUser

interface FirebaseChatUserMapper {
    fun fromEntity(entity: DatabaseChatUser): ChatUser
    fun fromData(data: ChatUser): DatabaseChatUser
}