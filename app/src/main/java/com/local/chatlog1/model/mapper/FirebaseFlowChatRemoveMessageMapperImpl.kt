package com.local.chatlog1.model.mapper

import com.local.chatlog1.interfaces.FirebaseFlowChatRemoveMessageMapper
import com.local.chatlog1.model.FirebaseFlowChatMessage
import com.local.chatlog1.model.FlowChatMessage

class FirebaseFlowChatRemoveMessageMapperImpl : FirebaseFlowChatRemoveMessageMapper {
    override fun fromEntity(entity: FirebaseFlowChatMessage.FirebaseFlowChatRemoveMessage): FlowChatMessage.FlowChatRemoveMessage {
        return FlowChatMessage.FlowChatRemoveMessage(
            key = entity.key,
            FirebaseChatMessageMapperImpl().fromEntity(entity = entity.chatMessage)
        )
    }

    override fun fromData(data: FlowChatMessage.FlowChatRemoveMessage): FirebaseFlowChatMessage.FirebaseFlowChatRemoveMessage {
        return FirebaseFlowChatMessage.FirebaseFlowChatRemoveMessage(
            key = data.key,
            chatMessage = FirebaseChatMessageMapperImpl().fromData(data.chatMessage)
        )
    }
}