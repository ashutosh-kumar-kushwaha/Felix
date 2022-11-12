package `in`.silive.felix.recyclerview

import `in`.silive.felix.R
import `in`.silive.felix.module.CategoryResponse
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class RecyclerHistoryAdapter(val context: Context, val movies : List<CategoryResponse>, val historyClickListener: HistoryClickListener) : RecyclerView.Adapter<RecyclerHistoryAdapter.ViewHolder>() {



    inner class ViewHolder(val itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val movieImgVw : ImageView = itemView.findViewById(R.id.movieImgVw)
        val crossImgVw : ImageView = itemView.findViewById(R.id.crossImgVw)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                historyClickListener.onItemClick(position, movies[position].movieId)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.movieImgVw.load(movies[position].coverImageServingPath)
        holder.crossImgVw.setOnClickListener {
            historyClickListener.onDeleteClick(
                position,
                movies[position].movieId
            )
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}

