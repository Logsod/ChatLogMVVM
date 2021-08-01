package com.local.chatlog1.di.component

import com.local.chatlog1.ChatLogActivity
import com.local.chatlog1.LoginActivity
import com.local.chatlog1.RegisterActivity
import com.local.chatlog1.UserListActivity
import com.local.chatlog1.di.module.Module
import com.local.chatlog1.di.viewmodel.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [Module::class, ViewModelModule::class])
interface AppComponent {
    fun inject(activity: ChatLogActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: RegisterActivity)
    fun inject(activity: UserListActivity)
}