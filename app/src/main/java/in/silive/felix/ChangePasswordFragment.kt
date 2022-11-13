package `in`.silive.felix

import `in`.silive.felix.module.ChangePasswordRequest
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordFragment : Fragment() {

    lateinit var oldPasswordETxt : TextInputEditText
    lateinit var password1ETxt : TextInputEditText
    lateinit var password2ETxt : TextInputEditText
    lateinit var changePassBtn : AppCompatButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_change_password, container, false)

        oldPasswordETxt = view.findViewById(R.id.oldPasswordETxt)
        password1ETxt = view.findViewById(R.id.password1ETxt)
        password2ETxt = view.findViewById(R.id.password2ETxt)
        changePassBtn = view.findViewById(R.id.changePassBtn)

        changePassBtn.setOnClickListener{
            changePassword()
        }


        return view
    }

    fun changePassword(){
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.changePassword("Bearer " + (activity as HomePageActivity).token,ChangePasswordRequest((activity as HomePageActivity).email, oldPasswordETxt.text.toString(), password1ETxt.text.toString()))
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.code() == 200){
                    Toast.makeText(requireContext(), response.body().toString(), Toast.LENGTH_SHORT).show()
                }
                else if(response.code() == 404){
                    Toast.makeText(requireContext(), response.body().toString(), Toast.LENGTH_SHORT).show()
                }
                else if(response.code() == 409){
                    Toast.makeText(requireContext(), response.body().toString(), Toast.LENGTH_SHORT).show()
                }
                else if(response.code() == 401){
                    Toast.makeText(requireContext(), response.body().toString(), Toast.LENGTH_SHORT).show()
                }
                else if (response.code() == 500) {
                    Toast.makeText(
                        requireContext(),
                        "Internal Server Error. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else if (response.code() == 400) {
                    Toast.makeText(
                        requireContext(),
                        response.body().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else {
                    Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }

        })
    }

}