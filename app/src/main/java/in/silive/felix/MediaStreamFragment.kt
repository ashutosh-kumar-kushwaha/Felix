package `in`.silive.felix

import `in`.silive.felix.module.CategoryResponse
import `in`.silive.felix.module.MediaStreamingResponse
import `in`.silive.felix.module.MoviesList
import `in`.silive.felix.recyclerview.ItemClickListener
import `in`.silive.felix.recyclerview.ParentRecyclerAdapter
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MediaStreamFragment : Fragment(), ItemClickListener {

    lateinit var movieTitle : AppCompatTextView
    lateinit var movieDetails : AppCompatTextView
    lateinit var castTxtVw : AppCompatTextView
    lateinit var synopsisTxtVw : AppCompatTextView
    lateinit var videoPlayer : StyledPlayerView
    lateinit var exoPlayer : ExoPlayer
    var isVPlaying = false
    lateinit var movieRecyclerView: RecyclerView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_media_stream, container, false)


        exoPlayer = ExoPlayer.Builder(view.context).build()
        movieTitle = view.findViewById(R.id.movieTitle)
        movieDetails = view.findViewById(R.id.movieDetails)
        castTxtVw = view.findViewById(R.id.castTxtVw)
        synopsisTxtVw = view.findViewById(R.id.synopsisTxtVw)
        videoPlayer = view.findViewById(R.id.videoPlayer)


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


                videoPlayer.player = exoPlayer

                val mediaItem = MediaItem.fromUri(details?.streamMoviePath.toString())

                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.play()

                exoPlayer.addListener(object  : Player.Listener{
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)
                        isVPlaying = !isVPlaying
                        Log.d("Ashu", isVPlaying.toString())
                    }
                })


                val call2 = retrofitAPI.getMovieByCategory(
                    "Bearer " + (activity as HomePageActivity).token,
                    "Movie"
                )

                call2.enqueue(object : Callback<List<CategoryResponse>>{
                    override fun onResponse(
                        call: Call<List<CategoryResponse>>,
                        response: Response<List<CategoryResponse>>
                    ) {

                        var moviesList = listOf(
                            MoviesList(
                                "Movies",
                                response.body() as List<CategoryResponse>
                            ), MoviesList(
                                "Movies",
                                response.body() as List<CategoryResponse>
                            ), MoviesList(
                                "Movies",
                                response.body() as List<CategoryResponse>
                            )
                        )
                        movieRecyclerView = view.findViewById(R.id.recyclerView)
                        movieRecyclerView.layoutManager =
                            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)

                        val parentRecyclerAdapter = ParentRecyclerAdapter(view.context, moviesList, this@MediaStreamFragment)

                        movieRecyclerView.adapter = parentRecyclerAdapter
                    }

                    override fun onFailure(call: Call<List<CategoryResponse>>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })


            }

            override fun onFailure(call: Call<MediaStreamingResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })








        return view
    }

    override fun onStop() {
        super.onStop()
        exoPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        if(isVPlaying){
            exoPlayer.play()
        }

    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }


}
