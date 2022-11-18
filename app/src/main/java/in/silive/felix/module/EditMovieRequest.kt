package `in`.silive.felix.module

data class EditMovieRequest(
    val movieId: String,
    val newCategory: List<NewCategory>,
    val newGenre: List<NewGenre>,
    val newMovieCast: String,
    val newMovieDescription: String,
    val newMovieName: String,
    val newMovieYear: String
)