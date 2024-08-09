package com.myperioddataismine

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val encryptedDatabase = EncryptedDatabase(application)

    fun encryptedDatabaseExists(): Boolean {
        return encryptedDatabase.exists()
    }

    fun openDatabase(passcode: String): Boolean {
        return encryptedDatabase.open(passcode)
    }

    fun deleteDatabase() {
        encryptedDatabase.delete()
    }

    fun getUserData(): UserData {
        return encryptedDatabase.getUserData()
    }

    fun saveUserData(userData: UserData) {
        encryptedDatabase.saveUserData(userData)
    }
}
