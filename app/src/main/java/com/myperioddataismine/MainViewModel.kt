package com.myperioddataismine

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val encryptedDatabase = (application as App).encryptedDatabase

    fun getUserData(): UserData {
        return encryptedDatabase.getUserData()
    }

    fun saveUserData(userData: UserData) {
        encryptedDatabase.saveUserData(userData)
    }
}
