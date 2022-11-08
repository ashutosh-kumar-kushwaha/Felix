package `in`.silive.felix.module

import com.google.gson.annotations.SerializedName

class Genre (
    @SerializedName("genreId") val genreId : Int?,
    @SerializedName("genreName") val genreName : String?
        )