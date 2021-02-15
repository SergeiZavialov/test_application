package ru.sergeyzavyalov.testapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.sergeyzavyalov.testapplication.databinding.ActivityMainBinding
import ru.sergeyzavyalov.testapplication.exoplayer.ExoplayerActivity
import ru.sergeyzavyalov.testapplication.images.ImagesActivity
import ru.sergeyzavyalov.testapplication.lottie.LottieActivity

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding<ActivityMainBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setupClicks()
    }

    private fun setupClicks() {
        binding.btToImageLoaders.setOnClickListener {
            val intent = Intent(this, ImagesActivity::class.java)

            startActivity(intent)
        }

        binding.btToExoplayer.setOnClickListener {
            val intent = Intent(this, ExoplayerActivity::class.java)

            startActivity(intent)
        }

        binding.btToLottie.setOnClickListener {
            val intent = Intent(this, LottieActivity::class.java)

            startActivity(intent)
        }
    }
}
