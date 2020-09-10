package ru.sergeyzavyalov.testapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.sergeyzavyalov.testapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
    }
}
