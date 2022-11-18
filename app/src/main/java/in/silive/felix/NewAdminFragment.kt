package `in`.silive.felix

import `in`.silive.felix.module.Email
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.Service
import android.os.Bundle
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

class NewAdminFragment : Fragment() {

    lateinit var emailETxt : TextInputEditText
    lateinit var addBtn : AppCompatButton
    lateinit var token : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_new_admin, container, false)
        emailETxt = view.findViewById(R.id.emailETxt)
        addBtn = view.findViewById(R.id.addBtn)
        addBtn.setOnClickListener{
            addAdmin()
        }

        token = (activity as HomePageActivity).token

        return view
    }

    fun addAdmin(){
        if(context != null){
            val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
            val call = retrofitAPI.addAdmin("Bearer $token", Email(emailETxt.text.toString()))
            call.enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(response.code() == 200){
                        Toast.makeText(requireContext(), "User is now admin", Toast.LENGTH_SHORT).show()
                    }
                    else if(response.code() == 409){
                        Toast.makeText(requireContext(), "This user is already ADMIN", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(requireContext(), "Failed to make admin", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }
}