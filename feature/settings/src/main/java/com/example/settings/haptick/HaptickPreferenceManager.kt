package com.example.settings.haptick

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class HaptickPreferenceManager @Inject constructor(
    private val context: Context
) {
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    fun isHapticEnabled(): Boolean {
        return sharedPreferences.getBoolean("haptic_enabled", true)
    }

    fun setHapticEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("haptic_enabled", enabled).apply()
    }

    fun getSelectedHapticEffect(): HapticEffect {
        val effectName = sharedPreferences.getString("haptic_effect", HapticEffect.CLICK.name)
        return HapticEffect.valueOf(effectName ?: HapticEffect.CLICK.name)
    }

    fun setSelectedHapticEffect(effect: HapticEffect) {
        sharedPreferences.edit().putString("haptic_effect", effect.name).apply()
    }
}