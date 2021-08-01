package com.local.chatlog1

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.local.chatlog1.databinding.ActivityRegisterBinding
import com.local.chatlog1.di.viewmodel.ViewModelFactory
import com.local.chatlog1.viewmodel.RegisterViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class RegisterActivity : AppCompatActivity() {
    private var profileImg: Uri? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val registerViewModel: RegisterViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }

    val disposable = CompositeDisposable()

    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        supportActionBar?.hide()

        binding()
    }

    private fun binding() {
        binding.textViewAlreadyHaveAccountRegister.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.imageViewProfileImageRegister.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.buttonRegisterRegister.setOnClickListener {
            val userName = binding.textViewUserNameRegister.text.toString().trim()
            val userEmail = binding.textViewEmailRegister.text.toString().trim()
            val userPassword = binding.textViewPasswordRegister.text.toString().trim()

            val d = registerViewModel.registerWithEmailAndPassword(
                userName,
                userEmail,
                userPassword,
                profileImg
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { success ->
                        val intent = Intent(applicationContext, UserListActivity::class.java)
                        startActivity(intent)
                    },
                    { error ->
                        Toast.makeText(this, error.message.toString(), Toast.LENGTH_SHORT).show()
                    },
                )
            disposable.add(d)
        }
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val target = binding.imageViewProfileImageRegister
            if (uri == null) return@registerForActivityResult
            profileImg = uri
            target.setImageURI(uri)

        }
}