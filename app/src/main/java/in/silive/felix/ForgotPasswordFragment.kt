package `in`.silive.felix

import `in`.silive.felix.module.Email
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordFragment : Fragment() {
    lateinit var continueBtn: AppCompatButton
    lateinit var emailETxt: TextInputEditText
    var builder: AlertDialog.Builder? = null
    lateinit var progressBar: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forgot_password, container, false)
        continueBtn = view.findViewById(R.id.continueBtn)
        emailETxt = view.findViewById(R.id.emailETxt)
        progressBar = getDialogueProgressBar(view).create()
        progressBar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressBar.setCanceledOnTouchOutside(false)

        continueBtn.setOnClickListener {
            sendEmail(it)
        }
        return view
    }

    fun sendEmail(view: View) {
        val email = Email(emailETxt.text.toString())
        if (email.email.isValidEmail()) {

            progressBar.show()

            val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
            val call = retrofitAPI.forgotPassword(email)
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    (activity as AuthenticationActivity).email = email.email
                    Toast.makeText(view.context, response.body().toString(), Toast.LENGTH_SHORT)
                        .show()
                    progressBar.dismiss()
                    if (response.body() == "OTP sent on the given mail")
                        (activity as AuthenticationActivity).otpVerificationFrag()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(view.context, "Failed", Toast.LENGTH_SHORT).show()
                    progressBar.dismiss()
                }
            })
        }
        else{
            emailETxt.error = "Enter a valid email"
        }
    }

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

    fun String.isValidEmail() =
        !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()


}