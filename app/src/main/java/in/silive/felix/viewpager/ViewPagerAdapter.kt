package `in`.silive.felix.viewpager

import `in`.silive.felix.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewpager.widget.PagerAdapter
import java.util.*

class ViewPagerAdapter(val context: Context, val images: MutableList<Int>) : PagerAdapter() {

    var mLayoutInflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view : View = LayoutInflater.from(context).inflate(R.layout.item, container, false)

        val item1 : AppCompatImageView = view.findViewById(R.id.item1)

        item1.setImageResource(images[position])

        Objects.requireNonNull(container).addView(view)

//        container.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }


}
