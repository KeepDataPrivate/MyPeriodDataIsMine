package com.myperioddataismine

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class UserDataEditFragment : Fragment(R.layout.user_data_edit_fragment) {
    private lateinit var userData: UserData
    private lateinit var nameEditText: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userData = (context as MainActivity).getUserData()
        nameEditText = view.findViewById<EditText?>(R.id.name_edit_text)
        nameEditText.text = userData.name
        view.findViewById<Button?>(R.id.save_button).setOnClickListener {
            save()
        }
    }

    private fun save() {
        userData.name = nameEditText.text.toString()
        (context as MainActivity).saveUserData(userData)
    }
}
