package `in`.silive.felix

import `in`.silive.felix.module.SearchResponseItem
import `in`.silive.felix.recyclerview.RecyclerSearchAdapter
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    lateinit var recyclerView : RecyclerView
    lateinit var searchView : SearchView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        searchView = (activity as HomePageActivity).searchView


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
//                Toast.makeText(this@HomePageActivity, p0, Toast.LENGTH_SHORT).show()
                if(p0 != null && p0 !=""){
                    searchItems(p0)
                    return true
                }
                if(p0 == ""){
                    val recyclerAdapter = RecyclerSearchAdapter(requireContext(), listOf())
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
                    val recyclerAdapter = RecyclerSearchAdapter(requireContext(), listOf())
                    recyclerView.adapter = recyclerAdapter
                }

                return false
            }

        })


        return view
    }

    fun searchItems(p0 : String){
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
//        Log.d("Ashu",  (activity as HomePageActivity).searchText)
        val call = retrofitAPI.search("Bearer "+ "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpbWFra3VzaHdhaGEyMDAyQGdtYWlsLmNvbSIsImlhdCI6MTY2ODExOTMxNywiZXhwIjoxNjY4MjA1NzE3fQ.NDlXYBfQAHx2nNg4f0nf-q9O31ckuuFM_5AUgQhrVLy8U95lqZZRD6GILYhpMEp6Atsd6B_W7-OVBrvMdumjyw", p0)
        call.enqueue(object : Callback<List<SearchResponseItem>> {
            override fun onResponse(
                call: Call<List<SearchResponseItem>>,
                response: Response<List<SearchResponseItem>>
            ) {
                val list : List<SearchResponseItem> = response.body() as List<SearchResponseItem>

                recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                val recyclerAdapter = RecyclerSearchAdapter(requireContext(), list)
                recyclerView.adapter = recyclerAdapter
            }

            override fun onFailure(call: Call<List<SearchResponseItem>>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }


        })


    }

//    override fun onDestroyView() {
//        searchView.isIconified = false
//        super.onDestroyView()
//    }

}
