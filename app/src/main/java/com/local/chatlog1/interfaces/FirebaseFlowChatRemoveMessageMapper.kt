package com.local.chatlog1.interfaces

import com.local.chatlog1.model.FirebaseFlowChatMessage
import com.local.chatlog1.model.FlowChatMessage

interface FirebaseFlowChatRemoveMessageMapper {
    fun fromEntity(entity: FirebaseFlowChatMessage.FirebaseFlowChatRemoveMessage): FlowChatMessage.FlowChatRemoveMessage
    fun fromData(data: FlowChatMessage.FlowChatRemoveMessage): FirebaseFlowChatMessage.FirebaseFlowChatRemoveMessage
}