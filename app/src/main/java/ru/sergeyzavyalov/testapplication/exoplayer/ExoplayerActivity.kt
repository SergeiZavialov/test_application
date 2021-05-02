package ru.sergeyzavyalov.testapplication.exoplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import ru.sergeyzavyalov.testapplication.databinding.ActivityExoplayerBinding

class ExoplayerActivity : AppCompatActivity() {

    private val binding by viewBinding<ActivityExoplayerBinding>()

    private lateinit var exoPlayer: SimpleExoPlayer

    private val audioItem =
        MediaItem.fromUri("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3")

    private val videoItem =
        MediaItem.fromUri("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exoPlayer = SimpleExoPlayer.Builder(this).build()

        setContentView(binding.root)

        with(binding) {
            playerView.player = exoPlayer

            btPlayAudio.setOnClickListener {
                exoPlayer.setMediaItem(audioItem)
                exoPlayer.prepare()
                startPlay()
            }

            btPlayVideo.setOnClickListener {
                exoPlayer.setMediaItem(videoItem)
                exoPlayer.prepare()
                startPlay()
            }

            btStopPlay.setOnClickListener {
                stopPlay()
            }
        }
    }

    private fun startPlay() {
        exoPlayer.play()
    }

    private fun stopPlay() {
        exoPlayer.stop(true)
    }
}