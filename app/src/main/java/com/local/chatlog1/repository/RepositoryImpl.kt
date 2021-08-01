package com.local.chatlog1.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.local.chatlog1.model.*
import com.local.chatlog1.utils.getRoomIdByOtherUser
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

class RepositoryImpl() : Repository {


    override val auth = FirebaseAuth.getInstance()
    override var user: DatabaseChatUser = DatabaseChatUser()
    override fun getCurrentUser(): DatabaseChatUser {
        return user
    }

    override fun getCurrentUid(): String {
        return FirebaseAuth.getInstance().uid.toString()
    }

    override fun sendMessage(
        chatRoom: DatabaseChatRoom,
        chatMessage: DatabaseChatMessage
    ) = Single.create<UserMessageData> { emitter ->
        try {
            FirebaseDatabase.getInstance().getReference("/rooms/${chatRoom.roomId}/messages")
                .push().setValue(chatMessage)
                .addOnSuccessListener { emitter.onSuccess(UserMessageData.Success) }
                .addOnFailureListener { emitter.onError(it) }

        } catch (e: Exception) {
            emitter.onError(e)
        }
    }

    override fun subscribeToNewMessages(roomId: String) =
        Observable.create<FirebaseFlowChatMessage> { emitter ->
            FirebaseDatabase.getInstance().getReference("/rooms/${roomId}/messages")
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        val data = snapshot.getValue(DatabaseChatMessage::class.java)
                        val key = snapshot.key

                        if (data == null || key == null) {
                            emitter.onError(Exception("data is null data:${data} key:${key}"))
                        } else {
                            emitter.onNext(
                                FirebaseFlowChatMessage.FirebaseFlowChatNewMessage(
                                    key,
                                    data
                                )
                            )
                        }
                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        val data = snapshot.getValue(DatabaseChatMessage::class.java)
                        val key = snapshot.key

                        if (data == null || key == null) {
                            emitter.onError(Exception("data is null data:${data} key:${key}"))
                        } else {
                            emitter.onNext(
                                FirebaseFlowChatMessage.FirebaseFlowChatRemoveMessage(
                                    key,
                                    data
                                )
                            )
                        }
                    }
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }

    override fun fetchAllUser() = Observable.create<UserListData> { emitter ->
        FirebaseDatabase.getInstance().getReference("/users")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    try {
                        val data = snapshot.getValue(DatabaseChatUser::class.java)
                        if (data != null) {
                            emitter.onNext(UserListData.Added(data))
                        } else {
                            emitter.onError(Exception("db error user data is null"))
                        }
                    } catch (e: Exception) {
                        emitter.onError(e)
                    }
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }
                override fun onChildRemoved(snapshot: DataSnapshot) {
                    try {
                        val data = snapshot.getValue(DatabaseChatUser::class.java)
                        if (data != null) {
                            emitter.onNext(UserListData.Remove(data))
                        } else {
                            emitter.onError(Exception("db error user data is null"))
                        }
                    } catch (e: Exception) {
                        emitter.onError(e)
                    }
                }
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }
                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }
            })
    }


    override fun createOrReceiveChatRoom(
        user: DatabaseChatUser,
        partner: DatabaseChatUser
    ) = Single.create<DatabaseChatRoom> { emitter ->
        if (user.uid.isEmpty() || partner.uid.isEmpty()) {
            emitter.onError(Exception("uid is empty"))
            return@create
        }

        val roomId: String = user.getRoomIdByOtherUser(partner.uid)!!

        try {
            FirebaseDatabase.getInstance().getReference("/rooms/${roomId}")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val room = snapshot.getValue(DatabaseChatRoom::class.java)
                        if (room == null) {
                            val chatRoom = DatabaseChatRoom(
                                roomId, user, partner, System.currentTimeMillis() / 1000
                            )
                            FirebaseDatabase.getInstance().getReference("/rooms/${roomId}")
                                .setValue(chatRoom)
                                .addOnSuccessListener {
                                    emitter.onSuccess(chatRoom)
                                }
                                .addOnFailureListener {
                                    emitter.onError(it)
                                }
                        } else {
                            //user 1 is always current user, user 2 is always a chat partner
                            // if we entered a room that was already created, the db records may not match
                            if (room.user1.uid != user.uid)
                                emitter.onSuccess(
                                    DatabaseChatRoom(
                                        room.roomId,
                                        room.user2,
                                        room.user1,
                                        room.createdOn
                                    )
                                )
                            else
                                emitter.onSuccess(room)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        emitter.onError(error.toException())
                    }

                })
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }

    override fun fetchCurrentUser() = Single.create<DatabaseChatUser> { emitter ->
        if (auth.uid == null) {
            emitter.onError(Exception("you are not login in"))
        }
        else {

            val data = FirebaseDatabase.getInstance().getReference("/users/${auth.uid}")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val data = snapshot.getValue(DatabaseChatUser::class.java)
                        if (data == null)
                            emitter.onError(Exception("db error user is null"))
                        else {
                            user = data
                            emitter.onSuccess(user)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        emitter.onError(error.toException())
                    }

                })
        }

    }

    override fun loginWithEmailAndPassword(
        email: String,
        password: String
    ) = Single.create<UserAuthData>
    { emitter ->
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val ref = FirebaseDatabase.getInstance().getReference("/users/${auth.uid}")
                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val data = snapshot.getValue(DatabaseChatUser::class.java)
                        if (data != null) {
                            user = data
                            emitter.onSuccess(UserAuthData.Success)
                        } else {
                            emitter.onError(Exception("user data is null"))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
            }
            .addOnFailureListener {
                emitter.onError(it)
            }
    }

    override fun registerWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
        profileImage: Uri?
    ) =
        Single.create<UserAuthData> { emitter ->

            var imgUrl = ""
            auth.createUserWithEmailAndPassword(email, password)
                .addOnFailureListener {
                    emitter.onError(it)
                }
                .addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        if (profileImage != null) {
                            val fileName = UUID.randomUUID().toString()
                            val ref = FirebaseStorage.getInstance()
                                .getReference("/images/{${auth.uid}}/$fileName")
                            ref.putFile(profileImage)
                                .addOnFailureListener {
                                    emitter.onError(it)
                                }
                                .addOnSuccessListener { uploadTask ->
                                    uploadTask.storage.downloadUrl.addOnSuccessListener {
                                        imgUrl = it.toString()
                                        FirebaseDatabase.getInstance()
                                            .getReference("/users/${auth.uid}")
                                            .setValue(DatabaseChatUser(auth.uid!!, name, imgUrl))
                                            .addOnFailureListener { e ->
                                                emitter.onError(e)
                                            }
                                            .addOnSuccessListener {
                                                emitter.onSuccess(UserAuthData.Success)
                                            }

                                    }

                                }
                        } else {
                            FirebaseDatabase.getInstance()
                                .getReference("/users/${auth.uid}")
                                .setValue(DatabaseChatUser(auth.uid!!, name, imgUrl))
                                .addOnFailureListener { e ->
                                    emitter.onError(e)
                                }
                                .addOnSuccessListener {
                                    emitter.onSuccess(UserAuthData.Success)
                                }
                        }
                    }

                }
        }
}