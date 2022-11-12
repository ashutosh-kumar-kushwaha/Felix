package `in`.silive.felix

import `in`.silive.felix.module.*
import `in`.silive.felix.recyclerview.ItemClickListener
import `in`.silive.felix.recyclerview.ParentRecyclerAdapter
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.flexbox.FlexboxLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MediaStreamFragment : Fragment(), ItemClickListener {

    lateinit var movieTitle: AppCompatTextView
    lateinit var movieDetails: AppCompatTextView
    lateinit var castTxtVw: AppCompatTextView
    lateinit var synopsisTxtVw: AppCompatTextView
    lateinit var videoPlayer: StyledPlayerView
    lateinit var exoPlayer: ExoPlayer
    lateinit var wishListBtn: AppCompatButton
    lateinit var heartImgVw: AppCompatImageView
    var isVPlaying = false
    lateinit var movieRecyclerView: RecyclerView
    var isLiked: Boolean? = false
    var isAddedToWishlist: Boolean? = false
    var isChangingWishList = false
    var isChangingLike = false
    lateinit var genres : List<Genre>
    lateinit var flexLayout : FlexboxLayout
    var isAddedToHistory = false


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
        heartImgVw = view.findViewById(R.id.heartImgVw)
        flexLayout = view.findViewById(R.id.flexLayout)

        wishListBtn.setOnClickListener {
            if (!isChangingWishList) {
                if (isAddedToWishlist == true) {
                    removeFromWishlist()
                } else {
                    addToWishList()
                }
            }
        }

        heartImgVw.setOnClickListener {
            if (!isChangingLike) {
                if (isLiked == true) {
                    removeLike()
                } else {
                    like()
                }
            }
        }


        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.getMediaStreamingDetails(
            "Bearer " + (activity as HomePageActivity).token,
            (activity as HomePageActivity).movieId.toString()
        )

        call.enqueue(object : Callback<MediaStreamingResponse> {
            override fun onResponse(
                call: Call<MediaStreamingResponse>,
                response: Response<MediaStreamingResponse>
            ) {


                val details = response.body()
                movieTitle.text = details?.movieName
                movieDetails.text =
                    "${details?.movieYear} | ${details?.movieRestriction} | ${details?.movieLength}"
                castTxtVw.text = details?.movieCast
                synopsisTxtVw.text = details?.movieDescription
                isAddedToWishlist = details?.addedToWishlist
                isLiked = details?.liked
                genres = details!!.genres


                for(i in genres){
                    var txtVw = AppCompatTextView(view.context)
                    txtVw.text = i.genreName
                    var lp = FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT)
                    txtVw.setTextColor(Color.WHITE)
                    txtVw.setTypeface(ResourcesCompat.getFont(view.context ,R.font.montserrat))
                    txtVw.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        resources.getDimensionPixelSize(R.dimen.sp_12).toFloat()
                    )
                    txtVw.background = ContextCompat.getDrawable(view.context ,R.drawable.genre_background)
                    lp.setMargins(0, 0, resources.getDimensionPixelSize(R.dimen.dp_7), resources.getDimensionPixelSize(R.dimen.dp_7))
                    txtVw.setPadding(resources.getDimensionPixelSize(R.dimen.dp_5), 0, resources.getDimensionPixelSize(R.dimen.dp_5),0)
                    txtVw.layoutParams = lp
                    flexLayout.addView(txtVw)
                }

                if (isAddedToWishlist == true) {
                    wishListBtn.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(
                            wishListBtn.context,
                            R.drawable.ic_cross
                        ), null, null, null
                    )
                }
                if (isLiked == true) {
                    heartImgVw.setImageResource(R.drawable.heart_liked)
                }




                videoPlayer.player = exoPlayer

                val mediaItem = MediaItem.fromUri(details?.streamMoviePath.toString())

                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.play()



                exoPlayer.addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)
                        isVPlaying = !isVPlaying
                        if(!isAddedToHistory){
                            addToHistory()
                            isAddedToHistory = true
                        }
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

        call2.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(
                call: Call<List<Movie>>,
                response: Response<List<Movie>>
            ) {

                var movie = listOf(
                    Category(
                        "Movies",
                        response.body() as List<Movie>
                    ), Category(
                        "Movies",
                        response.body() as List<Movie>
                    ), Category(
                        "Movies",
                        response.body() as List<Movie>
                    )
                )
                movieRecyclerView = view.findViewById(R.id.recyclerView)
                movieRecyclerView.layoutManager =
                    LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)

                val parentRecyclerAdapter =
                    ParentRecyclerAdapter(view.context, movie, this@MediaStreamFragment)

                movieRecyclerView.adapter = parentRecyclerAdapter
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                Toast.makeText(view.context, "Failed to load similar movies", Toast.LENGTH_SHORT)
                    .show()
            }

        })


        return view
    }

    fun addToHistory() {
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.addToHistory("Bearer " + (activity as HomePageActivity).token, MovieId((activity as HomePageActivity).movieId))
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.code() == 200){
                    Toast.makeText(requireContext(), response.body().toString(), Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed to add to history", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onPause() {
        super.onPause()
        exoPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        if (isVPlaying) {
            exoPlayer.play()
        }
    }

    override fun onItemClick(position: Int, movieId: Int) {
        exoPlayer.pause()
        (activity as HomePageActivity).movieId = movieId
        (activity as HomePageActivity).mediaStreamingFrag()
    }

    fun addToWishList() {
        isChangingWishList = true
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.addToWishList(
            "Bearer " + (activity as HomePageActivity).token,
            MovieId((activity as HomePageActivity).movieId)
        )
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Toast.makeText(requireContext(), response.body().toString(), Toast.LENGTH_SHORT)
                    .show()
                wishListBtn.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        wishListBtn.context,
                        R.drawable.ic_cross
                    ), null, null, null
                )
                isAddedToWishlist = true
                isChangingWishList = false
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Failed to add to wish list",
                    Toast.LENGTH_SHORT
                ).show()
                isChangingWishList = false
            }

        })

    }

    fun removeFromWishlist() {
        isChangingWishList = true
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.removeFromWishList(
            "Bearer " + (activity as HomePageActivity).token,
            (activity as HomePageActivity).movieId.toString()
        )
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Toast.makeText(requireContext(), response.body().toString(), Toast.LENGTH_SHORT)
                    .show()
                wishListBtn.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        wishListBtn.context,
                        R.drawable.ic_plus
                    ), null, null, null
                )
                isAddedToWishlist = false
                isChangingWishList = false
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Failed to remove from wish list",
                    Toast.LENGTH_SHORT
                ).show()
                isChangingWishList = false
            }

        })

    }

    fun like() {
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.like(
            "Bearer " + (activity as HomePageActivity).token,
            MovieId((activity as HomePageActivity).movieId)
        )
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Toast.makeText(requireContext(), response.body().toString(), Toast.LENGTH_SHORT)
                    .show()
                isLiked = true
                heartImgVw.setImageResource(R.drawable.heart_liked)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed to like", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun removeLike() {
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.removeLike(
            "Bearer " + (activity as HomePageActivity).token,
            (activity as HomePageActivity).movieId.toString()
        )
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Toast.makeText(requireContext(), response.body().toString(), Toast.LENGTH_SHORT)
                    .show()
                isLiked = false
                heartImgVw.setImageResource(R.drawable.heart_unliked)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed to like", Toast.LENGTH_SHORT).show()
            }

        })
    }

}
