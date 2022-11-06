package `in`.silive.felix

import `in`.silive.felix.recyclerview.RecyclerMoviesAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class WishlistFragment : Fragment() {


    lateinit var movieRecyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_wishlist, container, false)

//        movieRecyclerView = view.findViewById(R.id.recyclerView)
//        movieRecyclerView.layoutManager = GridLayoutManager(view.context, 3)
//
//        movieRecyclerView.adapter = RecyclerMoviesAdapter(view.context, listOf(R.drawable.daredevil, R.drawable.daredevil, R.drawable.daredevil, R.drawable.daredevil, R.drawable.daredevil, R.drawable.daredevil, R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil))


        return view
    }


}