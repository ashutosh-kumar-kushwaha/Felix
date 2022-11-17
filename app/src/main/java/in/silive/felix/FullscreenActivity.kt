package `in`.silive.felix

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.widget.LinearLayout
import android.widget.TextView
import `in`.silive.felix.databinding.ActivityFullscreenBinding
import android.content.pm.ActivityInfo
import android.os.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView

class FullscreenActivity : AppCompatActivity() {

    lateinit var videoPlayer: StyledPlayerView
    lateinit var exoPlayer: ExoPlayer


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        exoPlayer = ExoPlayer.Builder(this).setSeekBackIncrementMs(10000).setSeekForwardIncrementMs(10000).build()
        videoPlayer = findViewById(R.id.videoPlayer)

        videoPlayer.player = exoPlayer

        val mediaItem = MediaItem.fromUri("")

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()

    }
}