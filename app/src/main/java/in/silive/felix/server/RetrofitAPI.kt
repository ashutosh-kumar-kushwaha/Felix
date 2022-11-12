package `in`.silive.felix.server

import `in`.silive.felix.module.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitAPI {
    @POST("api/auth/login")
    fun logIn(@Body userSend: User) : Call<User>

    @POST("api/auth/signup")
    fun signUp(@Body userSend: User) : Call<String>

    @PUT("api/auth/resend-verification-link")
    fun resendVerificationLink(@Body email : Email) : Call<String>

    @PUT("api/auth/forgot-password")
    fun forgotPassword(@Body email : Email) : Call<String>

    @POST("api/auth/confirm-otp")
    fun verifyOtp(@Body verifyOtpRequest: VerifyOtpRequest) : Call<String>

    @PUT("api/auth/change-password")
    fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest) :  Call<String>

    @PUT("api/auth/resend-otp")
    fun resendOtp(@Body email:Email) : Call<String>

    @GET("api/home/movies-by-category")
    fun getMovieByCategory(@Header ("Authorization") token : String, @Query ("category") category : String) : Call<List<Movie>>

    @GET("api/home/wishlist")
    fun getWishList(@Header ("Authorization") token: String) : Call<List<Movie>>

    @GET("api/history/get")
    fun getHistory(@Header ("Authorization") token: String) : Call<List<Movie>>

    @GET("api/media-streaming")
    fun getMediaStreamingDetails(@Header ("Authorization") token : String, @Query ("movieId") movieId : String) : Call<MediaStreamingResponse>

    @POST("api/home/add-to-wishlist")
    fun addToWishList(@Header ("Authorization") token : String, @Body movieId: MovieId) : Call<String>

    @DELETE("api/home/remove-from-wishlist")
    fun removeFromWishList(@Header ("Authorization") token : String, @Query("movieId") movieId: String) : Call<String>

    @POST("api/home/add-to-liked")
    fun like(@Header ("Authorization") token: String, @Body movieId: MovieId) : Call<String>

    @DELETE("api/home/delete-from-liked")
    fun removeLike(@Header ("Authorization") token: String, @Query("movieId") movieId: String) : Call<String>

    @GET("api/home/search")
    fun search(@Header ("Authorization") token: String, @Query("searchText") searchText : String) : Call<List<SearchResponseItem>>

    @POST("api/history/add")
    fun addToHistory(@Header ("Authorization") token: String, @Body movieId: MovieId) : Call<String>

    @DELETE("api/history/delete")
    fun removeFromHistory(@Header ("Authorization") token : String, @Query("movieId") movieId : String) : Call<String>

    @GET("api/home/trending")
    fun trending(@Header("Authorization") token : String) : Call<List<Movie>>

}