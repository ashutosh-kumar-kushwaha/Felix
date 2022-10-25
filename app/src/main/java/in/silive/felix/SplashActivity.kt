package `in`.silive.felix

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
import androidx.core.content.res.ResourcesCompat
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout

class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val intent : Intent = Intent(this, IntroActivity::class.java)
        val felixLL : LinearLayout = findViewById(R.id.felixLogoLL)
        val felixImages = arrayOf(R.drawable.f, R.drawable.e, R.drawable.l, R.drawable.i, R.drawable.x)

        for(i in 0..4){
            Handler(Looper.getMainLooper()).postDelayed({
                val img : ImageView = ImageView(this)
                img.setImageResource(felixImages[i])
                val params = LinearLayout.LayoutParams(resources.getDimensionPixelSize(R.dimen.dp_50), resources.getDimensionPixelSize(R.dimen.dp_80))
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

