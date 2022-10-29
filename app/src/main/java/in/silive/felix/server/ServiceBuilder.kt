package `in`.silive.felix.server

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ServiceBuilder {
    private const val URL = "https://felixapis.herokuapp.com/"

    private val okHttp = OkHttpClient.Builder()

    private val builder = Retrofit.Builder().baseUrl(URL).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).client(okHttp.build())

    private val retrofit = builder.build()

    fun <T> buildService(serviceType : Class<T>) : T{
        return retrofit.create(serviceType)
    }
}