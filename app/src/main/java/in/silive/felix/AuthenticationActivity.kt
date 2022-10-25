package `in`.silive.felix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        val fm : FragmentManager = supportFragmentManager
        val ft : FragmentTransaction = fm.beginTransaction()
        val logInFragment = LogInFragment()
        ft.add(R.id.container, logInFragment)
        ft.commit()

        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

    }

}