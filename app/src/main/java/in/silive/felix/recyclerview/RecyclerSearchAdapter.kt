package `in`.silive.felix.recyclerview

import `in`.silive.felix.R
import `in`.silive.felix.module.SearchResponseItem
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class RecyclerSearchAdapter(val context : Context, val movieList : List<SearchResponseItem>) : RecyclerView.Adapter<RecyclerSearchAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val movieImgVw : AppCompatImageView = itemView.findViewById(R.id.movieImgVw)
        val movieTitleTxtVw : AppCompatTextView = itemView.findViewById(R.id.movieTitleTxtVw)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.movieImgVw.load(movieList[position].movieCoverServingURL)
        holder.movieTitleTxtVw.text = movieList[position].movieName
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}