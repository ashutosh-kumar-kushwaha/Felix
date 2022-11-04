package `in`.silive.felix.module

import com.google.gson.annotations.SerializedName

data class Email(
    @SerializedName("email") val email: String
)