package com.example.movieappv2.ui.home

import android.content.Intent
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieappv2.R
import com.example.movieappv2.data.model.Movie
import com.example.movieappv2.databinding.ActivityHomeBinding
import com.example.movieappv2.ui.detail.DetailActivity
import com.example.movieappv2.ui.favorites.FavoritesActivity
import com.example.movieappv2.ui.movielist.MovieListActivity
import com.example.movieappv2.ui.search.SearchActivity // Import SearchActivity
import com.example.movieappv2.ui.settings.SettingsActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var popularMoviesAdapter: MoviePosterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gọi hàm mới để cập nhật header
        setupHeader()

        setupRecyclerView()
        observeViewModel()
        setupBottomNav()
        setupClickListeners()

        homeViewModel.fetchPopularMovies()
    }
    private fun setupHeader() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Lấy tên hiển thị, nếu không có thì lấy phần đầu của email
            val greetingName = if (it.displayName.isNullOrEmpty()) {
                it.email?.split("@")?.get(0)
            } else {
                it.displayName
            }
            binding.tvGreetingName.text = "Hi, $greetingName" // Gán vào TextView tên

            // Dùng Glide để tải ảnh đại diện
            Glide.with(this)
                .load(it.photoUrl)
                .placeholder(R.drawable.placeholder_avatar) // Ảnh mặc định
                .error(R.drawable.placeholder_avatar)       // Ảnh khi lỗi
                .into(binding.ivAvatar) // Gán vào ImageView avatar
        }
    }

    private fun handleMovieClick(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("MOVIE_ID", movie.id)
        startActivity(intent)
    }

    private fun setupRecyclerView() {
        popularMoviesAdapter = MoviePosterAdapter(emptyList()) { movie ->
            handleMovieClick(movie)
        }
        binding.rvRecommended.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularMoviesAdapter
        }
    }

    private fun observeViewModel() {
        homeViewModel.popularMovies.observe(this) { movies ->
            popularMoviesAdapter.updateData(movies)
        }
    }

    private fun setupBottomNav() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                // Khi người dùng nhấn vào item Home (đang ở màn hình Home rồi)
                R.id.nav_home -> {
                    // Không làm gì cả, chỉ trả về true
                    true
                }

                // Khi người dùng nhấn vào item Search
                R.id.nav_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    true
                }

                // Khi người dùng nhấn vào item Favorites
                R.id.nav_favorites -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    true
                }

                R.id.nav_profile -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    // HÀM MỚI: Xử lý các click còn lại trên màn hình Home
    private fun setupClickListeners() {
        binding.ivSearch.setOnClickListener { // Giả sử icon kính lúp có id này
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        binding.tvSeeAllRecommended.setOnClickListener {
            val intent = Intent(this, MovieListActivity::class.java)
            // Sau này có thể truyền thêm loại danh sách, ví dụ:
            // intent.putExtra("LIST_TYPE", "popular")
            startActivity(intent)
        }
    }
}