package `in`.silive.felix.server

import `in`.silive.felix.module.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitAPI {
    @POST("api/auth/login")
    fun sendUserData(@Body userSend: User) : Call<User>
}