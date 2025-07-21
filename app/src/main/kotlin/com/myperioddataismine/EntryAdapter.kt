package com.myperioddataismine

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView

class EntryAdapter private constructor(
    context: Context,
    private val entryLayoutId: Int,
    arrayList: ArrayList<Entry>,
    private val entryType: EntryType
) : ArrayAdapter<EntryAdapter.Entry>(context, entryLayoutId, arrayList) {

    class Entry(val textId: Int, val imageId: Int)
    enum class EntryType {
        SingleEntry,
        MultipleEntry
    }

    var value = 0
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    fun updateValue(position: Int) {
        when (entryType) {
            EntryType.SingleEntry -> {
                value = position
            }
            EntryType.MultipleEntry -> {
                val mask = 1 shl position
                value = value xor mask
            }
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val entryView: View = convertView ?: run {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val entryViewGroup = inflater.inflate(entryLayoutId, parent, false) as ViewGroup
            entryViewGroup.tag = -1
            entryViewGroup
        }
        if (entryView.tag != position) {
            createEntryView(entryView as ViewGroup, position)
        }
        updateEntryView(entryView as ViewGroup, position)
        return entryView
    }

    private fun createEntryView(entryViewGroup: ViewGroup, position: Int) {
        val entry = getItem(position) as Entry
        val entryImage = entryViewGroup.findViewById<ImageView>(R.id.entry_image)
        val entryText = entryViewGroup.findViewById<TextView>(R.id.entry_text)
        val description = context.getString(entry.textId)

        entryViewGroup.tag = position
        entryImage.setImageResource(entry.imageId)
        entryImage.setContentDescription(description)
        entryText.text = description
    }

    private fun updateEntryView(entryViewGroup: ViewGroup, position: Int) {
        when (entryType) {
            EntryType.SingleEntry -> {
                val entryRadioButton = entryViewGroup.findViewById<RadioButton>(R.id.entry_radio_button)
                entryRadioButton.isChecked = (position == value)
            }
            EntryType.MultipleEntry -> {
                val entryCheckBox = entryViewGroup.findViewById<CheckBox>(R.id.entry_check_box)
                val mask = 1 shl position
                entryCheckBox.isChecked = (value and mask) != 0
            }
        }
    }

    companion object {
        operator fun invoke(
            context: Context,
            entries: Array<Pair<Int, Int>>,
            entryType: EntryType
        ): EntryAdapter {
            val arrayList = ArrayList<Entry>()
            for (entry in entries) {
                arrayList.add(Entry(entry.first, entry.second))
            }
            val entryLayoutId = when (entryType) {
                EntryType.SingleEntry -> R.layout.list_single_entry
                EntryType.MultipleEntry -> R.layout.list_multiple_entry
            }
            return EntryAdapter(context, entryLayoutId, arrayList, entryType)
        }
    }
}
