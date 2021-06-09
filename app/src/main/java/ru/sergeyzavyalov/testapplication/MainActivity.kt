package ru.sergeyzavyalov.testapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.sergeyzavyalov.testapplication.activity_result_api.ResultApiActivity
import ru.sergeyzavyalov.testapplication.databinding.ActivityMainBinding
import ru.sergeyzavyalov.testapplication.exoplayer.ExoplayerActivity
import ru.sergeyzavyalov.testapplication.images.ImagesActivity
import ru.sergeyzavyalov.testapplication.lottie.LottieActivity

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding<ActivityMainBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setupClicks()
    }

    private fun setupClicks() {
        with(binding) {
            btToImageLoaders.setOnClickListener {
                val intent = Intent(this@MainActivity, ImagesActivity::class.java)

                startActivity(intent)
            }

            btToExoplayer.setOnClickListener {
                val intent = Intent(this@MainActivity, ExoplayerActivity::class.java)

                startActivity(intent)
            }

            btToLottie.setOnClickListener {
                val intent = Intent(this@MainActivity, LottieActivity::class.java)

                startActivity(intent)
            }

            btToActivityResult.setOnClickListener {
                val intent = Intent(this@MainActivity, ResultApiActivity::class.java)

                startActivity(intent)
            }
        }
    }
}
