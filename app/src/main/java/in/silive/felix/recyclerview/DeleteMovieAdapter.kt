package `in`.silive.felix.recyclerview

import `in`.silive.felix.R
import `in`.silive.felix.module.SearchResponseItem
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class DeleteMovieAdapter (val context : Context, val movieList : List<SearchResponseItem>, val itemClickListener: ItemClickListener)  : RecyclerView.Adapter<DeleteMovieAdapter.ViewHolder>(){

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val movieImgVw : AppCompatImageView = itemView.findViewById(R.id.movieImgVw)
        val movieTitleTxtVw : AppCompatTextView = itemView.findViewById(R.id.movieTitleTxtVw)
        val deleteMovieBtn : AppCompatImageButton = itemView.findViewById(R.id.deleteMovieBtn)
        init {
            deleteMovieBtn.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if(position != RecyclerView.NO_POSITION){
                itemClickListener.onItemClick(position, movieList[position].movieId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeleteMovieAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.delete_movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeleteMovieAdapter.ViewHolder, position: Int) {
        holder.movieImgVw.load(movieList[position].movieCoverServingURL)
        holder.movieTitleTxtVw.text = movieList[position].movieName
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

}