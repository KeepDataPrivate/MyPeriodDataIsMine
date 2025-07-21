package com.myperioddataismine

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class WelcomeFragment : Fragment(R.layout.welcome_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.get_started_button).setOnClickListener {
            (context as MainActivity).getStarted()
        }
    }
}
