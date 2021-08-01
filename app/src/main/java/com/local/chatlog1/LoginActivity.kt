package com.local.chatlog1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.local.chatlog1.databinding.ActivityLoginBinding
import com.local.chatlog1.di.viewmodel.ViewModelFactory
import com.local.chatlog1.repository.RepositoryImpl
import com.local.chatlog1.utils.log
import com.local.chatlog1.viewmodel.LoginViewModel
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private val disposable = CompositeDisposable()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding()
    }

    private fun binding() {
        binding.textViewBackToRegisterLogin.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        binding.buttonLoginLogin.setOnClickListener {
            val email = binding.editTextEmailLogin.text.toString().trim()
            val password = binding.editTextPasswordLogin.text.toString().trim()

            val d = loginViewModel.loginWithEmailAndPassword(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        onLoginSuccess()
                    },
                    {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    })
            disposable.add(d)
        }
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    private fun onLoginSuccess() {
        val intent = Intent(this, UserListActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}