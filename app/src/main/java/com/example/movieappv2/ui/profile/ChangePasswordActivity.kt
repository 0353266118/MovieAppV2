// 1. Dòng package LUÔN LUÔN nằm ở trên cùng
package com.example.movieappv2.ui.profile

// 2. TẤT CẢ các dòng import phải nằm ngay sau dòng package
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movieappv2.databinding.ActivityChangePasswordBinding
import com.google.firebase.auth.FirebaseAuth

// 3. Khai báo class nằm sau tất cả các dòng import
class ChangePasswordActivity : AppCompatActivity() {

    // Khai báo các biến của class
    private lateinit var binding: ActivityChangePasswordBinding

    // Các hàm của class
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener { finish() }

        binding.btnChangeNow.setOnClickListener {
            val newPassword = binding.etNewPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (newPassword.length < 6) {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = FirebaseAuth.getInstance().currentUser
            user?.updatePassword(newPassword)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Lỗi: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}