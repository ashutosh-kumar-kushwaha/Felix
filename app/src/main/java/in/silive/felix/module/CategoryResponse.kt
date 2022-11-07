package `in`.silive.felix.module

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("movieId") val movieId : Int,
    @SerializedName("coverImageServingPath") val coverImageServingPath : String
)