package com.myperioddataismine

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class UserDataViewFragment : Fragment(R.layout.user_data_view_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userData = (context as MainActivity).getUserData()
        val nameTextView = view.findViewById<TextView?>(R.id.name)
        nameTextView.text = userData.name
        view.findViewById<Button?>(R.id.edit_button).setOnClickListener {
            edit()
        }
    }

    private fun edit() {
        (context as MainActivity).editUserData()
    }
}
