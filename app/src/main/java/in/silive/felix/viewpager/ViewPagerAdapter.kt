package `in`.silive.felix.viewpager

import `in`.silive.felix.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter(val context: Context, val images: MutableList<Int>) : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    class ViewHolder(val itemView : View) : RecyclerView.ViewHolder(itemView) {
        var images : AppCompatImageView = itemView.findViewById(R.id.item)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.item, parent, false)

        return ViewHolder(view as LinearLayoutCompat)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.images.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }


}
