package `in`.silive.felix

import `in`.silive.felix.module.MediaStreamingResponse
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MediaStreamFragment : Fragment() {

    lateinit var movieTitle : AppCompatTextView
    lateinit var movieDetails : AppCompatTextView
    lateinit var castTxtVw : AppCompatTextView
    lateinit var synopsisTxtVw : AppCompatTextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_media_stream, container, false)

        movieTitle = view.findViewById(R.id.movieTitle)
        movieDetails = view.findViewById(R.id.movieDetails)
        castTxtVw = view.findViewById(R.id.castTxtVw)
        synopsisTxtVw = view.findViewById(R.id.synopsisTxtVw)

        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.getMediaStreamingDetails(
            "Bearer " + (activity as HomePageActivity).token,
            "4")

        call.enqueue(object : Callback<MediaStreamingResponse>{
            override fun onResponse(
                call: Call<MediaStreamingResponse>,
                response: Response<MediaStreamingResponse>
            ) {
                val details = response.body()
                movieTitle.text = details?.movieName
                movieDetails.text = "${details?.movieYear} | ${details?.movieRestriction} | ${details?.movieLength}"
                castTxtVw.text = details?.movieCast
                synopsisTxtVw.text = details?.movieDescription


            }

            override fun onFailure(call: Call<MediaStreamingResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })


        return view
    }
}