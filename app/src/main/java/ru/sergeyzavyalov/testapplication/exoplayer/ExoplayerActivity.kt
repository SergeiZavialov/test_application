package ru.sergeyzavyalov.testapplication.exoplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import ru.sergeyzavyalov.testapplication.databinding.ActivityExoplayerBinding

class ExoplayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExoplayerBinding

    private lateinit var exoPlayer: SimpleExoPlayer

    private val mediaItem = MediaItem.fromUri("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExoplayerBinding.inflate(layoutInflater)

        exoPlayer = SimpleExoPlayer.Builder(this).build()

        setContentView(binding.root)

        with(binding) {
            playerView.player = exoPlayer
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()

            btStartPlay.setOnClickListener {
                startPlay()
            }
        }
    }

    private fun startPlay() {
        exoPlayer.play()
    }
}