package `in`.silive.felix

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton

class SignUpFragment : Fragment() {

    lateinit var firstNameETxt : EditText
    lateinit var lastNameETxt : EditText
    lateinit var emailETxt : EditText
    lateinit var password1ETxt : EditText
    lateinit var password2ETxt : EditText
    lateinit var createAccBtn : AppCompatButton


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

            createAccBtn.setOnClickListener{
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

                                    Toast.makeText(view.context, "Fine", Toast.LENGTH_SHORT).show()


                                }
                                else{
                                    Toast.makeText(view.context, msg, Toast.LENGTH_SHORT).show()
                                }
                            }
                            else{
                                if(password1 != password2){
                                    Toast.makeText(view.context, "Enter the same password in both fields", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else{
                            Toast.makeText(view.context, "Enter a valid email", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(view.context, "Enter a valid last name", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(view.context, "Enter a valid first name", Toast.LENGTH_SHORT).show()
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



}