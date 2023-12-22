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
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        const val DB_VERSION: Int = 1

        // Table Name
        const val TABLE_NAME: String = "Users"
        // Table columns
        const val ID: String = "_id"
        const val NAME: String = "Name"

        // Creating table query
        private const val CREATE_TABLE = (
            "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT NOT NULL);"
        )
    }
}
