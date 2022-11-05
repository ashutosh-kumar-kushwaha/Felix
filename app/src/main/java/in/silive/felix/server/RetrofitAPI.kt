package `in`.silive.felix.server

import `in`.silive.felix.module.Email
import `in`.silive.felix.module.ResetPasswordRequest
import `in`.silive.felix.module.User
import `in`.silive.felix.module.VerifyOtpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface RetrofitAPI {
    @POST("api/auth/login")
    fun sendUserData(@Body userSend: User) : Call<User>

    @POST("api/auth/signup")
    fun signIn(@Body userSend: User) : Call<String>

    @PUT("api/auth/resend-verification-link")
    fun resendVerificationLink(@Body email : Email) : Call<String>

    @PUT("api/auth/forgot-password")
    fun forgotPassword(@Body email : Email) : Call<String>

    @POST("api/auth/confirm-otp")
    fun verifyOtp(@Body verifyOtpRequest: VerifyOtpRequest) : Call<String>

    @PUT("api/auth/change-password")
    fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest) :  Call<String>
}