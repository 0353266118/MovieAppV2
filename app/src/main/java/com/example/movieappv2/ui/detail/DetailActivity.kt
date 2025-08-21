package com.example.movieappv2.ui.detail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.movieappv2.R
import com.example.movieappv2.data.model.MovieDetail
import com.example.movieappv2.data.model.Video
import com.example.movieappv2.databinding.ActivityDetailBinding
import com.example.movieappv2.ui.adapters.CastAdapter
import com.example.movieappv2.ui.adapters.ReviewAdapter
import com.example.movieappv2.utils.Constants
import java.text.SimpleDateFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private var currentMovie: MovieDetail? = null

    private lateinit var castAdapter: CastAdapter
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViews()
        observeViewModel()
        setupClickListeners()

        val movieId = intent.getIntExtra("MOVIE_ID", -1)
        if (movieId != -1) {
            detailViewModel.fetchMovieDetails(movieId)
            detailViewModel.checkFavoriteStatus(movieId)
            detailViewModel.fetchMovieCredits(movieId)
            detailViewModel.fetchMovieReviews(movieId)
            detailViewModel.fetchMovieTrailer(movieId)
        }
    }

    private fun setupRecyclerViews() {
        castAdapter = CastAdapter()
        binding.rvCast.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = castAdapter
        }

        reviewAdapter = ReviewAdapter()
        binding.rvReviews.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = reviewAdapter
        }
    }

    private fun observeViewModel() {
        detailViewModel.movieDetail.observe(this) { movieDetail ->
            currentMovie = movieDetail
            bindMovieDetail(movieDetail)
        }
        detailViewModel.isFavorite.observe(this) { isFav ->
            updateFavoriteIcon(isFav)
        }
        detailViewModel.cast.observe(this) { castList ->
            castAdapter.submitList(castList)
        }
        detailViewModel.reviews.observe(this) { reviewList ->
            reviewAdapter.submitList(reviewList)
        }
        detailViewModel.trailerVideo.observe(this) { video ->
            // Gán sự kiện click cho nút trailer VÀ kiểm tra xem có video không
            setupTrailerButton(video)
        }
    }

    private fun bindMovieDetail(movieDetail: MovieDetail) {
        binding.tvTitleDetail.text = movieDetail.title
        binding.tvOverview.text = movieDetail.overview
        binding.tvRating.text = String.format("%.1f", movieDetail.voteAverage)

        // --- BẮT ĐẦU THAY ĐỔI ---

        // Hiển thị đầy đủ ngày tháng năm, và định dạng lại cho đẹp
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
            val date = inputFormat.parse(movieDetail.releaseDate)
            binding.tvReleaseDate.text = if (date != null) outputFormat.format(date) else movieDetail.releaseDate
        } catch (e: Exception) {
            // Nếu có lỗi định dạng, hiển thị ngày gốc
            binding.tvReleaseDate.text = movieDetail.releaseDate
        }

        // Nối tên các thể loại lại với nhau
        binding.tvGenres.text = movieDetail.genres.joinToString(", ") { it.name }

        val backdropUrl = Constants.IMAGE_BASE_URL + movieDetail.backdropPath
        Glide.with(this).load(backdropUrl).into(binding.ivBackdrop)

        // --- KẾT THÚC THAY ĐỔI ---
    }

    private fun setupClickListeners() {
        binding.ivBackArrow.setOnClickListener {
            finish()
        }
        binding.ivFavorite.setOnClickListener {
            currentMovie?.let { movie ->
                detailViewModel.toggleFavorite(movie)
            }
        }
    }
    private fun setupTrailerButton(video: Video?) {
        if (video != null) {
            // Nếu có trailer, hiện nút lên
            binding.btnPlayTrailer.visibility = View.VISIBLE
            binding.btnPlayTrailer.setOnClickListener {
                // Tạo Intent để mở ứng dụng YouTube
                val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:${video.key}"))
                // Tạo Intent để mở trình duyệt nếu không có app YouTube
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=${video.key}"))

                try {
                    startActivity(appIntent) // Ưu tiên mở app
                } catch (ex: ActivityNotFoundException) {
                    startActivity(webIntent) // Nếu không có app, mở web
                }
            }
        } else {
            // Nếu không có trailer, ẩn nút đi
            binding.btnPlayTrailer.visibility = View.GONE
        }
    }


    private fun updateFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.ivFavorite.setImageResource(R.drawable.ic_favorite_filled)
            binding.ivFavorite.setColorFilter(Color.RED)
        } else {
            binding.ivFavorite.setImageResource(R.drawable.ic_favorite_border)
            binding.ivFavorite.clearColorFilter()
            binding.ivFavorite.setColorFilter(Color.WHITE)
        }
    }
}