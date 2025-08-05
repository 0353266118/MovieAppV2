package com.example.movieappv2.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.movieappv2.R
import com.example.movieappv2.data.model.MovieDetail
import com.example.movieappv2.databinding.ActivityDetailBinding
import com.example.movieappv2.ui.adapters.CastAdapter
import com.example.movieappv2.utils.Constants

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private var currentMovie: MovieDetail? = null
    private lateinit var castAdapter: CastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.getIntExtra("MOVIE_ID", -1)
        if (movieId != -1) {
            detailViewModel.fetchMovieDetails(movieId)
            detailViewModel.checkFavoriteStatus(movieId)
        }

        observeViewModel()
        // Bật lại hàm này
        setupClickListeners()
        setupCastRecyclerView()
        observeViewModel()
        if (movieId != -1) {
            detailViewModel.fetchMovieDetails(movieId)
            detailViewModel.checkFavoriteStatus(movieId)
            // Ra lệnh lấy danh sách diễn viên
            detailViewModel.fetchMovieCredits(movieId)
        }
    }
    private fun setupCastRecyclerView() {
        castAdapter = CastAdapter()
        binding.rvCast.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = castAdapter
        }
    }

    private fun observeViewModel() {
        detailViewModel.movieDetail.observe(this) { movieDetail ->
            currentMovie = movieDetail

            // Cập nhật giao diện (giữ nguyên)
            binding.tvTitleDetail.text = movieDetail.title
            binding.tvOverview.text = movieDetail.overview
            binding.tvRating.text = String.format("%.1f", movieDetail.voteAverage)
            val backdropUrl = Constants.IMAGE_BASE_URL + movieDetail.backdropPath
            Glide.with(this).load(backdropUrl).into(binding.ivBackdrop)
        }

        // Bật lại phần này
        detailViewModel.isFavorite.observe(this) { isFav ->
            updateFavoriteIcon(isFav)

        }
        detailViewModel.cast.observe(this) { castList ->
            castAdapter.submitList(castList)
        }
    }

    // Bật lại hàm này
    private fun setupClickListeners() {
        // Xử lý nút quay lại
        binding.ivBackArrow.setOnClickListener {
            // finish() sẽ đóng Activity hiện tại và quay lại màn hình trước đó
            finish()
        }

        // Xử lý nút yêu thích
        binding.ivFavorite.setOnClickListener {
            currentMovie?.let { movie ->
                detailViewModel.toggleFavorite(movie)
            }
        }
    }

    // Bật lại hàm này
    private fun updateFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.ivFavorite.setImageResource(R.drawable.ic_favorite_filled)
            binding.ivFavorite.setColorFilter(Color.RED)
        } else {
            binding.ivFavorite.setImageResource(R.drawable.ic_favorite_border)
            // Xóa bộ lọc màu để nó trở về màu trắng gốc (được định nghĩa bằng app:tint trong XML)
            binding.ivFavorite.clearColorFilter()
        }
    }
}