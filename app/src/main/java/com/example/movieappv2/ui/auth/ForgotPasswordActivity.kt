package com.example.movieappv2.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movieappv2.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Xử lý nút quay lại
        binding.ivBack.setOnClickListener {
            finish()
        }

        // Xử lý nút gửi link
        binding.btnSendLink.setOnClickListener {
            handleForgotPassword()
        }
    }

    private fun handleForgotPassword() {
        val email = binding.etEmailForgot.text.toString().trim()

        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email của bạn", Toast.LENGTH_SHORT).show()
            return
        }

        // Gọi hàm sendPasswordResetEmail của Firebase
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Nếu thành công, thông báo cho người dùng và đóng màn hình
                    Toast.makeText(this, "Link đặt lại mật khẩu đã được gửi đến email của bạn.", Toast.LENGTH_LONG).show()
                    finish() // Quay lại màn hình Login
                } else {
                    // Nếu thất bại, hiển thị lỗi
                    Toast.makeText(this, "Lỗi: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}