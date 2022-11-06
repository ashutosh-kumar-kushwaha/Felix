package `in`.silive.felix

import `in`.silive.felix.datastore.DataStoreManager
import `in`.silive.felix.module.Email
import `in`.silive.felix.module.LogInInfo
import `in`.silive.felix.module.User
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class EmailVerificationFragment : Fragment() {

    lateinit var resendBtn : AppCompatButton
    lateinit var timeTxtVw : AppCompatTextView
    var time : Int = 60
    var canResend = false
    var isSending = true
    lateinit var resendEmailTxtVw : AppCompatTextView
    lateinit var emailSentTxtVw : AppCompatTextView
    var timer = Timer()
    var builder : AlertDialog.Builder? = null
    var isTimerCancelled = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_email_verification, container, false)

        emailSentTxtVw = view.findViewById(R.id.emailSentTxtVw)
        emailSentTxtVw.text = "We have sent a verification email to ${(activity as AuthenticationActivity).email}"
        resendEmailTxtVw = view.findViewById(R.id.resendMailTxtVw)
        resendBtn = view.findViewById(R.id.resendBtn)

        val progressBar = getDialogueProgressBar(view).create()
        progressBar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressBar.setCanceledOnTouchOutside(false)

        resendBtn.setOnClickListener{
            Log.d("Ashu", isSending.toString())
            if(canResend && !isSending){
                resendEmail()
            }
        }
        timeTxtVw = view.findViewById(R.id.timeTxtVw)

        updateTime()
        resendBtn.alpha = 0.5f

        isTimerCancelled = false
        timer.schedule(object : TimerTask(){
            override fun run() {


                val user = User(
                    null,
                    (activity as AuthenticationActivity).email,
                    null,
                    null,
                    (activity as AuthenticationActivity).password,
                    null
                )
                val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
                val call = retrofitAPI.logIn(user)
                Log.d("Ashu", "Ashu")
                call.enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.body() != null) {


                            GlobalScope.launch(Dispatchers.IO) {
                                val dataStoreManager = DataStoreManager(view.context)
                                dataStoreManager.storeLogInInfo(
                                    LogInInfo(
                                        response.headers().get("Set-Cookie").toString(), true
                                    )
                                )
                            }

                            Log.d("Ashu", response.body().toString())

                            if(!isTimerCancelled){
                                timer.cancel()
                                isTimerCancelled = true
                            }


                            (activity as AuthenticationActivity).homePage()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(
                            view.context,
                            "Please check your internet connection",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("Ashu", "Please check your internet connection")
                    }
                })
            }



        }, 0, 3000)

        return view
    }


    fun resendEmail(){
        isSending = true
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.resendVerificationLink(Email((activity as AuthenticationActivity).email))
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                time = 60
                Toast.makeText(view?.context, response.body(), Toast.LENGTH_SHORT).show()
                canResend = false
                updateTime()
                resendEmailTxtVw.text = "Resend email in"
                resendBtn.alpha = 0.5f
                Log.d("Ashu", "Here")
                isSending = false
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(view?.context, "Failed", Toast.LENGTH_SHORT).show()
                Log.d("Ashu", "Here")
                isSending = false
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
                resendEmailTxtVw.text = ""
                timeTxtVw.text = ""
                resendBtn.alpha = 1f
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


    override fun onPause() {
        if(!isTimerCancelled){
            timer.cancel()
            isTimerCancelled = true
        }
        super.onPause()
    }

    override fun onResume() {
        if (isTimerCancelled) {
            isTimerCancelled = false
            timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {


                    val user = User(
                        null,
                        (activity as AuthenticationActivity).email,
                        null,
                        null,
                        (activity as AuthenticationActivity).password,
                        null
                    )
                    val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
                    val call = retrofitAPI.logIn(user)
                    Log.d("Ashu", "Ashu")
                    call.enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            if (response.body() != null) {


                                GlobalScope.launch(Dispatchers.IO) {
                                    val dataStoreManager = DataStoreManager(view!!.context)
                                    dataStoreManager.storeLogInInfo(
                                        LogInInfo(
                                            response.headers().get("Set-Cookie").toString(), true
                                        )
                                    )
                                }

                                Log.d("Ashu", response.body().toString())

                                if (!isTimerCancelled)
                                    timer.cancel()

                                (activity as AuthenticationActivity).homePage()
                            }
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            Toast.makeText(
                                view!!.context,
                                "Please check your internet connection",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("Ashu", "Please check your internet connection")
                        }
                    })
                }


            }, 0, 3000)

        }
        super.onResume()
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

