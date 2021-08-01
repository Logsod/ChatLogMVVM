package com.local.chatlog1.model


sealed class FirebaseFlowChatMessage() {
    class FirebaseFlowChatNewMessage(val key : String, val chatMessage: DatabaseChatMessage) : FirebaseFlowChatMessage()
    class FirebaseFlowChatRemoveMessage(val key : String, val chatMessage: DatabaseChatMessage) : FirebaseFlowChatMessage()
}