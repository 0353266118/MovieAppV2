package com.example.movieappv2.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey

import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorite_movies") // Đánh dấu đây là một bảng trong database, đặt tên là "favorite_movies"
data class Movie(
    @PrimaryKey // Đánh dấu 'id' là khóa chính, mỗi phim sẽ có id duy nhất
    @SerializedName("id") val id: Int,

    @SerializedName("title") val title: String,

    // Overview có thể null từ API, nên ta cho phép nó là nullable
    @SerializedName("overview") val overview: String?,

    @SerializedName("poster_path") val posterPath: String?,

    @SerializedName("vote_average") val voteAverage: Double
)