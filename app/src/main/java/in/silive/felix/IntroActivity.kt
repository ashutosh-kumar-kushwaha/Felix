package `in`.silive.felix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View

class IntroActivity : AppCompatActivity() {
    fun authenticationActivity(view : View){
        val intent = Intent(this, AuthenticationActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayShowTitleEnabled(false)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.intro_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

}