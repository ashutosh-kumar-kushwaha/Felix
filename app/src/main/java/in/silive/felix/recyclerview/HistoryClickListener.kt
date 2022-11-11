package `in`.silive.felix.recyclerview

interface HistoryClickListener {
    fun onItemClick(position : Int, movieId : Int)
    fun onDeleteClick(position: Int, movieId: Int)
}