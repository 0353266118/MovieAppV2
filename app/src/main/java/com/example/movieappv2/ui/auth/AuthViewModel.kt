package com.example.movieappv2.ui.auth



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {

    // Khởi tạo Firebase Auth instance
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // LiveData để giữ trạng thái đăng nhập
    // FirebaseUser? có nghĩa là: có thể là một đối tượng User (đăng nhập thành công) hoặc null (thất bại/chưa đăng nhập)
    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> = _user

    // LiveData để thông báo lỗi
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun login(email: String, password: String) {
        // Gọi hàm signIn của Firebase
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Đăng nhập thành công, cập nhật LiveData với thông tin user
                    _user.value = auth.currentUser
                } else {
                    // Đăng nhập thất bại, cập nhật LiveData lỗi
                    _error.value = task.exception?.message ?: "Lỗi đăng nhập không xác định"
                }
            }
    }

    // Thêm hàm đăng ký để dùng cho RegisterActivity sau này
    fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _user.value = auth.currentUser
                } else {
                    _error.value = task.exception?.message ?: "Lỗi đăng ký không xác định"
                }
            }
    }
}