package com.myperioddataismine

import android.content.Context

class EncryptedDatabase(private val context: Context) {
    private lateinit var databaseHelper: DatabaseHelper

    init {
        System.loadLibrary("sqlcipher")
    }

    fun open(passcode: String) {
        databaseHelper = DatabaseHelper(context, DB_FILENAME, passcode)
        databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
    }

    fun delete() {
        val databaseFile = context.getDatabasePath(DB_FILENAME)
        databaseFile.delete()
    }

    companion object {
        const val DB_FILENAME: String = "encrypted.db"
    }
}
