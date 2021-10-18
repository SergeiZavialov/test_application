package ru.sergeyzavyalov.testapplication.activity_result_api

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import ru.sergeyzavyalov.testapplication.BuildConfig
import java.io.File

object FileUtils {

    fun createUriFromFile(context: Context): Uri? = getOrCreateFile(context)?.let {
        FileProvider.getUriForFile(
            context,
            BuildConfig.APPLICATION_ID + ".provider",
            it
        )
    }

    fun getOrCreateFile(context: Context): File? {
        val file = File(context.filesDir, PHOTO_NAME)

        return if (file.createNewFile() || file.exists()) file else null
    }

    private const val PHOTO_NAME = "photo_name"
}