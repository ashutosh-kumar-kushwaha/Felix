package `in`.silive.felix.recyclerview


import `in`.silive.felix.R
import `in`.silive.felix.module.CategoryResponse
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load

class RecyclerMoviesAdapter(val context: Context, val movies : List<CategoryResponse>) : RecyclerView.Adapter<RecyclerMoviesAdapter.ViewHolder>(){

    class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView){
        val movieImgVw : ImageView = itemView.findViewById(R.id.movieImgVw)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.movie, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.movieImgVw.load(movies[position].coverImageServingPath){
                Log.d("Ashu", "Loaded")
            }
    }

    override fun getItemCount(): Int {
        return movies.size
    }



}