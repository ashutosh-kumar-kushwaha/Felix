package `in`.silive.felix

import `in`.silive.felix.datastore.DataStoreManager
import `in`.silive.felix.module.LogInInfo
import `in`.silive.felix.module.User
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.datastore.dataStore
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInFragment : Fragment() {

    lateinit var emailETxt : EditText
    lateinit var passwordETxt : EditText
    lateinit var passwordLayout : TextInputLayout
    lateinit var logInBtn : AppCompatButton
    lateinit var forgotPassTxtVw : TextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_log_in, container, false)

        passwordLayout = view.findViewById(R.id.passwordETxtLayout)
        emailETxt = view.findViewById(R.id.emailETxt)
        passwordETxt = view.findViewById(R.id.passwordETxt)
        logInBtn = view.findViewById(R.id.logInBtn)
        forgotPassTxtVw = view.findViewById(R.id.forgotPassTxtVw)
        forgotPassTxtVw.setOnClickListener {
            (activity as AuthenticationActivity).forgotPassFrag()
        }

        logInBtn.setOnClickListener{

            val user = User(null, emailETxt.text.toString(), null, null, passwordETxt.text.toString(), null)
            val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
            val call = retrofitAPI.sendUserData(user)

            call.enqueue(object: Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.body() == null){
                        Toast.makeText(view.context, "Invalid Email or Password", Toast.LENGTH_SHORT).show()
//                        var l = passwordLayout.layoutParams
//                        l.height = resources.getDimensionPixelSize(R.dimen.dp_74)
//                        passwordLayout.layoutParams = l
//                        passwordLayout.error = "Invalid email or password"
                    }
                    else{
//                        Toast.makeText(view.context, "Hello " + response.body()?.firstName.toString() + "!\nRole = " + response.body()?.role.toString(), Toast.LENGTH_SHORT).show()
                        Toast.makeText(view.context, response.headers().get("Set-Cookie").toString(), Toast.LENGTH_SHORT).show()

                        GlobalScope.launch(Dispatchers.IO) {
                            val dataStoreManager = DataStoreManager(view.context)
                            dataStoreManager.storeLogInInfo(LogInInfo(response.headers().get("Set-Cookie").toString(), true))
                        }

//                        runBlocking { view?.let { DataStoreManager(it.context).storeLogInInfo(
//                            LogInInfo(response.headers().get("Set-Cookie").toString(), true)
//                        ) } }



                        Log.d("Ashu", response.headers().get("Set-Cookie").toString())
//                        var l = passwordLayout.layoutParams
//                        l.height = resources.getDimensionPixelSize(R.dimen.dp_45)
//                        passwordLayout.layoutParams = l
                        Log.i("Ashu", response.headers().toString())

                        (activity as AuthenticationActivity).homePage()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(view.context, "Please check your internet connection", Toast.LENGTH_SHORT).show()
                    Log.i("Ashu", "Please check your internet connection")
                }
            })

        }


        return view
    }


}