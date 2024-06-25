package com.myperioddataismine

import android.app.Application

class App: Application() {
    val encryptedDatabase = EncryptedDatabase(this)
}
