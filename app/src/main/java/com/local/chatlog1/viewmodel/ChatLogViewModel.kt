package com.local.chatlog1.viewmodel

import androidx.lifecycle.ViewModel
import com.local.chatlog1.interfaces.FirebaseChatMessageMapper
import com.local.chatlog1.interfaces.FirebaseChatRoomMapper
import com.local.chatlog1.model.*
import com.local.chatlog1.model.mapper.FirebaseChatMessageMapperImpl
import com.local.chatlog1.model.mapper.FirebaseChatRoomMapperImpl
import com.local.chatlog1.model.mapper.FirebaseFlowChatNewMessageMapperImpl
import com.local.chatlog1.model.mapper.FirebaseFlowChatRemoveMessageMapperImpl
import com.local.chatlog1.repository.Repository
import com.local.chatlog1.repository.RepositoryImpl
import com.local.chatlog1.repository.UserMessageData
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class ChatLogViewModel @Inject constructor(
    private val repository: Repository,
    private val chatRoomMapper: FirebaseChatRoomMapper,
    private val chatMessageMapper: FirebaseChatMessageMapper
) : ViewModel() {


    fun sendMessage(chatRoom: ChatRoom, chatMessage: ChatMessage): Single<UserMessageData> {
        return repository.sendMessage(
            chatRoomMapper.fromData(chatRoom),
            chatMessageMapper.fromData(chatMessage)
        )
    }

    fun getCurrentUid(): String {
        return repository.getCurrentUid()
    }

    fun subscribeToNewMessages(roomId: String): Observable<FlowChatMessage> {
        return repository.subscribeToNewMessages(roomId)
            .map {
                when (it) {
                    is FirebaseFlowChatMessage.FirebaseFlowChatNewMessage -> {
                        FirebaseFlowChatNewMessageMapperImpl().fromEntity(it)
                    }
                    is FirebaseFlowChatMessage.FirebaseFlowChatRemoveMessage -> {
                        FirebaseFlowChatRemoveMessageMapperImpl().fromEntity(it)
                    }
                }
            }
//            .ofType(FirebaseFlowChatMessage.FirebaseFlowChatNewMessage::class.java)
//            .map { FirebaseFlowChatNewMessageMapperImpl().fromEntity(it) }
//            .ofType(FirebaseFlowChatMessage.FirebaseFlowChatRemoveMessage::class.java)
//            .map { FirebaseFlowChatRemoveMessageMapperImpl().fromEntity(it) }
    }
}