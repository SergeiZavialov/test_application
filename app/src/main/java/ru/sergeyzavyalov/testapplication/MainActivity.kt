package ru.sergeyzavyalov.testapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.sergeyzavyalov.testapplication.databinding.ActivityMainBinding
import ru.sergeyzavyalov.testapplication.exoplayer.ExoplayerActivity
import ru.sergeyzavyalov.testapplication.glideimages.GlideImagesActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClicks()
    }

    private fun setupClicks() {
        binding.btToGlide.setOnClickListener {
            val intent = Intent(this, GlideImagesActivity::class.java)

            startActivity(intent)
        }

        binding.btToExoplayer.setOnClickListener {
            val intent = Intent(this, ExoplayerActivity::class.java)

            startActivity(intent)
        }
    }
}
