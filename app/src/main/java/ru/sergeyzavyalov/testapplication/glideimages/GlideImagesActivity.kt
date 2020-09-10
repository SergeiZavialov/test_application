package ru.sergeyzavyalov.testapplication.glideimages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import ru.sergeyzavyalov.testapplication.databinding.ActivityGlideImagesBinding

class GlideImagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGlideImagesBinding
    private val image = "https://smartcdn.prod.postmedia.digital/devondispatch/images?url=https://postmediacanoe.files.wordpress.com/2019/07/gettyimages-910314172-e1564420108411.jpg&w=840&h=630"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGlideImagesBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.loadPicture.setOnClickListener {
            loadPicture()
        }
    }

    private fun loadPicture() {
        Glide
            .with(this)
            .load(image)
            .centerCrop()
            .into(binding.imageView)
    }
}
