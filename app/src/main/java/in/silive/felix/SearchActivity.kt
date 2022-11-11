package `in`.silive.felix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0,0);

        setContentView(R.layout.activity_search)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0);

    }
}