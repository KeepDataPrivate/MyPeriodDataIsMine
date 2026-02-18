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
import java.util.Calendar

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
            menu.findItem(R.id.change_passcode).isVisible = false
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
                databaseOpened()
            }
            else -> {
                if (viewModel.openDatabase(passcode)) {
                    databaseOpened()
                } else {
                    databaseErasePrompt()
                }
            }
        }
    }

    private fun databaseOpened() {
        passcodeState = PasscodeState.None
        invalidateMenu()
        viewDayData()
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

    private fun viewDayData() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<DayDataFragment>(R.id.main_container)
        }
    }

    fun viewPreviousDay() {
        viewModel.date.add(Calendar.DATE, -1)
        viewDayData()
    }

    fun viewNextDay() {
        viewModel.date.add(Calendar.DATE, 1)
        viewDayData()
    }

    fun editField(field: DayData.Field, currentValue: Int) {
        val editFieldFragment = EditFieldFragment()
        val bundle = Bundle()
        bundle.putSerializable(EditFieldFragment.FIELD, field)
        bundle.putInt(EditFieldFragment.VALUE, currentValue)
        editFieldFragment.arguments = bundle
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.main_container, editFieldFragment)
            addToBackStack(null)
        }
    }

    fun saveField(field: DayData.Field, value: Int) {
        val dayData = viewModel.getDayData()
        when (field) {
            DayData.Field.FlowLevel -> dayData.flowLevel = value
            DayData.Field.Moods -> dayData.moods = value
            DayData.Field.Symptoms -> dayData.symptoms = value
        }
        viewModel.saveDayData(dayData)
        back()
    }

    fun back() {
        supportFragmentManager.popBackStack()
    }
}
