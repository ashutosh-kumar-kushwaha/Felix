package `in`.silive.felix.recyclerview

import android.view.View

interface ItemClickListener {
    fun onItemClick(position : Int, movieId : Int)
}