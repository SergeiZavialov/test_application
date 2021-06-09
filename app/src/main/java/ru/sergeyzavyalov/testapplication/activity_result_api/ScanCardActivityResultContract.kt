package ru.sergeyzavyalov.testapplication.activity_result_api

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import cards.pay.paycardsrecognizer.sdk.Card
import cards.pay.paycardsrecognizer.sdk.ScanCardIntent

/**
 * Кастомный контракт на основе ScanCardIntent из внешней библиотеки
 */
class ScanCardActivityResultContract : ActivityResultContract<Unit?, Card?>() {
    override fun createIntent(context: Context, input: Unit?): Intent =
        ScanCardIntent.Builder(context).build()

    override fun parseResult(resultCode: Int, intent: Intent?): Card? = when {
        resultCode != Activity.RESULT_OK -> null      // Return null, if action is cancelled
        else -> {
            intent?.getParcelableExtra(ScanCardIntent.RESULT_PAYCARDS_CARD)
        }
    }
}