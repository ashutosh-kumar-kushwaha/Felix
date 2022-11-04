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

    lateinit var email : String

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

    fun resetPasswordFrag(view : View){
        val resetPasswordFrag = ResetPasswordFragment()
        replaceFrag(resetPasswordFrag, "resetPass")
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