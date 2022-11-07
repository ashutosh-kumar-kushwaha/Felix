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

    @GET("api/home/movies-by-category?category=Movie")
    fun getMovieByCategory(@Header ("Authorization") token : String, @Query ("key") key : String, @Query ("value") value : String) : Call<List<CategoryResponse>>

}