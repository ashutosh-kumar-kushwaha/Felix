package `in`.silive.felix

import `in`.silive.felix.module.Email
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class EmailVerificationFragment : Fragment() {

    lateinit var resendBtn : AppCompatButton
    lateinit var timeTxtVw : AppCompatTextView
    var time : Int = 60
    var canResend = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_email_verification, container, false)

        resendBtn = view.findViewById(R.id.resendBtn)
        resendBtn.setOnClickListener{
            if(canResend){
                resendEmail()
            }
        }
        timeTxtVw = view.findViewById(R.id.timeTxtVw)

        updateTime()

        return view
    }


    fun resendEmail(){
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.resendVerificationLink(Email((activity as AuthenticationActivity).email))
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                time = 60
                Toast.makeText(view?.context, response.body(), Toast.LENGTH_SHORT).show()
                canResend = false
                updateTime()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(view?.context, "Failed", Toast.LENGTH_SHORT).show()
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
            }

        }.start()
    }

    fun printTime(time : Int) : String{
        val hr = time/60
        val min = time%60
        if(min < 10){
            return "${hr}:0${min}"
        }
        else{
            return "${hr}:${min}"
        }
    }

//    fun timer(){
//        Timer().schedule(object : TimerTask(){
//            override fun run() {
//                updateTime()
//            }
//
//        }, 0, 1000)
//    }


}

