package com.local.chatlog1.repository

import com.local.chatlog1.model.DatabaseChatRoom
import com.local.chatlog1.model.DatabaseChatUser

sealed class UserListData {
    class Added(val chatUser: DatabaseChatUser) : UserListData()
    class Remove(val chatUser: DatabaseChatUser) : UserListData()
}

sealed class UserMessageData {
    object Success : UserMessageData()
}

sealed class UserAuthData {
    object Success : UserAuthData()
}

sealed class UserRoomData {
    class Success(val chatRoom: DatabaseChatRoom) : UserRoomData()
}
