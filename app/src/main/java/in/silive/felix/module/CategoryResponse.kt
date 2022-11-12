package `in`.silive.felix.module

import com.google.gson.annotations.SerializedName

data class CategoryResponse (
    @SerializedName("allCategoryId") val allCategoryId : Int,
    @SerializedName("allCategoryName") val allCategoryName : String
)