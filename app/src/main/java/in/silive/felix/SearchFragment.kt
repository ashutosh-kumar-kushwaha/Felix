package `in`.silive.felix

import `in`.silive.felix.module.SearchResponseItem
import `in`.silive.felix.recyclerview.ItemClickListener
import `in`.silive.felix.recyclerview.RecyclerSearchAdapter
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment(), ItemClickListener {

    lateinit var recyclerView : RecyclerView
    lateinit var searchView : SearchView
    lateinit var closeBtn : ImageView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        searchView = (activity as HomePageActivity).searchView
        searchView.setOnCloseListener {
            (activity as HomePageActivity).onBackPressed()
            false
        }


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
//                Toast.makeText(this@HomePageActivity, p0, Toast.LENGTH_SHORT).show()
                if(p0 != null && p0 !=""){
                    searchItems(p0)
                    return true
                }
                if(p0 == ""){
                    val recyclerAdapter = RecyclerSearchAdapter(requireContext(), listOf(), this@SearchFragment)
                    recyclerView.adapter = recyclerAdapter
                }

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
//                Toast.makeText(this@HomePageActivity, p0, Toast.LENGTH_SHORT).show()
                if(p0 != null && p0 !=""){
//                    Toast.makeText(view.context, p0, Toast.LENGTH_SHORT).show()
                    searchItems(p0)
                    return true
                }

                if(p0 == ""){
                    val recyclerAdapter = RecyclerSearchAdapter(requireContext(), listOf(), this@SearchFragment)
                    recyclerView.adapter = recyclerAdapter
                }

                return false
            }

        })


        return view
    }

    fun searchItems(p0 : String){
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.search("Bearer "+ (activity as HomePageActivity).token, p0)
        call.enqueue(object : Callback<List<SearchResponseItem>> {
            override fun onResponse(
                call: Call<List<SearchResponseItem>>,
                response: Response<List<SearchResponseItem>>
            ) {

                if(context != null) {

                    val list: List<SearchResponseItem> = response.body() as List<SearchResponseItem>

                    recyclerView.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    val recyclerAdapter =
                        RecyclerSearchAdapter(requireContext(), list, this@SearchFragment)
                    recyclerView.adapter = recyclerAdapter
                }
            }

            override fun onFailure(call: Call<List<SearchResponseItem>>, t: Throwable) {
                if(context != null) {
                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                }
            }


        })

    }

    override fun onItemClick(position: Int, movieId: Int) {
        (activity as HomePageActivity).movieId = movieId
        (activity as HomePageActivity).mediaStreamingFrag()
    }

    override fun onDestroyView() {
        searchView.onActionViewCollapsed()
        super.onDestroyView()
    }

}
