package com.myperioddataismine

import android.content.Context
import android.database.sqlite.SQLiteException

class EncryptedDatabase(private val context: Context) {
    private var databaseHelper: DatabaseHelper? = null

    init {
        System.loadLibrary("sqlcipher")
    }

    fun exists(): Boolean {
        return context.getDatabasePath(DB_FILENAME).exists()
    }

    fun open(passcode: String): Boolean {
        databaseHelper = DatabaseHelper(context, DB_FILENAME, passcode)
        try {
            val database = databaseHelper?.writableDatabase
            if (database != null) {
                return true
            }
        } catch (_: SQLiteException) {
            // Unable to open an existing database file with the incorrect passcode.
        }
        return false
    }

    fun delete() {
        val databaseFile = context.getDatabasePath(DB_FILENAME)
        databaseFile.delete()
    }

    fun getUserData(): UserData {
        return UserData.get(databaseHelper!!.writableDatabase)
    }

    fun saveUserData(userData: UserData) {
        UserData.save(userData, databaseHelper!!.writableDatabase)
    }

    companion object {
        const val DB_FILENAME: String = "encrypted.db"
    }
}
