package com.local.chatlog1.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.local.chatlog1.model.DatabaseChatMessage
import com.local.chatlog1.model.DatabaseChatRoom
import com.local.chatlog1.model.DatabaseChatUser
import com.local.chatlog1.model.FirebaseFlowChatMessage
import com.local.chatlog1.model.mapper.FirebaseChatUserMapperImpl
import io.reactivex.Observable
import io.reactivex.Single

interface Repository {
    val auth: FirebaseAuth
    var user: DatabaseChatUser
    fun getCurrentUser(): DatabaseChatUser
    fun getCurrentUid(): String
    fun sendMessage(
        chatRoom: DatabaseChatRoom,
        chatMessage: DatabaseChatMessage
    ): Single<UserMessageData>

    fun subscribeToNewMessages(roomId: String): Observable<FirebaseFlowChatMessage>
    fun fetchAllUser(): Observable<UserListData>
    fun createOrReceiveChatRoom(
        user: DatabaseChatUser,
        partner: DatabaseChatUser
    ): Single<DatabaseChatRoom>

    fun fetchCurrentUser(): Single<DatabaseChatUser>
    fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Single<UserAuthData>

    fun registerWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
        profileImage: Uri?
    ): Single<UserAuthData>
}