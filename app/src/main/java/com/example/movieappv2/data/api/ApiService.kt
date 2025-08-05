package com.example.movieappv2.data.api

import com.example.movieappv2.data.model.MovieDetail
import com.example.movieappv2.data.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // HÀM MỚI: Lấy danh sách phim thịnh hành trong ngày
    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("api_key") apiKey: String
    ): Response<MovieResponse>

    // HÀM MỚI: Tìm kiếm phim theo từ khóa
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Response<MovieResponse>
    // Lấy danh sách phim phổ biến
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): Response<MovieResponse>

    // THÊM HÀM MỚI
    @GET("movie/{movie_id}") // Dùng {movie_id} để truyền ID vào đường dẫn
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int, // Annotation @Path để thay thế {movie_id}
        @Query("api_key") apiKey: String
    ): Response<MovieDetail> // Trả về đối tượng MovieDetail
}
