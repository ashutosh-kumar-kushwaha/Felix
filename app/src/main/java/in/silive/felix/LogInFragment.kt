package `in`.silive.felix

import `in`.silive.felix.module.User
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInFragment : Fragment() {

    lateinit var emailETxt : EditText
    lateinit var passwordETxt : EditText
    lateinit var logInBtn : AppCompatButton



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_log_in, container, false)

        emailETxt = view.findViewById(R.id.emailETxt)
        passwordETxt = view.findViewById(R.id.passwordETxt)
        logInBtn = view.findViewById(R.id.logInBtn)
        logInBtn.setOnClickListener{


            val user = User(null, emailETxt.text.toString(), null, null, passwordETxt.text.toString(), null)
            val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
            val call = retrofitAPI.sendUserData(user)

            call.enqueue(object: Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.body() == null){
                        Toast.makeText(view.context, "Invalid Email or Password", Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(view.context, "Hello " + response.body()?.firstName.toString() + "!", Toast.LENGTH_LONG).show()
                        Log.i("Ashu", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(view.context, "Please check your internet connection", Toast.LENGTH_LONG).show()
                    Log.i("Ashu", "Please check your internet connection")
                }
            })

        }
        return view
    }


}