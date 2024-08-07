package com.myperioddataismine

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
        DecryptDatabase,
        ChangePasscode
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        if (!viewModel.decryptedDatabaseIsOpen()) {
            menu.findItem(R.id.change_passcode).setVisible(false)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.change_passcode -> {
                changePasscode()
                true
            }
            else -> super.onOptionsItemSelected(item)
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

    private fun changePasscode() {
        getPasscode(PasscodeState.ChangePasscode)
    }

    private fun getPasscode(passcodeState: PasscodeState) {
        this.passcodeState = passcodeState
        invalidateMenu()
        val message = when (passcodeState) {
            PasscodeState.CreateDatabase -> resources.getString(R.string.encrypt_text)
            PasscodeState.DecryptDatabase -> resources.getString(R.string.decrypt_text)
            PasscodeState.ChangePasscode -> resources.getString(R.string.change_passcode_text)
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
        when (passcodeState) {
            PasscodeState.ChangePasscode -> {
                viewModel.changePasscode(passcode)
                viewUserData()
            }
            else -> {
                if (viewModel.openDatabase(passcode)) {
                    viewUserData()
                } else {
                    databaseErasePrompt()
                }
            }
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
        passcodeState = PasscodeState.None
        invalidateMenu()
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
