package `in`.silive.felix.recyclerview

import `in`.silive.felix.R
import `in`.silive.felix.module.Movie
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class RecyclerMoviesByCategoryAdapter(val context: Context, val movies : List<Movie>, val itemClickListener: ItemClickListener) : RecyclerView.Adapter<RecyclerMoviesByCategoryAdapter.ViewHolder>() {

    inner class ViewHolder(val itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val movieImgVw : ImageView = itemView.findViewById(R.id.movieImgVw)
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
        val view = LayoutInflater.from(context).inflate(R.layout.movie_by_category_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.movieImgVw.load(movies[position].coverImageServingPath)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

}
