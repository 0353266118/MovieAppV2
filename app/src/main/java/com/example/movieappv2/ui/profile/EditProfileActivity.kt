package com.example.movieappv2.ui.profile

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.movieappv2.R
import com.example.movieappv2.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage

//màn hình edit profile với chỉnh tên, ảnh đại diện

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private var imageUri: Uri? = null // Biến để lưu trữ Uri của ảnh người dùng chọn

    // 1. Khai báo ActivityResultLauncher
    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it // Lưu lại Uri
            // Hiển thị ảnh người dùng vừa chọn lên ImageView
            Glide.with(this).load(it).into(binding.ivProfileAvatar)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserInfo()
        setupClickListeners()
    }

    // Hàm để tải và hiển thị thông tin người dùng hiện tại
    private fun loadUserInfo() {
        currentUser?.let { user ->
            // Hiển thị Tên
            binding.etUsername.setText(user.displayName)
            // Hiển thị Email
            binding.etEmail.setText(user.email)

            // Hiển thị ảnh đại diện bằng Glide
            Glide.with(this)
                .load(user.photoUrl)
                .placeholder(R.drawable.placeholder_avatar) // Ảnh mặc định
                .error(R.drawable.placeholder_avatar)       // Ảnh khi lỗi
                .into(binding.ivProfileAvatar)
        }
    }

    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener { finish() }

        binding.btnSaveChanges.setOnClickListener {
            // Sửa lại hàm này để tải ảnh lên nếu có
            uploadProfileImageAndUpdate()
        }

        binding.ivProfileAvatar.setOnClickListener {
            // 2. Mở thư viện ảnh khi người dùng nhấn vào avatar
            selectImageLauncher.launch("image/*")
        }
    }

    // 3. Hàm mới để tải ảnh lên và sau đó cập nhật profile
    private fun uploadProfileImageAndUpdate() {
        val newUsername = binding.etUsername.text.toString().trim()
        if (newUsername.isEmpty()) {
            Toast.makeText(this, "Username không được để trống", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageUri != null) {
            // Nếu có ảnh mới, tải ảnh lên Firebase Storage trước
            val storageRef = FirebaseStorage.getInstance().reference.child("avatars/${currentUser?.uid}")
            storageRef.putFile(imageUri!!)
                .addOnSuccessListener {
                    // Tải lên thành công, lấy URL của ảnh
                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        // Có URL rồi, giờ mới cập nhật profile
                        updateUserProfile(newUsername, downloadUrl)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Lỗi tải ảnh: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            // Nếu không có ảnh mới, chỉ cập nhật username
            updateUserProfile(newUsername, null)
        }
    }

    // 4. Hàm cập nhật profile, nhận vào cả username và photoUrl (có thể null)
    private fun updateUserProfile(username: String, photoUrl: Uri?) {
        val profileUpdatesBuilder = UserProfileChangeRequest.Builder().setDisplayName(username)

        photoUrl?.let {
            profileUpdatesBuilder.setPhotoUri(it)
        }

        val profileUpdates = profileUpdatesBuilder.build()

        currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Lỗi: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}