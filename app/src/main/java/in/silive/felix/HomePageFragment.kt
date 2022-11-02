package `in`.silive.felix

import `in`.silive.felix.module.MoviesList
import `in`.silive.felix.recyclerview.ParentRecyclerAdapter
import `in`.silive.felix.recyclerview.RecyclerMoviesAdapter
import `in`.silive.felix.viewpager.ViewPagerAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs


class HomePageFragment : Fragment() {

    lateinit var viewPager : ViewPager2
    var images : MutableList<Int> = mutableListOf(R.drawable.money_heist, R.drawable.daredevil, R.drawable.money_heist, R.drawable.daredevil , R.drawable.money_heist, R.drawable.daredevil)
    lateinit var viewPagerAdapter : ViewPagerAdapter
    lateinit var movieRecyclerView: RecyclerView
    var moviesList = listOf(MoviesList("Top Picks for you", images), MoviesList("Top Picks for you", images), MoviesList("Top Picks for you", images))




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        viewPager = view.findViewById(R.id.viewPager)

//        images.add(R.drawable.daredevil)
//        images.add(R.drawable.money_heist)
//        images.add(R.drawable.daredevil)
//        images.add(R.drawable.money_heist)
//        images.add(R.drawable.daredevil)
//        images.add(R.drawable.money_heist)

//        viewPagerAdapter = ViewPagerAdapter(view.context, listOf(images[images.size-2]) + listOf(images[images.size-1]) + images + listOf(images[0]) + listOf(images[1]))


        viewPagerAdapter = ViewPagerAdapter(view.context, listOf(images.last()) + images + listOf(images.first()))
        viewPager.adapter = viewPagerAdapter

        viewPager.offscreenPageLimit = 3
        viewPager.clipChildren = false


        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        viewPager.clipToPadding = false;
        viewPager.setPadding(resources.getDimensionPixelSize(R.dimen.dp_79),resources.getDimensionPixelSize(R.dimen.dp_13),resources.getDimensionPixelSize(R.dimen.dp_79),resources.getDimensionPixelSize(R.dimen.dp_13));

        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer(ViewPager2.PageTransformer { page, position -> val r : Float = 1 - abs(position)
            Log.d("Ashu", r.toString())
        page.scaleY = 1f + r*0.12f
            page.scaleX = 1f + r*0.12f
        })

        viewPager.setPageTransformer(transformer)
//        viewPager.setCurrentItem(2)

        onInfinitePageChangeCallBack(images.size + 2)




        movieRecyclerView = view.findViewById(R.id.recyclerView)
        movieRecyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)

        val parentRecyclerAdapter = ParentRecyclerAdapter(view.context, moviesList)

        movieRecyclerView.adapter = parentRecyclerAdapter



        return view
    }


    fun onInfinitePageChangeCallBack(listSize : Int){
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if(state == ViewPager2.SCROLL_STATE_IDLE){
                    when (viewPager.currentItem){
                        listSize - 1 -> viewPager.setCurrentItem(1, false)
                        0 -> viewPager.setCurrentItem(listSize-2, false)
                    }
                }
            }
        })
    }


}