package com.local.chatlog1.interfaces

import com.local.chatlog1.model.FirebaseFlowChatMessage
import com.local.chatlog1.model.FlowChatMessage

interface FirebaseFlowChatNewMessageMapper {
    fun fromEntity(entity: FirebaseFlowChatMessage.FirebaseFlowChatNewMessage): FlowChatMessage.FlowChatNewMessage
    fun fromData(data: FlowChatMessage.FlowChatNewMessage): FirebaseFlowChatMessage.FirebaseFlowChatNewMessage
}