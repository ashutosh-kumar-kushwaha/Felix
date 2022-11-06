package `in`.silive.felix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class AuthenticationActivity : AppCompatActivity() {

    lateinit var email : String
    lateinit var otp : String
    lateinit var password : String

    fun back(view : View){
        onBackPressed()
    }

    private fun replaceFrag(fragment : Fragment, name : String){
        val fm : FragmentManager = supportFragmentManager
        val ft : FragmentTransaction = fm.beginTransaction()
        ft.addToBackStack(name)
        ft.add(R.id.container, fragment)
        ft.commit()
    }

    fun signUpFrag(view: View){
        val signUpFrag = SignUpFragment()
        replaceFrag(signUpFrag, "signUp")
    }

    fun logInFrag(view : View){
        val fm = supportFragmentManager
        if(fm.backStackEntryCount > 0){
            fm.popBackStackImmediate()
            return
        }

        val logInFrag = LogInFragment()
        replaceFrag(logInFrag, "LogIn")
    }

    fun forgotPassFrag(){
        val forgotPasswordFrag = ForgotPasswordFragment()
        replaceFrag(forgotPasswordFrag, "forgotPass")
    }

    fun otpVerificationFrag(){
        val otpVerificationFrag = OtpVerificationFragment()
        replaceFrag(otpVerificationFrag, "otpVerify")
    }

    fun resetPasswordFrag(){
        val resetPasswordFrag = ResetPasswordFragment()
        replaceFrag(resetPasswordFrag, "resetPass")
    }

    fun emailVerifyFrag(){
        Log.d("Ashu", "Reset")
        val emailVerificationFragment = EmailVerificationFragment()
        replaceFrag(emailVerificationFragment, "emailVerify")
    }

    fun homePage(){
        val intent = Intent(applicationContext, HomePageActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        val fm : FragmentManager = supportFragmentManager
        val ft : FragmentTransaction = fm.beginTransaction()
        val logInFragment = LogInFragment()
        ft.add(R.id.container, logInFragment)
        ft.commit()

        setSupportActionBar(findViewById(R.id.toolbar))

        val actionBar = supportActionBar
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)

    }


}