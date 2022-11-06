package `in`.silive.felix

import `in`.silive.felix.module.ResetPasswordRequest
import `in`.silive.felix.module.User
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordFragment : Fragment() {
    lateinit var resetPassBtn : AppCompatButton
    lateinit var password1ETxt : TextInputEditText
    lateinit var password2ETxt : TextInputEditText
    lateinit var progressBar: AlertDialog
    var builder : AlertDialog.Builder? = null


    fun getDialogueProgressBar(view : View) : AlertDialog.Builder{
        if(builder==null){
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
        val view = inflater.inflate(R.layout.fragment_reset_password, container, false)

        progressBar = getDialogueProgressBar(view).create()
        progressBar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressBar.setCanceledOnTouchOutside(false)

        password1ETxt = view.findViewById(R.id.password1ETxt)
        password2ETxt = view.findViewById(R.id.password2ETxt)
        resetPassBtn = view.findViewById(R.id.resetPassBtn)
        resetPassBtn.setOnClickListener{
            resetPass()
        }
        return view
    }

    fun resetPass(){
        progressBar.show()
        val password1 = password1ETxt.text.toString()
        val password2 = password2ETxt.text.toString()

        if(password1 == password2){
            val msg = isValidPassword(password1)
            if(msg == "true"){
                val resetPasswordRequest = ResetPasswordRequest((activity as AuthenticationActivity).email, (activity as AuthenticationActivity).otp, password1)
                val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
                val call = retrofitAPI.resetPassword(resetPasswordRequest)
                call.enqueue(object : Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val user = User(null, (activity as AuthenticationActivity).email, null, null, password1, null)
                        val call2 = retrofitAPI.logIn(user)
                        call2.enqueue(object : Callback<User>{
                            override fun onResponse(call: Call<User>, response: Response<User>) {
                                if(response.body() == null){
                                    Toast.makeText(view?.context, "Invalid Email or Password", Toast.LENGTH_SHORT).show()
                                    progressBar.dismiss()
//                        var l = passwordLayout.layoutParams
//                        l.height = resources.getDimensionPixelSize(R.dimen.dp_74)
//                        passwordLayout.layoutParams = l
//                        passwordLayout.error = "Invalid email or password"
                                }
                                else{
//                        Toast.makeText(view.context, "Hello " + response.body()?.firstName.toString() + "!\nRole = " + response.body()?.role.toString(), Toast.LENGTH_SHORT).show()
                                    Toast.makeText(view?.context, response.headers().get("Set-Cookie").toString(), Toast.LENGTH_SHORT).show()
                                    Log.d("Ashu", response.headers().get("Set-Cookie").toString())
//                        var l = passwordLayout.layoutParams
//                        l.height = resources.getDimensionPixelSize(R.dimen.dp_45)
//                        passwordLayout.layoutParams = l
                                    Log.i("Ashu", response.headers().toString())

                                    progressBar.dismiss()

                                    (activity as AuthenticationActivity).homePage()
                                }
                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                Toast.makeText(view?.context, "Please check your internet connection", Toast.LENGTH_SHORT).show()
                                Log.i("Ashu", "Please check your internet connection")
                                progressBar.dismiss()
                            }

                        })
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(view?.context, "Failed", Toast.LENGTH_SHORT).show()
                        progressBar.dismiss()
                    }

                })
            }
            else{
                Toast.makeText(view?.context, msg, Toast.LENGTH_SHORT).show()
                progressBar.dismiss()
            }
        }
        else{
            if(password1 != password2){
                Toast.makeText(view?.context, "Enter the same password in both fields", Toast.LENGTH_SHORT).show()
                progressBar.dismiss()
            }
        }

    }

    fun isValidPassword(password: String): String {


        if(password.length < 8){
            return "Password must contain at least 8 characters"
        }

        var hasUpperCase = false
        var hasLowerCase = false
        var hasNumber = false
        var hasSpecialSymbol = false

        for(char in password){
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

        if(!hasUpperCase){
            return "Password must contain at least one uppercase character"
        }
        if(!hasLowerCase){
            return "Password must contain at least one lowercase character"
        }
        if(!hasNumber){
            return "Password must contain at least one number"
        }
        if(!hasSpecialSymbol){
            return "Password must contain at least one special symbol"
        }
        return "true"
    }

}