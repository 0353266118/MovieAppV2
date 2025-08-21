package com.example.movieappv2.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
//import com.example.movieapp.databinding.ActivityLoginBinding
//import com.example.movieapp.ui.home.HomeActivity
import com.example.movieappv2.databinding.ActivityLoginBinding
import com.example.movieappv2.ui.auth.AuthViewModel
import com.example.movieappv2.ui.home.HomeActivity

//import com.example.movieapp.uiView.auth.RegisterActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.example.movieappv2.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider



// class đăng nhập xử lí code xml
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    // 1. Khai báo GoogleSignInClient
    private lateinit var googleSignInClient: GoogleSignInClient

    // 2. Khai báo ActivityResultLauncher
    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Đăng nhập Google thành công, bây giờ lấy token để xác thực với Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Đăng nhập Google thất bại
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 3. Cấu hình Google Sign-In
        configureGoogleSignIn()

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
        // Sự kiện đi đến trang đăng ký
        binding.tvGoToSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
        // 4. Kích hoạt và gán sự kiện cho nút Google
        binding.btnGoogle.visibility = View.VISIBLE // Đảm bảo nút được hiển thị
        binding.btnGoogle.setOnClickListener {
            signInWithGoogle()
        }
    }
    private fun configureGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Lấy token để xác thực với Firebase
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    // 5. Hàm xác thực với Firebase sau khi đăng nhập Google thành công
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Đăng nhập Firebase thành công, thông báo và chuyển màn hình NGAY LẬP TỨC
                    Toast.makeText(this, "Đăng nhập bằng Google thành công!", Toast.LENGTH_SHORT).show()
                    goToHomeActivity() // GỌI TRỰC TIẾP Ở ĐÂY, KHÔNG CHỜ LiveData NỮA
                } else {
                    // Đăng nhập Firebase thất bại, hiển thị lỗi chi tiết hơn
                    Toast.makeText(this, "Firebase authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
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