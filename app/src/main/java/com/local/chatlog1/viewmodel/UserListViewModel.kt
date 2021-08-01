package com.local.chatlog1.viewmodel

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.local.chatlog1.interfaces.FirebaseChatRoomMapper
import com.local.chatlog1.interfaces.FirebaseChatUserMapper
import com.local.chatlog1.model.ChatRoom
import com.local.chatlog1.model.ChatUser
import com.local.chatlog1.model.mapper.FirebaseChatRoomMapperImpl
import com.local.chatlog1.model.mapper.FirebaseChatUserMapperImpl
import com.local.chatlog1.repository.Repository
import com.local.chatlog1.repository.RepositoryImpl
import com.local.chatlog1.repository.UserListData
import com.local.chatlog1.repository.UserRoomData
import com.local.chatlog1.utils.log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserListViewModel @Inject constructor(
    private val repository: Repository,
    val firebaseChatRoomMapper: FirebaseChatRoomMapper,
    val firebaseChatUserMapper: FirebaseChatUserMapper
) : ViewModel() {


    private val _user = MutableLiveData<ChatUser>()
    val user: LiveData<ChatUser>
        get() = _user


    fun fetchCurrentUser(): Observable<ChatUser> =
        repository.fetchCurrentUser()
            .map { FirebaseChatUserMapperImpl().fromEntity(it) }
            .doOnSuccess { _user.value = it }
            .toObservable()


    fun fetchAllUsers(): Observable<UserListData> =
        repository.fetchAllUser()

    fun createOrReceiveChatRoom(user: ChatUser, partner: ChatUser): Observable<ChatRoom> {

        return repository.createOrReceiveChatRoom(
            firebaseChatUserMapper.fromData(user),
            firebaseChatUserMapper.fromData(partner)
        )
            .map { firebaseChatRoomMapper.fromEntity(it) }
            .toObservable()
    }


}