package `in`.silive.felix

import `in`.silive.felix.module.SearchResponseItem
import `in`.silive.felix.recyclerview.ItemClickListener
import `in`.silive.felix.recyclerview.SearchForEditAdapter
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchMovieForEditFragment : Fragment() , ItemClickListener{

    lateinit var moviesRecyclerView: RecyclerView
    lateinit var movieSearchView: SearchView
    lateinit var token : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_movie_for_edit, container, false)

        token = (activity as HomePageActivity).token

        moviesRecyclerView = view.findViewById(R.id.moviesRecyclerView)
        movieSearchView = view.findViewById(R.id.movieSearchView)
        val searchETxt = movieSearchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchETxt.setTextColor(resources.getColor(R.color.white))

        val searchClose: ImageView =
            movieSearchView.findViewById(androidx.appcompat.R.id.search_close_btn) as ImageView
        searchClose.setColorFilter(Color.WHITE)


        movieSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 != null && p0 != "") {
                    searchItems(p0)
                    return true
                }
                if (p0 == "") {
                    val recyclerAdapter =
                        SearchForEditAdapter(requireContext(), listOf(), this@SearchMovieForEditFragment)
                    moviesRecyclerView.adapter = recyclerAdapter
                }

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 != null && p0 != "") {
                    searchItems(p0)
                    return true
                }

                if (p0 == "") {
                    val recyclerAdapter =
                        SearchForEditAdapter(requireContext(), listOf(), this@SearchMovieForEditFragment)
                    moviesRecyclerView.adapter = recyclerAdapter
                }

                return false
            }

        })


        return view
    }

    override fun onItemClick(position: Int, movieId: Int) {
        (activity as HomePageActivity).movieId = movieId
        (activity as HomePageActivity).editMovieFrag()
    }

    fun searchItems(p0: String) {
        if(context != null) {
            val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
            val call = retrofitAPI.search("Bearer " + (activity as HomePageActivity).token, p0)
            call.enqueue(object : Callback<List<SearchResponseItem>> {
                override fun onResponse(
                    call: Call<List<SearchResponseItem>>,
                    response: Response<List<SearchResponseItem>>
                ) {
                    if (context != null) {

                        val list: List<SearchResponseItem> =
                            response.body() as List<SearchResponseItem>

                        moviesRecyclerView.layoutManager =
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        val recyclerAdapter =
                            SearchForEditAdapter(
                                requireContext(),
                                list,
                                this@SearchMovieForEditFragment
                            )
                        moviesRecyclerView.adapter = recyclerAdapter
                    }

                }

                override fun onFailure(call: Call<List<SearchResponseItem>>, t: Throwable) {
                    if(context != null) {
                        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
}