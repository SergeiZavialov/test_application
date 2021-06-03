package ru.sergeyzavyalov.testapplication.activity_result_api

import android.Manifest
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import cards.pay.paycardsrecognizer.sdk.Card
import coil.load
import ru.sergeyzavyalov.testapplication.R
import ru.sergeyzavyalov.testapplication.activity_result_api.FileUtils.createUriFromFile
import ru.sergeyzavyalov.testapplication.activity_result_api.FileUtils.getOrCreateFile
import ru.sergeyzavyalov.testapplication.databinding.ActivityResultApiBinding

class ResultApiActivity : AppCompatActivity(R.layout.activity_result_api) {

    private val binding by viewBinding<ActivityResultApiBinding>()

    private val galleryPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) galleryContract.launch(IMAGE_TYPE)
        }

    private val cameraPreviewPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) cameraPreviewContract.launch(null)
        }

    private val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                /**
                 * Для работы с полноценным фото необходимо в контракт cameraContract передать URI
                 * файла, в который сохранится снимок. Поэтому сначала нужно создать сам файл и URI
                 * для него
                 */
                createUriFromFile(this)?.let {
                    cameraContract.launch(it)
                }
            }
        }

    private val galleryContract =
        registerForActivityResult(ActivityResultContracts.GetContent()) { image ->
            binding.ivImage.load(image)
        }

    /**
     * Данный контракт позволяет получить превью фото с камеры в виде Bitmap.
     * Для получения полноценного фото и возможности дальнейшей работы с ним
     * (например, обработки или обрезания) нужно вызывать cameraContract
     */
    private val cameraPreviewContract =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            binding.ivImage.load(it)
        }

    private val cameraContract = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            // Получаем URI из ранее созданного файла и грузим его в ImageView
            val uri = Uri.fromFile(getOrCreateFile(this))

            binding.ivImage.load(uri)
        }
    }

    // вызов кастомного контракта и маппинг данных во вью
    private val takeCardDataLauncher =
        registerForActivityResult(ScanCardActivityResultContract()) {
            if (it != null) {
                parseCardInfo(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupClicks()
    }

    private fun setupClicks() {
        with(binding) {
            btnFromGallery.setOnClickListener {
                // Делаем запрос на доступ к внутренней памяти
                galleryPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            btnFromCameraPreview.setOnClickListener {
                // Делаем запрос на доступ к камере
                cameraPreviewPermission.launch(Manifest.permission.CAMERA)
            }

            btnFromCamera.setOnClickListener {
                // Делаем запрос на доступ к камере
                cameraPermission.launch(Manifest.permission.CAMERA)
            }

            btnCustomContract.setOnClickListener {
                takeCardDataLauncher.launch(null)
            }
        }
    }

    private fun parseCardInfo(card: Card) {
        with(binding) {
            tvCardholder.text = card.cardHolderName
            tvCardNumber.text = card.cardNumber
        }
    }

    companion object {
        private const val IMAGE_TYPE = "image/*"
    }
}