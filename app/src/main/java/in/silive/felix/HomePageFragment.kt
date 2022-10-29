package `in`.silive.felix

import `in`.silive.felix.viewpager.ViewPagerAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager


class HomePageFragment : Fragment() {

    lateinit var viewPager : ViewPager
    var images : MutableList<Int> = mutableListOf(R.drawable.money_heist, R.drawable.daredevil, R.drawable.money_heist, R.drawable.daredevil, R.drawable.money_heist, R.drawable.daredevil)
    lateinit var viewPagerAdapter : ViewPagerAdapter



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



        viewPagerAdapter = ViewPagerAdapter(view.context, images)

        viewPager.adapter = viewPagerAdapter

        viewPager.clipToPadding = false;
        viewPager.setPadding(300,0,300,0);

        Log.d("Ashu", viewPagerAdapter.count.toString())


//        viewPager = view.findViewById(R.id.viewPager)
//        imageList = mutableListOf()
//
//        imageList.add(Image(R.drawable.daredevil))
//        imageList.add(Image(R.drawable.money_heist))
//        imageList.add(Image(R.drawable.daredevil))
//        imageList.add(Image(R.drawable.money_heist))
//        imageList.add(Image(R.drawable.daredevil))
//        imageList.add(Image(R.drawable.money_heist))
//
//        imageAdapter = ImageAdapter(imageList, viewPager)
//
//        viewPager.adapter = imageAdapter


        return view
    }

}