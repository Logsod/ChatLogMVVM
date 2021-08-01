package com.local.chatlog1.model

sealed class FlowChatMessage() {
    class FlowChatNewMessage(val key : String, val chatMessage: ChatMessage) : FlowChatMessage()
    class FlowChatRemoveMessage(val key : String, val chatMessage: ChatMessage) : FlowChatMessage()
}