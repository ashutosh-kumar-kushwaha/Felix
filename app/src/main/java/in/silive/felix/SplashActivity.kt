package `in`.silive.felix

import `in`.silive.felix.datastore.DataStoreManager
import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.content.ClipDescription
import android.content.Intent
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.GlobalScope

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var intent = Intent(this@SplashActivity, IntroActivity::class.java)



        lifecycleScope.launch(Dispatchers.IO) {
            val dataStoreManager = DataStoreManager(this@SplashActivity)
            dataStoreManager.getLogInInfo().collect{
                if(it.logInState){
                    intent = Intent(this@SplashActivity, HomePageActivity::class.java)
                }
            }
        }

//        Log.d("Ashu", let {
//            DataStoreManager(it.applicationContext).getLogInInfo().collect(){}
//        })


//        runBlocking {
//            DataStoreManager(this@SplashActivity).getLogInInfo().collect {
//                if (it.logInState) {
//                    Log.d("Ashu", "A")
//                    intent = Intent(this@SplashActivity, HomePageActivity::class.java)
//                }
//            }
//        }

        val felixLL : LinearLayout = findViewById(R.id.felixLogoLL)
        val felixImages = arrayOf(R.drawable.f, R.drawable.e, R.drawable.l, R.drawable.i, R.drawable.x)

        for(i in 0..4){
            Handler(Looper.getMainLooper()).postDelayed({
                val img : ImageView = ImageView(this)
                img.setImageResource(felixImages[i])
                val params = LinearLayout.LayoutParams(resources.getDimensionPixelSize(R.dimen.dp_43), resources.getDimensionPixelSize(R.dimen.dp_80))
                img.layoutParams = params
                felixLL.addView(img)
            }, (300 + i*300).toLong())
        }


        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
            finish()
        }, 2000)



    }
}

