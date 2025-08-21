package com.example.movieappv2.ui.detail

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.movieappv2.R
import com.example.movieappv2.data.model.MovieDetail
import com.example.movieappv2.databinding.ActivityDetailBinding
import com.example.movieappv2.ui.adapters.CastAdapter
import com.example.movieappv2.ui.adapters.ReviewAdapter
import com.example.movieappv2.utils.Constants


// thêm dàn cast , thêm review
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private var currentMovie: MovieDetail? = null

    // Khai báo các adapter
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
            // Ra lệnh cho ViewModel tải tất cả dữ liệu cần thiết
            detailViewModel.fetchMovieDetails(movieId)
            detailViewModel.checkFavoriteStatus(movieId)
            detailViewModel.fetchMovieCredits(movieId)
            detailViewModel.fetchMovieReviews(movieId)
        }
    }

    // Hàm để cài đặt tất cả các RecyclerView
    private fun setupRecyclerViews() {
        // Cài đặt cho Cast
        castAdapter = CastAdapter()
        binding.rvCast.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = castAdapter
        }

        // Cài đặt cho Reviews
        reviewAdapter = ReviewAdapter()
        binding.rvReviews.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = reviewAdapter
        }
    }

    // Hàm để lắng nghe tất cả LiveData
    private fun observeViewModel() {
        // Lắng nghe chi tiết phim
        detailViewModel.movieDetail.observe(this) { movieDetail ->
            currentMovie = movieDetail
            bindMovieDetail(movieDetail)
        }

        // Lắng nghe trạng thái yêu thích
        detailViewModel.isFavorite.observe(this) { isFav ->
            updateFavoriteIcon(isFav)
        }

        // Lắng nghe danh sách diễn viên
        detailViewModel.cast.observe(this) { castList ->
            castAdapter.submitList(castList)
        }

        // Lắng nghe danh sách reviews
        detailViewModel.reviews.observe(this) { reviewList ->
            reviewAdapter.submitList(reviewList)
        }
    }

    // Hàm riêng để gán dữ liệu chi tiết phim cho gọn
    private fun bindMovieDetail(movieDetail: MovieDetail) {
        binding.tvTitleDetail.text = movieDetail.title
        binding.tvOverview.text = movieDetail.overview
        binding.tvRating.text = String.format("%.1f", movieDetail.voteAverage)

        binding.tvReleaseDate.text = movieDetail.releaseDate // Hiển thị đầy đủ "YYYY-MM-DD"

        // Nối tên các thể loại lại với nhau
        binding.tvGenres.text = movieDetail.genres.joinToString(", ") { it.name }

        val backdropUrl = Constants.IMAGE_BASE_URL + movieDetail.backdropPath
        Glide.with(this).load(backdropUrl).into(binding.ivBackdrop)
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