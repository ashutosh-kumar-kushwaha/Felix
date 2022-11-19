package `in`.silive.felix.recyclerview

import `in`.silive.felix.R
import `in`.silive.felix.module.Movie
import `in`.silive.felix.module.SearchResponseItem
import `in`.silive.felix.recyclerview.ChildClickListener
import `in`.silive.felix.recyclerview.RecyclerMoviesAdapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class RecyclerRecommendationAdapter(val context: Context, val movies: List<SearchResponseItem>, val clickListener: ChildClickListener) : RecyclerView.Adapter<RecyclerRecommendationAdapter.ViewHolder>() {

    inner class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val movieImgVw : ImageView = itemView.findViewById(R.id.movieImgVw)

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?){
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                clickListener.onItemClick(position, movies[position].movieId)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerRecommendationAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerRecommendationAdapter.ViewHolder, position: Int) {
        holder.movieImgVw.load(movies[position].movieCoverServingURL)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

}