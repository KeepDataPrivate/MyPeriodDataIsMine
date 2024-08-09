package com.myperioddataismine

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class PasscodeFragment : Fragment(R.layout.passcode_fragment) {
    private lateinit var passcodeMessageText: TextView
    private lateinit var passcodeEditText: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        passcodeMessageText = view.findViewById(R.id.passcode_message_text)
        passcodeEditText = view.findViewById(R.id.passcode_edit_text)

        setMessageText(arguments?.getString("Message"))

        view.findViewById<Button?>(R.id.number_0_button).setOnClickListener {
            passcodeAppend('0')
        }
        view.findViewById<Button?>(R.id.number_1_button).setOnClickListener {
            passcodeAppend('1')
        }
        view.findViewById<Button?>(R.id.number_2_button).setOnClickListener {
            passcodeAppend('2')
        }
        view.findViewById<Button?>(R.id.number_3_button).setOnClickListener {
            passcodeAppend('3')
        }
        view.findViewById<Button?>(R.id.number_4_button).setOnClickListener {
            passcodeAppend('4')
        }
        view.findViewById<Button?>(R.id.number_5_button).setOnClickListener {
            passcodeAppend('5')
        }
        view.findViewById<Button?>(R.id.number_6_button).setOnClickListener {
            passcodeAppend('6')
        }
        view.findViewById<Button?>(R.id.number_7_button).setOnClickListener {
            passcodeAppend('7')
        }
        view.findViewById<Button?>(R.id.number_8_button).setOnClickListener {
            passcodeAppend('8')
        }
        view.findViewById<Button?>(R.id.number_9_button).setOnClickListener {
            passcodeAppend('9')
        }
        view.findViewById<Button?>(R.id.del_button).setOnClickListener {
            passcodeDrop()
        }
        view.findViewById<Button?>(R.id.ok_button).setOnClickListener {
            submit()
        }
    }

    private fun setMessageText(message: String?) {
        passcodeMessageText.text = message ?: resources.getString(R.string.decrypt_text)
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
        (context as MainActivity).tryPasscode(passcodeEditText.text.toString())
    }
}
