package `in`.silive.felix.viewpager

import `in`.silive.felix.R
import `in`.silive.felix.module.Movie
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load

class ViewPagerAdapter(val context: Context, val movies: List<Movie?>) : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    class ViewHolder(val itemView : View) : RecyclerView.ViewHolder(itemView) {
        var images : AppCompatImageView = itemView.findViewById(R.id.item)
    }


//    val private runnable:Runnable = Runnable(){
//        override fun run(){
//            images.addAll(images)
//            notifyDataSetChanged()
//        }
//    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.item, parent, false)

        return ViewHolder(view as LinearLayoutCompat)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.images.load(movies[position]?.coverImageServingPath)
    }

    override fun getItemCount(): Int {
        return movies.size
    }



}
