package ru.sergeyzavyalov.testapplication.activity_result_api

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.sergeyzavyalov.testapplication.databinding.ActivityResultApiBinding

class ResultApiActivity : AppCompatActivity() {

    private val binding by viewBinding<ActivityResultApiBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}