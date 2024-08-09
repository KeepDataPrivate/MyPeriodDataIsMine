package com.myperioddataismine

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private var passcodeState = PasscodeState.None

    private enum class PasscodeState {
        None,
        CreateDatabase,
        DecryptDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            if (viewModel.encryptedDatabaseExists()) {
                decryptDatabase()
            } else {
                welcome()
            }
        }
    }

    private fun welcome() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<WelcomeFragment>(R.id.main_container)
        }
    }

    fun getStarted() {
        createDatabase()
    }

    private fun createDatabase() {
        getPasscode(PasscodeState.CreateDatabase)
    }

    private fun decryptDatabase() {
        getPasscode(PasscodeState.DecryptDatabase)
    }

    private fun getPasscode(passcodeState: PasscodeState) {
        this.passcodeState = passcodeState
        invalidateMenu()
        val message = when (passcodeState) {
            PasscodeState.CreateDatabase -> resources.getString(R.string.encrypt_text)
            PasscodeState.DecryptDatabase -> resources.getString(R.string.decrypt_text)
            PasscodeState.None -> throw Exception("Passcode state should never be None")
        }
        val bundle = Bundle()
        bundle.putString("Message", message)
        val passcodeFragment = PasscodeFragment()
        passcodeFragment.arguments = bundle
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.main_container, passcodeFragment)
        }
    }

    fun tryPasscode(passcode: String) {
        passcodeState = PasscodeState.None
        if (viewModel.openDatabase(passcode)) {
            viewUserData()
        } else {
            databaseErasePrompt()
        }
    }

    private fun databaseErasePrompt() {
        AlertDialog.Builder(this)
            .setTitle("Erase data?")
            .setMessage("Unable to decrypt data!")
            .setPositiveButton("Retry") { _, _ ->
                decryptDatabase()
            }
            .setNegativeButton("Erase") { _, _ ->
                viewModel.deleteDatabase()
                createDatabase()
            }
            .show()
    }

    private fun viewUserData() {
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

    fun getUserData(): UserData {
        return viewModel.getUserData()
    }

    fun saveUserData(userData: UserData) {
        viewModel.saveUserData(userData)
        viewUserData()
    }
}
