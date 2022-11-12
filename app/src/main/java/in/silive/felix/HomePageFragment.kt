package `in`.silive.felix

import `in`.silive.felix.module.Movie
import `in`.silive.felix.module.Category
import `in`.silive.felix.recyclerview.ItemClickListener
import `in`.silive.felix.recyclerview.ParentRecyclerAdapter
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import `in`.silive.felix.viewpager.ViewPagerAdapter
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs


class HomePageFragment : Fragment(), ItemClickListener {

    lateinit var viewPager: ViewPager2


    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var movieRecyclerView: RecyclerView
    lateinit var circles: List<ImageView>


    lateinit var progressBar: AlertDialog
    var builder: AlertDialog.Builder? = null

    var loadedItems = 0

    var moviesList: MutableList<Category> = mutableListOf()

    var isTrendingLoaded = false
    var isHollywoodLoaded = false
    var isBollywoodLoaded = false
    var isAnimeLoaded = false
    var isHorrorLoaded = false

    lateinit var token: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        progressBar = getDialogueProgressBar(view).create()
        progressBar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressBar.setCanceledOnTouchOutside(false)


        token = (activity as HomePageActivity).token


        viewPager = view.findViewById(R.id.viewPager)


        circles = listOf(
            view.findViewById<ImageView>(R.id.circle1),
            view.findViewById<ImageView>(R.id.circle2),
            view.findViewById<ImageView>(R.id.circle3),
            view.findViewById<ImageView>(R.id.circle4),
            view.findViewById<ImageView>(R.id.circle5),
            view.findViewById<ImageView>(R.id.circle6)
        )

//        images.add(R.drawable.daredevil)
//        images.add(R.drawable.money_heist)
//        images.add(R.drawable.daredevil)
//        images.add(R.drawable.money_heist)
//        images.add(R.drawable.daredevil)
//        images.add(R.drawable.money_heist)

//        viewPagerAdapter = ViewPagerAdapter(view.context, listOf(images[images.size-2]) + listOf(images[images.size-1]) + images + listOf(images[0]) + listOf(images[1]))

        var currentPage = 0



        movieRecyclerView = view.findViewById(R.id.recyclerView)


        viewPager.offscreenPageLimit = 3
        viewPager.clipChildren = false



        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        viewPager.clipToPadding = false;
        viewPager.setPadding(
            resources.getDimensionPixelSize(R.dimen.dp_73),
            resources.getDimensionPixelSize(R.dimen.dp_13),
            resources.getDimensionPixelSize(R.dimen.dp_73),
            resources.getDimensionPixelSize(R.dimen.dp_13)
        );

        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer(ViewPager2.PageTransformer { page, position ->
            val r: Float = 1 - abs(position)
            page.scaleY = 1f + r * 0.12f
            page.scaleX = 1f + r * 0.12f
        })

        viewPager.setPageTransformer(transformer)







        Log.d("Ashu", (activity as HomePageActivity).token)


        getHomePage()

//        https://upload.wikimedia.org/wikipedia/en/1/1b/Daredevil_season_1_poster.jpg

        return view
    }

    fun getTrending() {

        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)

        val callTrending = retrofitAPI.trending("Bearer $token")
        callTrending.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(
                call: Call<List<Movie>>,
                response: Response<List<Movie>>
            ) {

                if (response.code() == 200) {
                    val trendingMovies = response.body() as List<Movie>
//                    viewPagerAdapter = ViewPagerAdapter(
//                        requireContext(),
//                        listOf(trendingMovies.last()) + trendingMovies + listOf(trendingMovies.first())
//                    )
//                    viewPager.adapter = viewPagerAdapter
//                    onInfinitePageChangeCallBack(8)
//                    viewPager.currentItem = 1

                    viewPagerAdapter = ViewPagerAdapter(
                        requireContext(),
                        trendingMovies + trendingMovies + trendingMovies, this@HomePageFragment
                    )
                    viewPager.adapter = viewPagerAdapter
                    onInfinitePageChangeCallBack(18)
                    viewPager.setCurrentItem(6, false)

                } else if (response.code() == 401) {
                    (activity as HomePageActivity).signOut()
                } else if (response.code() == 500) {
                    Toast.makeText(
                        requireContext(),
                        "Internal Server Error. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                isTrendingLoaded = true
                checkProgressBar()

            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Failed to load trending movies",
                    Toast.LENGTH_SHORT
                ).show()
                isTrendingLoaded = true
                checkProgressBar()
            }

        })
    }

    fun checkProgressBar() {
        if (isTrendingLoaded && loadedItems == 4) {
            progressBar.dismiss()
        }
    }


    fun getCategory(categoryName: String) {
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)

        val call = retrofitAPI.getMovieByCategory(
            "Bearer $token",
            categoryName
        )
        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(
                call: Call<List<Movie>>,
                response: Response<List<Movie>>
            ) {
                if (response.code() == 200) {
                    val movies = response.body() as List<Movie>

                    moviesList.add(Category(categoryName, movies))

                    movieRecyclerView.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

                    val parentRecyclerAdapter =
                        ParentRecyclerAdapter(requireContext(), moviesList, this@HomePageFragment)

                    movieRecyclerView.adapter = parentRecyclerAdapter

                } else if (response.code() == 401) {
                    (activity as HomePageActivity).signOut()
                } else if (response.code() == 500) {
                    Toast.makeText(
                        requireContext(),
                        "Internal Server Error. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT)
                        .show()
                    progressBar.dismiss()
                }
                loadedItems++
                checkProgressBar()
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                loadedItems++
                checkProgressBar()
            }

        })
    }


    fun getHomePage() {
        progressBar.show()
        getTrending()
        getCategory("Bollywood")
        getCategory("Hollywood")
        getCategory("Anime")
        getCategory("Horror")
    }


    fun onInfinitePageChangeCallBack(listSize: Int) {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
//                    when (viewPager.currentItem) {
//                        listSize - 1 -> viewPager.setCurrentItem(1, false)
//                        0 -> viewPager.setCurrentItem(listSize - 2, false)
//                    }
                    if (viewPager.currentItem < 6) {
                        viewPager.setCurrentItem(viewPager.currentItem + 6, false)
                    } else if (viewPager.currentItem > 11) {
                        viewPager.setCurrentItem(viewPager.currentItem - 6, false)
                    }
                }

                if ((viewPager.currentItem) % circles.size == 0) {
                    circles[5].setImageResource(R.drawable.ic_circle_grey)
                } else {
                    circles[(viewPager.currentItem - 1) % circles.size].setImageResource(R.drawable.ic_circle_grey)
                }



                circles[(viewPager.currentItem) % circles.size].setImageResource(R.drawable.ic_circle_white)



                if((viewPager.currentItem) % circles.size == 5){
                        circles[0].setImageResource(R.drawable.ic_circle_grey)
                    } else {
                    circles[(viewPager.currentItem + 1) % circles.size].setImageResource(R.drawable.ic_circle_grey)
                }



//                try{
//                    circles[(viewPager.currentItem - 1) % circles.size].setImageResource(R.drawable.ic_circle_white)
//                }
//                catch (e:Error){
//                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
//                }


//                try{
//
//                    if ((viewPager.currentItem - 1) % circles.size == 0) {
//                        circles[listSize - 3].setImageResource(R.drawable.ic_circle_grey)
//                    } else{
//                        circles[(viewPager.currentItem - 2) % circles.size].setImageResource(R.drawable.ic_circle_grey)
//                    }
//                }
//                catch (e:Error){
//                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
//                }
//
//                try{
//                    if((viewPager.currentItem-1) % circles.size == listSize-3){
//                        circles[0].setImageResource(R.drawable.ic_circle_grey)
//                    }
//                    else{
//                        circles[(viewPager.currentItem) % circles.size].setImageResource(R.drawable.ic_circle_grey)
//                    }
//                }
//                catch (e:Error){
//                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
//                }


            }
        })
    }

    fun getDialogueProgressBar(view: View): AlertDialog.Builder {
        if (builder == null) {
            builder = AlertDialog.Builder(view.context)
            val progressBar = ProgressBar(view.context)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            progressBar.layoutParams = lp
            builder!!.setView(progressBar)
        }
        return builder as AlertDialog.Builder
    }

    override fun onItemClick(position: Int, movieId: Int) {
        (activity as HomePageActivity).movieId = movieId
        (activity as HomePageActivity).mediaStreamingFrag()
    }


}