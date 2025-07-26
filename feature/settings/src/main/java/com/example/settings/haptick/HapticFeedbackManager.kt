package com.example.settings.haptick

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import javax.inject.Inject

class HapticFeedbackManager @Inject constructor(
    private val context: Context,
    private val preferencesManager: HaptickPreferenceManager
) {
    private val vibrator: Vibrator by lazy {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    fun vibrate(effect: VibrationEffect) {
        if (preferencesManager.isHapticEnabled()) {
            vibrator.vibrate(effect)
        }
    }

    fun performClickFeedback() {
        if (!preferencesManager.isHapticEnabled()) return

        when (preferencesManager.getSelectedHapticEffect()) {
            HapticEffect.CLICK -> performClick()
            HapticEffect.DOUBLE_CLICK -> performDoubleClick()
            HapticEffect.HEAVY_CLICK -> performHeavyClick()
        }
    }

    private fun performClick() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
        } else {
            @Suppress("DEPRECATION")
            vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    private fun performDoubleClick() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK))
        } else {
            @Suppress("DEPRECATION")
            vibrate(VibrationEffect.createWaveform(longArrayOf(0, 10, 50, 10), intArrayOf(0, 100, 0, 100), -1))
        }
    }

    private fun performHeavyClick() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
        } else {
            @Suppress("DEPRECATION")
            vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}

enum class HapticEffect {
    CLICK, DOUBLE_CLICK, HEAVY_CLICK
}