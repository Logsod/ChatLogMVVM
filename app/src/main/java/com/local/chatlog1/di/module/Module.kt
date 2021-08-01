package com.local.chatlog1.di.module

import com.local.chatlog1.interfaces.FirebaseChatMessageMapper
import com.local.chatlog1.interfaces.FirebaseChatRoomMapper
import com.local.chatlog1.interfaces.FirebaseChatUserMapper
import com.local.chatlog1.model.mapper.FirebaseChatMessageMapperImpl
import com.local.chatlog1.model.mapper.FirebaseChatRoomMapperImpl
import com.local.chatlog1.model.mapper.FirebaseChatUserMapperImpl
import com.local.chatlog1.repository.Repository
import com.local.chatlog1.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class Module {
    @Provides
    @Singleton
    fun provideRepository() : Repository
    {
        return RepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideFirebaseChatMessageMapper(): FirebaseChatMessageMapper {
        return FirebaseChatMessageMapperImpl()
    }

    @Provides
    @Singleton
    fun provideFirebaseChatUserMapper(): FirebaseChatUserMapper {
        return FirebaseChatUserMapperImpl()
    }

    @Provides
    @Singleton
    fun provideFirebaseChatRoomMapper(): FirebaseChatRoomMapper {
        return FirebaseChatRoomMapperImpl()
    }

}