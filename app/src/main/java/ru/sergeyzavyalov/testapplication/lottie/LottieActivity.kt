package ru.sergeyzavyalov.testapplication.lottie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.sergeyzavyalov.testapplication.databinding.ActivityLottieBinding

class LottieActivity : AppCompatActivity() {

    private val binding by viewBinding<ActivityLottieBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        with(binding) {
            btStartAnim.setOnClickListener {
                avRobot.playAnimation()
                avMoneyBag.playAnimation()
            }

            avFromNetworkFirst.setAnimationFromUrl("https://assets5.lottiefiles.com/packages/lf20_tFYxgv.json")

            avFromNetworkSecond.setAnimationFromUrl("https://assets8.lottiefiles.com/datafiles/AdjEeDEH5vWvOwB/data.json")
        }
    }
}