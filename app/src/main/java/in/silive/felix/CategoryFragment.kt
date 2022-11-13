package `in`.silive.felix

import `in`.silive.felix.module.CategoryResponse
import `in`.silive.felix.recyclerview.CategoryClickListener
import `in`.silive.felix.recyclerview.ItemClickListener
import `in`.silive.felix.recyclerview.RecyclerCategoryAdapter
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.Service
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryFragment : Fragment(), CategoryClickListener {

    lateinit var recyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        getCategories()
        return view
    }

    override fun onItemClick(position: Int, categoryName: String) {
        (activity as HomePageActivity).categoryName = categoryName
        (activity as HomePageActivity).moviesByCategoryFrag()
    }

    fun getCategories(){
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.getAllCategories("Bearer " + (activity as HomePageActivity).token)
        call.enqueue(object : Callback<List<CategoryResponse>> {
            override fun onResponse(
                call: Call<List<CategoryResponse>>,
                response: Response<List<CategoryResponse>>
            ) {
                if(response.code() == 200){
                    val categories = response.body() as List<CategoryResponse>
                    recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    recyclerView.adapter = RecyclerCategoryAdapter(requireContext(), categories, this@CategoryFragment)
                }
                else if(response.code()==401){
                    (activity as HomePageActivity).signOut()
                }
                else if(response.code()==500){
                    Toast.makeText(requireContext(), "Internal Server Error\nPlease try again", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CategoryResponse>>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed to load categories", Toast.LENGTH_SHORT).show()
            }

        })
    }
}