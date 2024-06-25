package com.myperioddataismine

import net.zetetic.database.sqlcipher.SQLiteDatabase

interface Table {
    fun createTable(db: SQLiteDatabase)
    fun upgradeTable(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)
}
