package com.local.chatlog1.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.local.chatlog1.repository.Repository
import com.local.chatlog1.repository.RepositoryImpl
import com.local.chatlog1.repository.UserAuthData
import io.reactivex.Observable
import javax.inject.Inject

class RegisterViewModel @Inject constructor(private val repository : Repository) : ViewModel() {

    fun registerWithEmailAndPassword(userName: String, userEmail: String, userPassword: String, profileImg: Uri?): Observable<UserAuthData> =
         repository.registerWithEmailAndPassword(userName,userEmail,userPassword,profileImg)
             .toObservable()
}