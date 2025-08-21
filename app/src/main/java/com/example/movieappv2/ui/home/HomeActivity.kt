package com.example.movieappv2.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieappv2.R
import com.example.movieappv2.data.model.Movie
import com.example.movieappv2.databinding.ActivityHomeBinding
import com.example.movieappv2.ui.adapters.BannerAdapter
import com.example.movieappv2.ui.home.MoviePosterAdapter
import com.example.movieappv2.ui.detail.DetailActivity
import com.example.movieappv2.ui.favorites.FavoritesActivity
import com.example.movieappv2.ui.movielist.MovieListActivity
import com.example.movieappv2.ui.search.SearchActivity
import com.example.movieappv2.ui.settings.SettingsActivity
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var popularMoviesAdapter: MoviePosterAdapter
    private lateinit var bannerAdapter: BannerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gọi tất cả các hàm cài đặt
        setupHeader()
        setupViews()
        observeViewModel()
        setupBottomNav()
        setupClickListeners()

        // Ra lệnh cho ViewModel tải dữ liệu
        homeViewModel.fetchInitialData()
    }
    override fun onResume() {
        super.onResume()
        // Hàm này được gọi mỗi khi Activity này quay trở lại foreground
        // (ví dụ: sau khi đóng SettingsActivity hoặc FavoritesActivity).

        // Chúng ta sẽ tìm đến item "Home" trong menu của BottomNavigationView
        // và đặt nó vào trạng thái được chọn (checked).
        binding.bottomNavigation.menu.findItem(R.id.nav_home).isChecked = true
    }

    // Hàm tải và hiển thị thông tin người dùng
    private fun setupHeader() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val greetingName = if (it.displayName.isNullOrEmpty()) {
                it.email?.split("@")?.get(0) ?: "User"
            } else {
                it.displayName
            }
            binding.tvGreetingName.text = "Hi, $greetingName"

            Glide.with(this)
                .load(it.photoUrl)
                .placeholder(R.drawable.placeholder_avatar)
                .error(R.drawable.placeholder_avatar)
                .into(binding.ivAvatar)
        }
    }

    // Hàm xử lý khi người dùng click vào một phim bất kỳ
    private fun handleMovieClick(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("MOVIE_ID", movie.id)
        startActivity(intent)
    }

    // Hàm TỔNG để cài đặt tất cả các View
    private fun setupViews() {
        // Cài đặt RecyclerView cho "Recommended"
        popularMoviesAdapter = MoviePosterAdapter { movie ->
            handleMovieClick(movie)
        }
        binding.rvRecommended.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularMoviesAdapter
        }

        // Cài đặt ViewPager2 cho Banner
        bannerAdapter = BannerAdapter { movie ->
            handleMovieClick(movie)
        }
        binding.viewPagerBanner.adapter = bannerAdapter

        // Các tùy chọn thêm để ViewPager2 trông đẹp hơn
        binding.viewPagerBanner.offscreenPageLimit = 3
        binding.viewPagerBanner.clipToPadding = false
        binding.viewPagerBanner.clipChildren = false
        binding.viewPagerBanner.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

    // Lắng nghe dữ liệu từ ViewModel
    private fun observeViewModel() {
        homeViewModel.popularMovies.observe(this) { movies ->
            popularMoviesAdapter.submitList(movies)
        }

        homeViewModel.bannerMovies.observe(this) { bannerMovies ->
            bannerAdapter.submitList(bannerMovies)
        }
    }

    private fun setupBottomNav() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Đã ở màn hình Home rồi, không cần làm gì
                    true
                }
                R.id.nav_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    true
                }
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

    // Xử lý các click còn lại trên màn hình
    private fun setupClickListeners() {
        binding.ivSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        binding.tvSeeAllRecommended.setOnClickListener {
            startActivity(Intent(this, MovieListActivity::class.java))
        }
    }
}