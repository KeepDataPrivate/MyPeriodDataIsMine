package com.myperioddataismine

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class Decrypt : AppCompatActivity() {
    private lateinit var encryptedDatabase: EncryptedDatabase
    private lateinit var passcodeEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.decrypt)

        encryptedDatabase = EncryptedDatabase(applicationContext)
        passcodeEditText = findViewById(R.id.passcode_edit_text)

        findViewById<Button?>(R.id.number_0_button).setOnClickListener {
            passcodeAppend('0')
        }
        findViewById<Button?>(R.id.number_1_button).setOnClickListener {
            passcodeAppend('1')
        }
        findViewById<Button?>(R.id.number_2_button).setOnClickListener {
            passcodeAppend('2')
        }
        findViewById<Button?>(R.id.number_3_button).setOnClickListener {
            passcodeAppend('3')
        }
        findViewById<Button?>(R.id.number_4_button).setOnClickListener {
            passcodeAppend('4')
        }
        findViewById<Button?>(R.id.number_5_button).setOnClickListener {
            passcodeAppend('5')
        }
        findViewById<Button?>(R.id.number_6_button).setOnClickListener {
            passcodeAppend('6')
        }
        findViewById<Button?>(R.id.number_7_button).setOnClickListener {
            passcodeAppend('7')
        }
        findViewById<Button?>(R.id.number_8_button).setOnClickListener {
            passcodeAppend('8')
        }
        findViewById<Button?>(R.id.number_9_button).setOnClickListener {
            passcodeAppend('9')
        }
        findViewById<Button?>(R.id.del_button).setOnClickListener {
            passcodeDrop()
        }
        findViewById<Button?>(R.id.ok_button).setOnClickListener {
            submit()
        }
    }

    private fun passcodeAppend(character: Char) {
        passcodeEditText.text.append(character)
    }

    private fun passcodeDrop() {
        val passcodeLength = passcodeEditText.length()
        if (passcodeLength == 0) return
        passcodeEditText.text.delete(passcodeLength - 1, passcodeLength)
    }

    private fun submit() {
        val passcode = passcodeEditText.text.toString()
        try {
            encryptedDatabase.open(passcode)
            dataDecryptedMessage("Database opened!")
            encryptedDatabase.close()
        } catch (e: Exception) {
            e.printStackTrace()
            dataErasePrompt()
        }
    }

    private fun dataDecryptedMessage(contents: String) {
        AlertDialog.Builder(this)
            .setTitle("Data decrypted!")
            .setMessage(contents)
            .setPositiveButton("OK") { _, _ -> }
            .show()
    }

    private fun dataErasePrompt() {
        AlertDialog.Builder(this)
            .setTitle("Erase data?")
            .setMessage("Unable to decrypt data!")
            .setPositiveButton("Retry") { _, _ -> }
            .setNegativeButton("Erase") { _, _ ->
                encryptedDatabase.delete()
            }
            .show()
    }
}
