package com.myperioddataismine

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.flexbox.FlexboxLayout
import java.text.DateFormat
import java.util.Calendar

class DayDataFragment : Fragment(R.layout.day_data_fragment) {
    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = context as MainActivity
        val dayData = viewModel.getDayData()
        val previousIcon = view.findViewById<ImageView>(R.id.previous_day)
        val nextIcon = view.findViewById<ImageView>(R.id.next_day)
        val flowLevelLayout = view.findViewById<LinearLayout>(R.id.flow_level)
        val moodsLayout = view.findViewById<FlexboxLayout>(R.id.moods)
        val symptomsLayout = view.findViewById<FlexboxLayout>(R.id.symptoms)

        setDate(view, dayData.date)
        setFlowLevel(flowLevelLayout, dayData.flowLevel)
        setMoods(moodsLayout, dayData.moods)
        setSymptoms(symptomsLayout, dayData.symptoms)

        previousIcon.setOnClickListener { mainActivity.viewPreviousDay() }
        if (dayData.isToday()) {
            nextIcon.setColorFilter(Color.LTGRAY)
        } else {
            nextIcon.setOnClickListener { mainActivity.viewNextDay() }
        }
        flowLevelLayout.setOnClickListener {
            mainActivity.editField(DayData.Field.FlowLevel, dayData.flowLevel)
        }
        moodsLayout.setOnClickListener {
            mainActivity.editField(DayData.Field.Moods, dayData.moods)
        }
        symptomsLayout.setOnClickListener {
            mainActivity.editField(DayData.Field.Symptoms, dayData.symptoms)
        }
    }

    private fun setDate(view: View, date: Calendar) {
        val dateTextView = view.findViewById<TextView>(R.id.date)
        val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)
        val dateString = dateFormat.format(date.time)
        dateTextView.text = dateString
    }

    private fun setFlowLevel(flowLevelViewGroup: ViewGroup, flowLevel: Int) {
        if (flowLevel != 0 && flowLevel in DayData.flowLevelValues.indices) {
            addImage(flowLevelViewGroup, DayData.flowLevelValues[flowLevel])
        } else {
            addText(flowLevelViewGroup, R.string.none)
        }
    }

    private fun setMoods(moodsViewGroup: ViewGroup, moods: Int) {
        for (moodIndex in DayData.moodValues.indices) {
            if (((moods shr moodIndex) and 1) != 0) {
                addImage(moodsViewGroup, DayData.moodValues[moodIndex])
            }
        }
        if (moodsViewGroup.childCount == 0) {
            addText(moodsViewGroup, R.string.none)
        }
    }

    private fun setSymptoms(symptomsViewGroup: ViewGroup, symptoms: Int) {
        for (symptomIndex in DayData.symptomValues.indices) {
            if (((symptoms shr symptomIndex) and 1) != 0) {
                addImage(symptomsViewGroup, DayData.symptomValues[symptomIndex])
            }
        }
        if (symptomsViewGroup.childCount == 0) {
            addText(symptomsViewGroup, R.string.none)
        }
    }

    private fun addText(viewGroup: ViewGroup, stringID: Int) {
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val textLayout = inflater.inflate(R.layout.text_view, viewGroup, false)
        val textView = textLayout.findViewById<TextView>(R.id.text_view)
        textView.text = getString(stringID)
        viewGroup.addView(textView)
    }

    private fun addImage(viewGroup: ViewGroup, imageData: Pair<Int, Int>) {
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val imageLayout = inflater.inflate(R.layout.image_view, viewGroup, false)
        val imageView = imageLayout.findViewById<ImageView>(R.id.image_view)
        imageView.setContentDescription(getString(imageData.first))
        imageView.setImageResource(imageData.second)
        viewGroup.addView(imageView)
    }
}
