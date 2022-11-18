package `in`.silive.felix

import `in`.silive.felix.module.NewCategoryRequest
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class NewCategoryFragment : Fragment() {

    lateinit var categoryETxt : TextInputEditText
    lateinit var doneBtn : AppCompatButton
    lateinit var token : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_category, container, false)

        token = (activity as HomePageActivity).token
        categoryETxt = view.findViewById(R.id.categoryETxt)
        doneBtn.setOnClickListener{
            createNewCategory()
        }


        return view
    }

    private fun createNewCategory() {
        if(context != null) {
            val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
            val call = retrofitAPI.addNewCategory("Bearer $token", NewCategoryRequest(categoryETxt.text.toString()))
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(response.code() == 200){
                        Toast.makeText(requireContext(), "Category added successfully", Toast.LENGTH_SHORT).show()
                    }
                    else if(response.code() == 403){
                        Toast.makeText(requireContext(), "You must be an ADMIN to perform this action", Toast.LENGTH_SHORT).show()
                    }
                    else if(response.code() == 409){
                        Toast.makeText(requireContext(), "This category already exists", Toast.LENGTH_SHORT).show()
                    }
                    else if (response.code() == 401) {
                        (activity as HomePageActivity).signOut()
                    } else if (response.code() == 500) {
                        Toast.makeText(
                            requireContext(),
                            "Internal Server Error. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(requireContext(), "Failed to add category", Toast.LENGTH_SHORT)
                        .show()
                }

            })
        }
    }
}