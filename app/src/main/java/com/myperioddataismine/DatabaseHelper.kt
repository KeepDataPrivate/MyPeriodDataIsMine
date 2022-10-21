package com.myperioddataismine

import android.content.Context
import net.zetetic.database.sqlcipher.SQLiteDatabase
import net.zetetic.database.sqlcipher.SQLiteOpenHelper

class DatabaseHelper(context: Context, filename: String, passcode: String) :
    SQLiteOpenHelper(
        context,
        filename,
        passcode,
        null,
        DB_VERSION,
        2,
        null,
        null,
        true
    ) {

    private val tables = Array(1) {
        when (it) {
            0 -> DayData
            else -> throw Exception("DatabaseHelper's tables array creation index error.")
        }
    }

   override fun onCreate(db: SQLiteDatabase) {
       for (table in tables) {
           table.createTable(db)
       }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        for (table in tables) {
            table.upgradeTable(db, oldVersion, newVersion)
        }
    }

    companion object {
        const val DB_VERSION: Int = 2
    }
}
