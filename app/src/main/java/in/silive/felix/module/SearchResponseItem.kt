package `in`.silive.felix.module

import com.google.gson.annotations.SerializedName

data class SearchResponseItem(
    @SerializedName("movieId") val movieId: Int,
    @SerializedName("movieName") val movieName: String,
    @SerializedName("movieCoverServingURL") val movieCoverServingURL: String
)