package `in`.silive.felix

import `in`.silive.felix.module.SearchResponseItem
import `in`.silive.felix.recyclerview.ItemClickListener
import `in`.silive.felix.recyclerview.DeleteMovieAdapter
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class DeleteMovieFragment : Fragment(), ItemClickListener {

    lateinit var moviesRecyclerView: RecyclerView
    lateinit var movieSearchView: SearchView
    lateinit var token : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_delete_movie, container, false)

        token = (activity as HomePageActivity).token

        moviesRecyclerView = view.findViewById(R.id.moviesRecyclerView)
        movieSearchView = view.findViewById(R.id.movieSearchView)


        movieSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 != null && p0 != "") {
                    searchItems(p0)
                    return true
                }
                if (p0 == "") {
                    val recyclerAdapter =
                        DeleteMovieAdapter(requireContext(), listOf(), this@DeleteMovieFragment)
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
                        DeleteMovieAdapter(requireContext(), listOf(), this@DeleteMovieFragment)
                    moviesRecyclerView.adapter = recyclerAdapter
                }

                return false
            }

        })

        return view
    }

    fun deleteMovie(movieId: Int) {
        if(context != null){
            val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
            val call = retrofitAPI.deleteMovie("Bearer $token", movieId)
            call.enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(requireContext(), "Failed to delete", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    override fun onItemClick(position: Int, movieId: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm Delete?").setMessage("Do you want to delete the movie?").setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ -> deleteMovie(movieId) }).setNegativeButton("No", DialogInterface.OnClickListener { _, _ ->  })
        val alertDialog = builder.create()
        alertDialog.show()
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

                    val list: List<SearchResponseItem> =
                        response.body() as List<SearchResponseItem>

                    moviesRecyclerView.layoutManager =
                        LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                    val recyclerAdapter =
                        DeleteMovieAdapter(requireContext(), list, this@DeleteMovieFragment)
                    moviesRecyclerView.adapter = recyclerAdapter
                }

                override fun onFailure(call: Call<List<SearchResponseItem>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}