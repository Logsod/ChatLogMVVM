package com.local.chatlog1

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.local.chatlog1.databinding.ActivityUserListBinding
import com.local.chatlog1.di.viewmodel.ViewModelFactory
import com.local.chatlog1.model.ChatRoom
import com.local.chatlog1.model.ChatUser
import com.local.chatlog1.model.mapper.FirebaseChatUserMapperImpl
import com.local.chatlog1.recyclerviewitems.UserItem
import com.local.chatlog1.repository.UserListData
import com.local.chatlog1.viewmodel.UserListViewModel
import com.xwray.groupie.GroupieAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserListActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserListBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val userListViewModel: UserListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(UserListViewModel::class.java)
    }
    private val disposable = CompositeDisposable()
    private val firebaseChatUserMapper = FirebaseChatUserMapperImpl()
    private val adapter = GroupieAdapter()

    companion object {
        const val ROOM_KEY = "ROOM_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)
        supportActionBar?.title = ""
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_list)

        binding()
        fetchCurrentUser()
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        adapter.setOnItemClickListener { item, view ->
            val row = item as UserItem
            val d = userListViewModel.createOrReceiveChatRoom(
                userListViewModel.user.value!!,
                ChatUser(item.uid, item.name, item.profileImg)
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    startChatLogActivity(it)
                }, {
                })
            disposable.add(d)
        }
    }

    private fun binding() {
        binding.recyclerViewUserListUserList.adapter = adapter
        userListViewModel.user.observe(this, Observer {
            supportActionBar?.title = it.userName
        })
    }

    private fun startChatLogActivity(chatRoom: ChatRoom) {
        val intent = Intent(applicationContext, ChatLogActivity::class.java)
        intent.putExtra(ROOM_KEY, chatRoom)
        startActivity(intent)
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    private fun fetchCurrentUser() {
        val d = userListViewModel.fetchCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    fetchAllUsers()
                }, {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    startRegisterActivity()
                })
        disposable.add(d)
    }

    private fun fetchAllUsers() {
        val d = userListViewModel.fetchAllUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is UserListData.Added -> {
                        val user = firebaseChatUserMapper.fromEntity(it.chatUser)
                        if (user.uid != userListViewModel.user.value?.uid)
                            adapter.add(
                                UserItem(
                                    user.uid,
                                    user.userName,
                                    user.userImageUri
                                )
                            )
                    }
                    is UserListData.Remove -> {
                        val user = firebaseChatUserMapper.fromEntity(it.chatUser)
                        removeFromAdapter(user.uid)
                    }
                }
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            })
        disposable.add(d)
    }

    private fun removeFromAdapter(uid: String) {
        loop@ for (i in 0 until adapter.itemCount) {
            val data = adapter.getItem(i) as UserItem
            if (data.uid == uid) {
                adapter.remove(data)
                adapter.notifyDataSetChanged()
                break@loop
            }
        }
    }


    private fun startRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        startRegisterActivity()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_sign_out) {
            signOut()
        }
        return super.onOptionsItemSelected(item)
    }
}