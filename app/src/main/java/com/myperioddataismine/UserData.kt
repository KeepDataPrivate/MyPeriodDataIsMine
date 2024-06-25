package com.myperioddataismine

import net.zetetic.database.sqlcipher.SQLiteDatabase

class UserData() {
    var name: String = ""

    companion object: Table {
        // Table Name
        private const val TABLE_NAME: String = "Users"
        // Table columns
        private const val ID: String = "_id"
        private const val NAME: String = "Name"

        override fun createTable(db: SQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE $TABLE_NAME (" +
                        "$ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$NAME TEXT NOT NULL);"
            )
        }

        override fun upgradeTable(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL(
                "DROP TABLE IF EXISTS $TABLE_NAME;"
            )
            createTable(db)
        }

        fun get(db:SQLiteDatabase, id:Int = 1): UserData {
            val userData = UserData()
            val cursor = db.query(
                "SELECT $NAME FROM $TABLE_NAME WHERE $ID = $id;"
            )
            cursor.moveToFirst()
            if (cursor.isAfterLast) return userData
            userData.name = cursor.getString(0)
            return userData
        }

        fun save(userData: UserData, db: SQLiteDatabase, id: Int = 1) {
            db.execSQL(
                "REPLACE INTO $TABLE_NAME ($ID, $NAME)" +
                        "VALUES('$id', '${userData.name}');"
            )
        }
    }
}
