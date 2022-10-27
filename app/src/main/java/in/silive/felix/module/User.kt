package `in`.silive.felix.module

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id : Int?,
    @SerializedName("email") val email : String?,
    @SerializedName("firstName") val firstName : String?,
    @SerializedName("lastName") val lastName : String?,
    @SerializedName("password") val password : String?,
    @SerializedName("role") val role : String?
)