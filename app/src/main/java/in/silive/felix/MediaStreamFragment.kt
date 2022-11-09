package `in`.silive.felix

import `in`.silive.felix.module.CategoryResponse
import `in`.silive.felix.module.MediaStreamingResponse
import `in`.silive.felix.module.MovieId
import `in`.silive.felix.module.MoviesList
import `in`.silive.felix.recyclerview.ItemClickListener
import `in`.silive.felix.recyclerview.ParentRecyclerAdapter
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.Service
import android.graphics.Movie
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
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
    lateinit var wishListBtn : AppCompatButton
    var isVPlaying = false
    lateinit var movieRecyclerView: RecyclerView
    var isLiked : Boolean? = false
    var isAddedToWishlist : Boolean? = false



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
        wishListBtn = view.findViewById(R.id.wishListBtn)



        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.getMediaStreamingDetails(
            "Bearer " + (activity as HomePageActivity).token,
            (activity as HomePageActivity).movieId.toString())

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
                isAddedToWishlist = details?.addedToWishlist

                if(isAddedToWishlist == true){
                    wishListBtn.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(wishListBtn.context, R.drawable.ic_cross), null, null, null)
                }

                wishListBtn.setOnClickListener{
                    if(isAddedToWishlist == true){
                        removeFromWishlist()
                    }
                    else{
                        addToWishList()
                    }
                }



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





            }

            override fun onFailure(call: Call<MediaStreamingResponse>, t: Throwable) {
                Toast.makeText(view.context, "Failed to load movie data", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(view.context, "Failed to load similar movies", Toast.LENGTH_SHORT).show()
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

    override fun onItemClick(position: Int, movieId : Int) {
        (activity as HomePageActivity).movieId = movieId
        (activity as HomePageActivity).mediaStreamingFrag()
    }

    fun addToWishList(){
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.addToWishList("Bearer " + (activity as HomePageActivity).token, MovieId((activity as HomePageActivity).movieId))
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Toast.makeText(requireContext(), response.body().toString(), Toast.LENGTH_SHORT).show()
                wishListBtn.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(wishListBtn.context, R.drawable.ic_cross), null, null, null)
                isAddedToWishlist = true
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed to add to wish list", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun removeFromWishlist(){
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.removeFromWishList("Bearer " + (activity as HomePageActivity).token, (activity as HomePageActivity).movieId.toString())
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Toast.makeText(requireContext(), response.body().toString(), Toast.LENGTH_SHORT).show()
                wishListBtn.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(wishListBtn.context, R.drawable.ic_plus), null, null, null)
                isAddedToWishlist = false
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed to remove from wish list", Toast.LENGTH_SHORT).show()
            }

        })
    }


}
