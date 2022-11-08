package `in`.silive.felix

import `in`.silive.felix.module.CategoryResponse
import `in`.silive.felix.module.MoviesList
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


class HomePageFragment : Fragment(), ItemClickListener{

    lateinit var viewPager: ViewPager2
    var images1: MutableList<Int> = mutableListOf(
        R.drawable.money_heist,
        R.drawable.daredevil,
        R.drawable.money_heist,
        R.drawable.daredevil,
        R.drawable.money_heist,
        R.drawable.daredevil
    )


    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var movieRecyclerView: RecyclerView
    lateinit var circles: List<ImageView>


    lateinit var progressBar: AlertDialog
    var builder: AlertDialog.Builder? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        progressBar = getDialogueProgressBar(view).create()
        progressBar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressBar.setCanceledOnTouchOutside(false)

        progressBar.show()

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

        viewPagerAdapter = ViewPagerAdapter(
            view.context,
            listOf(images1.last()) + images1 + listOf(images1.first())
        )
        viewPager.adapter = viewPagerAdapter

        viewPager.offscreenPageLimit = 3
        viewPager.clipChildren = false



        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        viewPager.clipToPadding = false;
        viewPager.setPadding(
            resources.getDimensionPixelSize(R.dimen.dp_79),
            resources.getDimensionPixelSize(R.dimen.dp_13),
            resources.getDimensionPixelSize(R.dimen.dp_79),
            resources.getDimensionPixelSize(R.dimen.dp_13)
        );

        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer(ViewPager2.PageTransformer { page, position ->
            val r: Float = 1 - abs(position)
            Log.d("Ashu", r.toString())
            page.scaleY = 1f + r * 0.12f
            page.scaleX = 1f + r * 0.12f
        })

        viewPager.setPageTransformer(transformer)
        viewPager.setCurrentItem(1)

        onInfinitePageChangeCallBack(images1.size + 2)


        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.getMovieByCategory(
            "Bearer " + (activity as HomePageActivity).token,
            "Movie"
        )
        call.enqueue(object : Callback<List<CategoryResponse>> {
            override fun onResponse(
                call: Call<List<CategoryResponse>>,
                response: Response<List<CategoryResponse>>
            ) {
                if (response.code() == 200) {
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

                    val parentRecyclerAdapter = ParentRecyclerAdapter(view.context, moviesList, this@HomePageFragment)

                    movieRecyclerView.adapter = parentRecyclerAdapter
                    progressBar.dismiss()

                } else {
                    Toast.makeText(view.context, response.code().toString(), Toast.LENGTH_SHORT)
                        .show()
                    progressBar.dismiss()
                }
            }

            override fun onFailure(call: Call<List<CategoryResponse>>, t: Throwable) {
                Toast.makeText(view.context, "Failed", Toast.LENGTH_SHORT).show()
                progressBar.dismiss()
            }

        })

        Log.d("Ashu", (activity as HomePageActivity).token)


//        https://upload.wikimedia.org/wikipedia/en/1/1b/Daredevil_season_1_poster.jpg

        return view
    }


    fun onInfinitePageChangeCallBack(listSize: Int) {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    when (viewPager.currentItem) {
                        listSize - 1 -> viewPager.setCurrentItem(1, false)
                        0 -> viewPager.setCurrentItem(listSize - 2, false)
                    }
                }

                circles[(viewPager.currentItem - 1) % circles.size].setImageResource(R.drawable.ic_circle_white)
                if ((viewPager.currentItem - 1) % circles.size == 0) {
                    circles[listSize - 3].setImageResource(R.drawable.ic_circle_grey)
                } else
                    circles[(viewPager.currentItem - 2) % circles.size].setImageResource(R.drawable.ic_circle_grey)
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

    override fun onItemClick(position: Int) {
        (activity as HomePageActivity).mediaStreamingFrag()
    }


}