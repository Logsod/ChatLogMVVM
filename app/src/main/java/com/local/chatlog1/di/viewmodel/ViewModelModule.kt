package com.local.chatlog1.di.viewmodel

import androidx.lifecycle.ViewModel
import com.local.chatlog1.viewmodel.ChatLogViewModel
import com.local.chatlog1.viewmodel.LoginViewModel
import com.local.chatlog1.viewmodel.RegisterViewModel
import com.local.chatlog1.viewmodel.UserListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Suppress
@Module
internal abstract class ViewModelModule {
    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    internal abstract fun bindRegisterActivityViewModel(registerViewModel: RegisterViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(UserListViewModel::class)
    internal abstract fun bindUserListActivityViewModel(userListViewModel: UserListViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(ChatLogViewModel::class)
    internal abstract fun bindChatMessageActivityViewModel(chatLogViewModel: ChatLogViewModel): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginActivityViewModel(loginViewModelImpl: LoginViewModel): ViewModel

}