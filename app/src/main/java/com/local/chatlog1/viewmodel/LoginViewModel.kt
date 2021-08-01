package com.local.chatlog1.viewmodel

import androidx.lifecycle.ViewModel
import com.local.chatlog1.repository.Repository
import com.local.chatlog1.repository.RepositoryImpl
import com.local.chatlog1.repository.UserAuthData
import io.reactivex.Observable
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun loginWithEmailAndPassword(email: String, password: String): Observable<UserAuthData> {
        return repository.loginWithEmailAndPassword(email, password).toObservable()
    }

}