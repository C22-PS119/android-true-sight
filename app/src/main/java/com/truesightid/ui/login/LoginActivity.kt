package com.truesightid.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.inyongtisto.myhelper.extension.*
import com.truesightid.data.source.local.entity.UserEntity
import com.truesightid.data.source.remote.StatusResponse
import com.truesightid.data.source.remote.request.LoginRequest
import com.truesightid.databinding.ActivityLoginBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.activity.ForgotPasswordActivity
import com.truesightid.ui.activity.SignupActivity
import com.truesightid.ui.main.MainActivity
import com.truesightid.utils.Prefs
import com.truesightid.utils.VotesSeparator

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]



        binding.btnLogin.setOnClickListener {
            login()
        }

        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        binding.register.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

    }

    private fun login() {
        val request = LoginRequest(
            binding.edtEmail.editText?.text.toString(),
            binding.edtPassword.editText?.text.toString()
        )

        viewModel.login(request).observe(this) { user ->
            showLoading()
            when (user.status) {
                StatusResponse.SUCCESS -> {
                    dismisLoading()
                    Prefs.isLogin = true

                    val response = user.body
                    val responseData = response.data
                    val userData = responseData?.user
                    if (userData != null) {
                        Prefs.setUser(
                            UserEntity(
                                userData.id.toString(),
                                responseData.apiKey.toString(),
                                userData.username.toString(),
                                userData.email.toString(),
                                userData.password.toString(),
                                VotesSeparator.separate(userData.votes.toString())
                            )
                        )
                    }
                    pushActivity(MainActivity::class.java)
                }
                StatusResponse.ERROR -> {
                    toastError(user.message ?: "Error")
                }
                StatusResponse.EMPTY -> {
                    toastInfo("Empty Response")
                }
            }
        }
    }
}