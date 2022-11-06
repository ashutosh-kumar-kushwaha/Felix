package `in`.silive.felix

import `in`.silive.felix.module.Email
import `in`.silive.felix.module.User
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpFragment : Fragment() {

    lateinit var firstNameETxt : EditText
    lateinit var lastNameETxt : EditText
    lateinit var emailETxt : EditText
    lateinit var password1ETxt : EditText
    lateinit var password2ETxt : EditText
    lateinit var createAccBtn : AppCompatButton
    var builder : AlertDialog.Builder? = null


        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

            firstNameETxt = view.findViewById(R.id.firstNameETxt)
            lastNameETxt = view.findViewById(R.id.lastNameETxt)
            emailETxt = view.findViewById(R.id.emailETxt)
            password1ETxt = view.findViewById(R.id.password1ETxt)
            password2ETxt = view.findViewById(R.id.password2ETxt)
            createAccBtn = view.findViewById(R.id.createAccBtn)

            val progressBar = getDialogueProgressBar(view).create()
            progressBar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressBar.setCanceledOnTouchOutside(false)



            createAccBtn.setOnClickListener{

                progressBar.show()


                val firstName = firstNameETxt.text.toString()
                val lastName = lastNameETxt.text.toString()
                val email = emailETxt.text.toString()
                val password1 = password1ETxt.text.toString()
                val password2 = password2ETxt.text.toString()

                val checkFirstName = isValidName(firstName)
                val checkLastName = isValidName(lastName)
                val checkEmail = isValidEmail(email)

                if(checkFirstName){
                    if(checkLastName){
                        if(checkEmail){
                            if(password1 == password2){
                                val msg = isValidPassword(password1)
                                if(msg == "true"){

                                    val user = User(null, email, firstName, lastName, password1, null)
                                    val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
                                    val call = retrofitAPI.signUp(user)

                                    call.enqueue(object: Callback<String> {
                                        override fun onResponse(call: Call<String>, response: Response<String>) {
                                            Toast.makeText(view.context, response.body().toString(), Toast.LENGTH_SHORT).show()
                                            if(response.code() == 200){
                                                if(response.body() == "Please verify your email first."){

                                                    val call2 = retrofitAPI.resendVerificationLink(Email(email))
                                                    call2.enqueue(object : Callback<String>{
                                                        override fun onResponse(call: Call<String>, response: Response<String>) {
                                                            if(response.code()==200){
//                                                                Toast.makeText(view.context, response.body(), Toast.LENGTH_SHORT).show()
                                                                (activity as AuthenticationActivity).email = email
                                                                (activity as AuthenticationActivity).password = password1
                                                                (activity as AuthenticationActivity).emailVerifyFrag()
                                                            }
                                                        }

                                                        override fun onFailure(call: Call<String>, t: Throwable) {
                                                            Toast.makeText(view.context, "Failed", Toast.LENGTH_SHORT).show()
                                                        }

                                                    })

                                                }
                                                (activity as AuthenticationActivity).email = email
                                                (activity as AuthenticationActivity).password = password1
                                                (activity as AuthenticationActivity).emailVerifyFrag()
                                            }
                                            else{
                                                Toast.makeText(view.context, response.code().toString(), Toast.LENGTH_SHORT).show()

                                            }
                                            progressBar.dismiss()
                                        }

                                        override fun onFailure(call: Call<String>, t: Throwable) {
                                            Toast.makeText(view.context, "Please check your internet connection", Toast.LENGTH_SHORT).show()
                                            Log.i("Ashu", "Please check your internet connection")
                                            progressBar.dismiss()
                                        }
                                    })


                                }
                                else{
                                    Toast.makeText(view.context, msg, Toast.LENGTH_SHORT).show()
                                    progressBar.dismiss()                                }
                            }
                            else{
                                if(password1 != password2){
                                    Toast.makeText(view.context, "Enter the same password in both fields", Toast.LENGTH_SHORT).show()
                                    progressBar.dismiss()
                                }
                            }
                        }
                        else{
                            Toast.makeText(view.context, "Enter a valid email", Toast.LENGTH_SHORT).show()
                            progressBar.dismiss()
                        }
                    }
                    else{
                        Toast.makeText(view.context, "Enter a valid last name", Toast.LENGTH_SHORT).show()
                        progressBar.dismiss()
                    }
                }
                else{
                    Toast.makeText(view.context, "Enter a valid first name", Toast.LENGTH_SHORT).show()
                    progressBar.dismiss()
                }








            }


        return view
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


    fun isValidName(name: String): Boolean {
        if(name.isEmpty()){
            return false
        }
        for (char in name){
            if (char !in 'A'..'Z' && char !in 'a'..'z') {
                return false
            }
        }
        return true
    }

    fun isValidEmail(email : String) : Boolean{
        val indexOfAt = email.indexOf('@')
        val indexOfDot = email.lastIndexOf('.')
        return indexOfDot != -1 && indexOfAt != -1 && indexOfDot > indexOfAt
    }


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


}