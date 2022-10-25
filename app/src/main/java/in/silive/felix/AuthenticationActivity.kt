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

    fun signUpFrag(view: View){
        val fm : FragmentManager = supportFragmentManager
        val ft : FragmentTransaction = fm.beginTransaction()
        val signUpFrag = SignUpFragment()
        ft.replace(R.id.container, signUpFrag)
        ft.commit()
    }

    fun logInFrag(view : View){
        val fm : FragmentManager = supportFragmentManager
        val ft : FragmentTransaction = fm.beginTransaction()
        val logInFrag = LogInFragment()
        ft.replace(R.id.container, logInFrag)
        ft.commit()
    }

    fun forgotPassFrag(view : View){
        val fm : FragmentManager = supportFragmentManager
        val ft : FragmentTransaction = fm.beginTransaction()
        val forgotPasswordFrag = ForgotPasswordFragment()
        ft.replace(R.id.container, forgotPasswordFrag)
        ft.commit()
    }

    fun otpVerificationFrag(view : View){
        val fm : FragmentManager = supportFragmentManager
        val ft : FragmentTransaction = fm.beginTransaction()
        val otpVerificationFrag = OtpVerificationFragment()
        ft.replace(R.id.container, otpVerificationFrag)
        ft.commit()
    }

    fun resetPasswordFrag(view : View){
        val fm : FragmentManager = supportFragmentManager
        val ft : FragmentTransaction = fm.beginTransaction()
        val resetPasswordFrag = ResetPasswordFragment()
        ft.replace(R.id.container, resetPasswordFrag)
        ft.commit()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}