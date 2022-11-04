package `in`.silive.felix

import `in`.silive.felix.module.VerifyOtpRequest
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OtpVerificationFragment : Fragment() {

    lateinit var continueBtn : AppCompatButton
    lateinit var otpETxt : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_otp_verification, container, false)
        continueBtn = view.findViewById(R.id.continueBtn)
        otpETxt = view.findViewById(R.id.otpETxt)
        continueBtn.setOnClickListener{
            sendOtp()
        }
        return view
    }

    private fun sendOtp() {
        val otpRequest = VerifyOtpRequest((activity as AuthenticationActivity).email, otpETxt.text.toString())
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.verifyOtp(otpRequest)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Toast.makeText(view?.context, response.body(), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(view?.context, "Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

}