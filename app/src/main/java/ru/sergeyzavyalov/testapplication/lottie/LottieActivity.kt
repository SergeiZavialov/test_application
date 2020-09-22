package ru.sergeyzavyalov.testapplication.lottie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.sergeyzavyalov.testapplication.R
import ru.sergeyzavyalov.testapplication.databinding.ActivityLottieBinding

class LottieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLottieBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLottieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btStartAnim.setOnClickListener {
                avRobot.playAnimation()
                avMoneyBag.playAnimation()
            }
        }
    }
}