package com.local.chatlog1.model.mapper

import com.local.chatlog1.interfaces.FirebaseFlowChatNewMessageMapper
import com.local.chatlog1.model.FirebaseFlowChatMessage
import com.local.chatlog1.model.FlowChatMessage


class FirebaseFlowChatNewMessageMapperImpl : FirebaseFlowChatNewMessageMapper {
    override fun fromEntity(entity: FirebaseFlowChatMessage.FirebaseFlowChatNewMessage): FlowChatMessage.FlowChatNewMessage {
        return FlowChatMessage.FlowChatNewMessage(
            key = entity.key,
            chatMessage = FirebaseChatMessageMapperImpl().fromEntity(entity.chatMessage)
        )
    }

    override fun fromData(data: FlowChatMessage.FlowChatNewMessage): FirebaseFlowChatMessage.FirebaseFlowChatNewMessage {
        return FirebaseFlowChatMessage.FirebaseFlowChatNewMessage(
            key = data.key,
            chatMessage = FirebaseChatMessageMapperImpl().fromData(data.chatMessage)
        )
    }

}