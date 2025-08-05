package com.example.movieappv2.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
//import com.example.movieapp.databinding.ActivityLoginBinding
//import com.example.movieapp.ui.home.HomeActivity
import com.example.movieappv2.databinding.ActivityLoginBinding
import com.example.movieappv2.ui.auth.AuthViewModel
import com.example.movieappv2.ui.home.HomeActivity

//import com.example.movieapp.uiView.auth.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        authViewModel.user.observe(this) { firebaseUser ->
            if (firebaseUser != null) {
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                goToHomeActivity()
            }
        }
        authViewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun setupClickListeners() {
        // Sự kiện cho nút "Continue"
        binding.btnContinue.setOnClickListener {
            handleLogin()
        }

//        // Sự kiện cho nút quay lại
//        binding.ivBack.setOnClickListener {
//            finish()
//        }
//
//        // Sự kiện đi đến trang đăng ký
//        binding.tvGoToSignUp.setOnClickListener {
//            val intent = Intent(this, RegisterActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun handleLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show()
            return
        }

        authViewModel.login(email, password)
    }

    private fun goToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}