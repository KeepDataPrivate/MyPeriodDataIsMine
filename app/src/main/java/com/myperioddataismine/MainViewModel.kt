package com.myperioddataismine

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val encryptedDatabase = EncryptedDatabase(application)
    private var userData: UserData? = null

    fun encryptedDatabaseExists(): Boolean {
        return encryptedDatabase.exists()
    }

    fun decryptedDatabaseIsOpen(): Boolean {
        return encryptedDatabase.isOpen()
    }

    fun openDatabase(passcode: String): Boolean {
        return encryptedDatabase.open(passcode)
    }

    fun deleteDatabase() {
        encryptedDatabase.delete()
        userData = null
    }

    fun changePasscode(passcode: String) {
        encryptedDatabase.changePasscode(passcode)
    }

    fun getUserData(): UserData {
        val data = userData ?: encryptedDatabase.getUserData()
        userData = userData ?: data
        return data
    }

    fun saveUserData(userData: UserData) {
        encryptedDatabase.saveUserData(userData)
    }
}
