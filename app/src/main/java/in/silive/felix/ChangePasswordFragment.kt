package `in`.silive.felix

import `in`.silive.felix.module.ChangePasswordRequest
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
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordFragment : Fragment() {

    lateinit var oldPasswordETxt : TextInputEditText
    lateinit var password1ETxt : TextInputEditText
    lateinit var password2ETxt : TextInputEditText
    lateinit var oldPasswordETxtLayout: TextInputLayout
    lateinit var password1ETxtLayout: TextInputLayout
    lateinit var password2ETxtLayout: TextInputLayout
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
        oldPasswordETxtLayout = view.findViewById(R.id.oldPasswordETxtLayout)
        password1ETxtLayout = view.findViewById(R.id.password1ETxtLayout)
        password2ETxtLayout = view.findViewById(R.id.password2ETxtLayout)

        changePassBtn.setOnClickListener{

            val oldPassword = oldPasswordETxt.text.toString()
            val password1 = password1ETxt.text.toString()
            val password2 = password2ETxt.text.toString()
            if(password1 == password2){
                val msg = isValidPassword(password1)
                if(msg == "true"){
                    password2ETxtLayout.helperText = ""
                    changePassword()
                }
                else{
                    password2ETxtLayout.helperText = msg
                }
            }
            else{
                password2ETxtLayout.helperText = "Both passwords must be same"
            }
        }

        return view
    }

    fun changePassword(){
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.changePassword("Bearer " + (activity as HomePageActivity).token,ChangePasswordRequest((activity as HomePageActivity).email, oldPasswordETxt.text.toString(), password1ETxt.text.toString()))
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(context != null) {
                    if (response.code() == 200) {
                        Toast.makeText(
                            requireContext(),
                            "Password changed successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (response.code() == 404) {
                        Toast.makeText(
                            requireContext(),
                            "User with email not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (response.code() == 409) {
                        Toast.makeText(
                            requireContext(),
                            "New password can't be same as old password",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (response.code() == 401) {
                        Log.d("Ashu", response.body().toString())
                        Toast.makeText(requireContext(), "Invalid Password", Toast.LENGTH_SHORT)
                            .show()
                    } else if (response.code() == 500) {
                        Toast.makeText(
                            requireContext(),
                            "Internal Server Error. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (response.code() == 400) {
                        Toast.makeText(
                            requireContext(),
                            response.body().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                if(context != null) {
                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    fun isValidPassword(password: String): String {

        if (password.length < 8) {
            return "Password must contain at least 8 characters"
        }

        var hasUpperCase = false
        var hasLowerCase = false
        var hasNumber = false
        var hasSpecialSymbol = false

        for (char in password) {
            when (char) {
                in 'A'..'Z' -> {
                    hasUpperCase = true
                }
                in 'a'..'z' -> {
                    hasLowerCase = true
                }
                in '0'..'9' -> {
                    hasNumber = true
                }
                else -> {
                    hasSpecialSymbol = true
                }
            }
        }

        if (!hasUpperCase) {
            return "Password must contain at least one uppercase character"
        }
        if (!hasLowerCase) {
            return "Password must contain at least one lowercase character"
        }
        if (!hasNumber) {
            return "Password must contain at least one number"
        }
        if (!hasSpecialSymbol) {
            return "Password must contain at least one special symbol"
        }
        return "true"
    }

}