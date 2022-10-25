package `in`.silive.felix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class AuthenticationActivity : AppCompatActivity() {

    fun back(view : View){
        finish()
    }

    private fun replaceFrag(fragment : Fragment){
        val fm : FragmentManager = supportFragmentManager
        val ft : FragmentTransaction = fm.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.commit()
    }

    fun signUpFrag(view: View){
        val signUpFrag = SignUpFragment()
        replaceFrag(signUpFrag)
    }

    fun logInFrag(view : View){
        val logInFrag = LogInFragment()
        replaceFrag(logInFrag)
    }

    fun forgotPassFrag(view : View){
        val forgotPasswordFrag = ForgotPasswordFragment()
        replaceFrag(forgotPasswordFrag)
    }

    fun otpVerificationFrag(view : View){
        val otpVerificationFrag = OtpVerificationFragment()
        replaceFrag(otpVerificationFrag)
    }

    fun resetPasswordFrag(view : View){
        val resetPasswordFrag = ResetPasswordFragment()
        replaceFrag(resetPasswordFrag)
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