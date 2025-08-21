package com.example.movieappv2.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.movieappv2.databinding.ActivitySettingsBinding
import com.example.movieappv2.ui.auth.LoginActivity
import com.example.movieappv2.ui.profile.ChangePasswordActivity
import com.example.movieappv2.ui.profile.EditProfileActivity
import com.google.firebase.auth.FirebaseAuth

// hàm  phần edit profile, thêm chức năng log out ra màn hình login

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.layoutEditProfile.setOnClickListener {

             startActivity(Intent(this, EditProfileActivity::class.java))
        }
        binding.layoutChangePassword.setOnClickListener {

             startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
        binding.layoutLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}