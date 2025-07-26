package com.example.settings.pin

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject

class PinCodeManager @Inject constructor(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_pin_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )


    fun savePin(pin: String) {
        sharedPreferences.edit().putString("user_pin", pin).apply()
    }

    fun getPin(): String? {
        return sharedPreferences.getString("user_pin", null)
    }

    fun isPinSet(): Boolean {
        return sharedPreferences.contains("user_pin")
    }

    fun clearPin() {
        sharedPreferences.edit().remove("user_pin").apply()
    }
}