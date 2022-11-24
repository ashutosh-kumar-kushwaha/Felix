package `in`.silive.felix.module

data class MediaStreamingResponse2(
    val addedToWishlist: Boolean,
    val categories: List<CategoryXX>,
    val coverImageServingPath: String,
    val genres: List<GenreXX>,
    val id: Int,
    val liked: Boolean,
    val movieCast: String,
    val movieDescription: String,
    val movieLength: String,
    val movieName: String,
    val movieRestriction: String,
    val movieYear: Int,
    val rating: String,
    val reviewed: Boolean,
    val streamMoviePath: String,
    val totalReviews: Int
)