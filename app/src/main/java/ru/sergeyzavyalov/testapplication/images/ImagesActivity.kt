package ru.sergeyzavyalov.testapplication.images

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import ru.sergeyzavyalov.testapplication.R
import ru.sergeyzavyalov.testapplication.databinding.ActivityImagesBinding

class ImagesActivity : AppCompatActivity(R.layout.activity_images) {

    private val binding by viewBinding<ActivityImagesBinding>()
    private val image =
        "https://smartcdn.prod.postmedia.digital/devondispatch/images?url=https://postmediacanoe.files.wordpress.com/2019/07/gettyimages-910314172-e1564420108411.jpg&w=840&h=630"
    private val gifFirst = "https://media.giphy.com/media/WXB88TeARFVvi/giphy.gif"

    private lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageLoader = ImageLoader.Builder(this)
            .componentRegistry {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder())
                } else {
                    add(GifDecoder())
                }
            }
            .build()

        setContentView(binding.root)

        binding.loadPicture.setOnClickListener {
            loadPictureWithGlide()
            loadPictureWithPicasso()
            loadPictureWithCoil()
        }

        binding.btLoadGifs.setOnClickListener {
            loadGifWithGlide()
            loadGifWithCoil()
        }
    }

    private fun loadPictureWithGlide() {
        Glide
            .with(this)
            .load(image)
            .placeholder(R.drawable.ic_cloud_download)
            .into(binding.ivGlideImage)
    }

    private fun loadPictureWithPicasso() {
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.ic_cloud_download)
            .into(binding.ivPicassoImage)
    }

    private fun loadPictureWithCoil() {
        binding.ivCoilImage.load(image) {
            placeholder(R.drawable.ic_cloud_download)
        }
    }

    private fun loadGifWithGlide() {
        Glide
            .with(this)
            .asGif()
            .load(gifFirst)
            .into(binding.ivGlideGif)
    }

    private fun loadGifWithCoil() {
        binding.ivCoilGif.load(gifFirst, imageLoader)
    }
}
