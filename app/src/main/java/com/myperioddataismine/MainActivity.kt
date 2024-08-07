package com.myperioddataismine

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val encryptedDatabase = (application as App).encryptedDatabase
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                if (encryptedDatabase.exists()) {
                    replace<PasscodeFragment>(R.id.main_container)
                } else {
                    replace<WelcomeFragment>(R.id.main_container)
                }
            }
        }
    }

    fun getStarted() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<PasscodeFragment>(R.id.main_container)
        }
    }

    fun databaseOpened() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<UserDataViewFragment>(R.id.main_container)
        }
    }

    fun getUserData(): UserData {
        return viewModel.getUserData()
    }

    fun saveUserData(userData: UserData) {
        viewModel.saveUserData(userData)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<UserDataViewFragment>(R.id.main_container)
        }
    }

    fun editUserData() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<UserDataEditFragment>(R.id.main_container)
        }
    }
}
