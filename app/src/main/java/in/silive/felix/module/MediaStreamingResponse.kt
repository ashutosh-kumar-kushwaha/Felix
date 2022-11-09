package `in`.silive.felix.module

import com.google.gson.annotations.SerializedName

class MediaStreamingResponse (
    @SerializedName("id") val id : Int?,
    @SerializedName("movieName") val movieName : String?,
    @SerializedName("movieDescription") val movieDescription : String?,
    @SerializedName("movieCast") val movieCast : String?,
    @SerializedName("movieYear") val movieYear : Int?,
    @SerializedName("movieRestriction") val movieRestriction : String?,
    @SerializedName("movieLength") val movieLength : String?,
    @SerializedName("genres") val genres : List<Genre>,
    @SerializedName("coverImageServingPath") val coverImageServingPath : String?,
    @SerializedName("streamMoviePath") val streamMoviePath : String?,
    @SerializedName("addedToWishlist") val addedToWishlist : Boolean?
        )