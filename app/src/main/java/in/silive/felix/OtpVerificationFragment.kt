package `in`.silive.felix

import `in`.silive.felix.module.Email
import `in`.silive.felix.module.VerifyOtpRequest
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OtpVerificationFragment : Fragment() {

    lateinit var continueBtn : AppCompatButton
    lateinit var otpETxt : EditText
    lateinit var resendOtpTxtVw : AppCompatTextView
    lateinit var timeTxtVw : AppCompatTextView
    var canResend = false
    var isSending = true
    var time : Int = 60
    lateinit var textView3 : TextView
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
        val view = inflater.inflate(R.layout.fragment_otp_verification, container, false)

        progressBar = getDialogueProgressBar(view).create()
        progressBar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressBar.setCanceledOnTouchOutside(false)

        timeTxtVw = view.findViewById(R.id.timeTxtVw)
        continueBtn = view.findViewById(R.id.continueBtn)
        otpETxt = view.findViewById(R.id.otpETxt)
        textView3 = view.findViewById(R.id.textView3)
        resendOtpTxtVw = view.findViewById(R.id.resendOtpTxtVw)
        textView3.text = "Enter the otp sent on this email ${(activity as AuthenticationActivity).email}"
                continueBtn.setOnClickListener{
            sendOtp()
        }
        resendOtpTxtVw.setOnClickListener {
            if(canResend && !isSending){
                resendOtp()
            }
        }
        updateTime()
        return view
    }

    private fun sendOtp() {
        progressBar.show()
        val otpRequest = VerifyOtpRequest((activity as AuthenticationActivity).email, otpETxt.text.toString())
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.verifyOtp(otpRequest)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Toast.makeText(view?.context, response.body(), Toast.LENGTH_SHORT).show()
                (activity as AuthenticationActivity).otp = otpETxt.text.toString()
                progressBar.dismiss()
                if(response.body()=="OTP Verified")
                    (activity as AuthenticationActivity).resetPasswordFrag()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(view?.context, "Failed", Toast.LENGTH_SHORT).show()
                progressBar.dismiss()
            }
        })
    }

    fun resendOtp(){
        progressBar.show()
        isSending = true
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.resendOtp(Email((activity as AuthenticationActivity).email))
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                time = 60
                Toast.makeText(view?.context, response.body(), Toast.LENGTH_SHORT).show()
                canResend = false
                updateTime()
                resendOtpTxtVw.text = "Resend OTP in"
                Log.d("Ashu", "Here")
                isSending = false
                progressBar.dismiss()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(view?.context, "Failed", Toast.LENGTH_SHORT).show()
                Log.d("Ashu", "Here")
                isSending = false
                progressBar.dismiss()
            }

        })

    }


    fun updateTime(){
        object : CountDownTimer(60000, 1000){
            override fun onTick(p0: Long) {
                time--
                timeTxtVw.text = printTime(time)
            }

            override fun onFinish() {
                canResend = true
                time=0
                resendOtpTxtVw.text = "Resend OTP"
                timeTxtVw.text = ""
                isSending = false
            }

        }.start()
    }

    fun printTime(time : Int) : String{
        val hr = time/60
        val min = time%60
        return if(min < 10){
            "${hr}:0${min}"
        } else{
            "${hr}:${min}"
        }
    }

}