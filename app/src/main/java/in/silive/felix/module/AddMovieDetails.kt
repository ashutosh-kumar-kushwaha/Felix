package `in`.silive.felix.module

data class AddMovieDetails(
    val categories: List<CategoryX>,
    val genres: List<GenreX>,
    val movieCast: String,
    val movieDescription: String,
    val movieLength: String,
    val movieName: String,
    val movieRestriction: String,
    val movieYear: String
)