package com.myperioddataismine

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import java.util.Calendar

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val encryptedDatabase = EncryptedDatabase(application)
    private var dayData: DayData? = null
    var date: Calendar = Calendar.getInstance()

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
        dayData = null
    }

    fun changePasscode(passcode: String) {
        encryptedDatabase.changePasscode(passcode)
    }

    fun getDayData(): DayData {
        val currentData = dayData ?: encryptedDatabase.getDayData(date)
        val updatedData = if (currentData.date != date) {
            encryptedDatabase.getDayData(date)
        } else {
            currentData
        }
        dayData = updatedData
        return updatedData
    }

    fun saveDayData(dayData: DayData) {
        encryptedDatabase.saveDayData(dayData)
    }
}
