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
        1,
        null,
        null,
        true
    ) {

    private val tables = Array<Table>(1) {
        UserData
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
        const val DB_VERSION: Int = 1
    }
}
