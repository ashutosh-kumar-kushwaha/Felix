package `in`.silive.felix.viewpager

import `in`.silive.felix.R
import `in`.silive.felix.module.Movie
import `in`.silive.felix.recyclerview.ItemClickListener
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load

class ViewPagerAdapter(val context: Context, val movies: List<Movie>, val itemClickListener: ItemClickListener) : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(val itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var images : AppCompatImageView = itemView.findViewById(R.id.item)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                itemClickListener.onItemClick(position, movies[position].movieId)
            }
        }
    }


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
