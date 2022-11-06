package `in`.silive.felix

import `in`.silive.felix.datastore.DataStoreManager
import `in`.silive.felix.module.LogInInfo
import `in`.silive.felix.module.User
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.datastore.dataStore
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInFragment : Fragment() {

    lateinit var emailETxt: TextInputEditText
    lateinit var passwordETxt: TextInputEditText
    lateinit var passwordLayout: TextInputLayout
    lateinit var logInBtn: AppCompatButton
    lateinit var forgotPassTxtVw: TextView
    var builder: AlertDialog.Builder? = null


    fun getDialogueProgressBar(view: View): AlertDialog.Builder {
        if (builder == null) {
            builder = AlertDialog.Builder(view.context)
            val progressBar = ProgressBar(view.context)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            progressBar.layoutParams = lp
            builder!!.setView(progressBar)
        }
        return builder as AlertDialog.Builder
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_log_in, container, false)

        val progressBar = getDialogueProgressBar(view).create()
        progressBar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressBar.setCanceledOnTouchOutside(false)

        passwordLayout = view.findViewById(R.id.passwordETxtLayout)
        emailETxt = view.findViewById(R.id.emailETxt)
        passwordETxt = view.findViewById(R.id.passwordETxt)
        logInBtn = view.findViewById(R.id.logInBtn)
        forgotPassTxtVw = view.findViewById(R.id.forgotPassTxtVw)
        forgotPassTxtVw.setOnClickListener {
            (activity as AuthenticationActivity).forgotPassFrag()
        }

        logInBtn.setOnClickListener {


            var email = emailETxt.text.toString()
            var password = passwordETxt.text.toString()
            email = email.trim()
            password = password.trim()

            val checkEmail = email.isValidEmail()
            val checkPass = password.isEmpty().not()


            if (!checkEmail) {
                emailETxt.error = "Enter a valid email"
            }
            if (!checkPass) {
                passwordLayout.helperText = "Enter a password"
            }

            if (checkEmail && checkPass) {
                progressBar.show()

                val user = User(null, email, null, null, password, null)
                val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
                val call = retrofitAPI.logIn(user)

                call.enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.body() == null) {
                            Toast.makeText(
                                view.context,
                                "Invalid Email or Password",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
//                        Toast.makeText(view.context, "Hello " + response.body()?.firstName.toString() + "!\nRole = " + response.body()?.role.toString(), Toast.LENGTH_SHORT).show()
                            Toast.makeText(
                                view.context,
                                response.headers().get("Set-Cookie").toString(),
                                Toast.LENGTH_SHORT
                            ).show()

                            GlobalScope.launch(Dispatchers.IO) {
                                val dataStoreManager = DataStoreManager(view.context)
                                dataStoreManager.storeLogInInfo(
                                    LogInInfo(
                                        response.headers().get("Set-Cookie").toString(), true
                                    )
                                )
                            }

//                        runBlocking { view?.let { DataStoreManager(it.context).storeLogInInfo(
//                            LogInInfo(response.headers().get("Set-Cookie").toString(), true)
//                        ) } }


                            Log.d("Ashu", response.headers().get("Set-Cookie").toString())

                            Log.i("Ashu", response.headers().toString())

                            (activity as AuthenticationActivity).homePage()
                        }
                        progressBar.dismiss()
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(
                            view.context,
                            "Please check your internet connection",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.i("Ashu", "Please check your internet connection")
                        progressBar.dismiss()
                    }
                })
            }


        }



        return view
    }

    fun String.isValidEmail() =
        !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()


}