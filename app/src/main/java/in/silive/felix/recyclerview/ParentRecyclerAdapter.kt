package `in`.silive.felix.recyclerview

import `in`.silive.felix.R
import `in`.silive.felix.module.MoviesList
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ParentRecyclerAdapter(val context: Context, val moviesList : List<MoviesList>, val itemClickListener: ItemClickListener) : RecyclerView.Adapter<ParentRecyclerAdapter.ViewHolder>(), ChildClickListener {


    class ViewHolder(val itemView : View) : RecyclerView.ViewHolder(itemView) {
        val titleTxtVw : TextView = itemView.findViewById(R.id.titleTxtVw)
        val movieRecyclerView : RecyclerView = itemView.findViewById(R.id.movieRVw)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.parent_recycler_view, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTxtVw.text = moviesList[position].title
        holder.movieRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.movieRecyclerView.adapter = RecyclerMoviesAdapter(context, moviesList[position].movies, this)
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }


    override fun onItemClick(position: Int, movieId : Int) {
        itemClickListener.onItemClick(position, movieId)
    }


}