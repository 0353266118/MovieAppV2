package com.example.movieappv2.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieappv2.databinding.ActivitySearchBinding
import com.example.movieappv2.ui.adapters.BaseMovieAdapter // 1. Import adapter mới
import com.example.movieappv2.ui.detail.DetailActivity

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    // 2. Sử dụng BaseMovieAdapter
    private lateinit var searchAdapter: BaseMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener { finish() }

        setupRecyclerView()
        observeViewModel()
        setupSearchBox()
    }

    private fun setupRecyclerView() {
        // 3. Khởi tạo BaseMovieAdapter
        searchAdapter = BaseMovieAdapter { movie ->
            // Khi người dùng click, mở DetailActivity
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("MOVIE_ID", movie.id)
            startActivity(intent)
        }

        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
        }
    }

    private fun observeViewModel() {
        searchViewModel.searchResults.observe(this) { movies ->
            searchAdapter.submitList(movies)
        }
    }

    private fun setupSearchBox() {
        binding.etSearch.addTextChangedListener { editable ->
            val query = editable.toString()
            searchViewModel.searchMovies(query)
        }
    }
}