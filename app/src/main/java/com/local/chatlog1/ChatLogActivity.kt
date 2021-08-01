package com.local.chatlog1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.local.chatlog1.databinding.ActivityChatLogBinding
import com.local.chatlog1.di.viewmodel.ViewModelFactory
import com.local.chatlog1.model.ChatMessage
import com.local.chatlog1.model.ChatRoom
import com.local.chatlog1.model.FlowChatMessage
import com.local.chatlog1.recyclerviewitems.UserMessageItem
import com.local.chatlog1.recyclerviewitems.UserMessagePartnerItem
import com.local.chatlog1.viewmodel.ChatLogViewModel
import com.xwray.groupie.GroupieAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChatLogActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatLogBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val chatLogViewModel: ChatLogViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ChatLogViewModel::class.java)
    }
    lateinit var chatRoom: ChatRoom
    private val disposable = CompositeDisposable()
    private val adapter = GroupieAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_log)
        chatRoom = intent.getParcelableExtra<ChatRoom>(UserListActivity.ROOM_KEY)
            ?: throw RuntimeException("Parcelable parse error")
        supportActionBar?.title = chatRoom.user2.userName

        binding()
        subscribeToNewMessage()
    }

    private fun binding() {
        binding.recyclerViewMessageLogChatLog.adapter = adapter
        binding.buttonSendMessageChatLog.setOnClickListener {
            if (binding.editTextMessageChatLog.text.isEmpty()) return@setOnClickListener

            sendMessage(
                chatRoom, ChatMessage(
                    chatLogViewModel.getCurrentUid(),
                    binding.editTextMessageChatLog.text.toString().trim(),
                    System.currentTimeMillis() / 1000
                )
            )
        }
    }

    private fun sendMessage(room: ChatRoom, chatMessage: ChatMessage) {
        val d = chatLogViewModel.sendMessage(
            room, chatMessage
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //binding.recyclerViewMessageLogChatLog.smoothScrollToPosition()
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            })
        binding.editTextMessageChatLog.text.clear()
        disposable.add(d)
    }

    private fun subscribeToNewMessage() {
        val d = chatLogViewModel.subscribeToNewMessages(chatRoom.roomId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                messageToView(it)
            }, {})
        disposable.add(d)
    }

    private fun messageToView(flowChatMessage: FlowChatMessage) {
        when (flowChatMessage) {
            is FlowChatMessage.FlowChatNewMessage -> {
                if (flowChatMessage.chatMessage.fromUid == chatLogViewModel.getCurrentUid())
                    adapter.add(UserMessageItem(chatRoom.user1, flowChatMessage))
                else
                    adapter.add(UserMessagePartnerItem(chatRoom.user2, flowChatMessage))

                binding.recyclerViewMessageLogChatLog.smoothScrollToPosition(adapter.itemCount - 1)
            }
            is FlowChatMessage.FlowChatRemoveMessage -> {
                removeMessage(flowChatMessage)
            }
        }
    }

    private fun removeMessage(flowChatMessage: FlowChatMessage.FlowChatRemoveMessage) {
        loop@ for (i in 0 until adapter.itemCount) {
            when (val data = adapter.getItem(i)) {
                is UserMessageItem -> {
                    if (data.flowMessage.key == flowChatMessage.key) {
                        adapter.remove(data)
                        break@loop
                    }
                }
                is UserMessagePartnerItem -> {
                    if (data.flowMessage.key == flowChatMessage.key) {
                        adapter.remove(data)
                        break@loop
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}