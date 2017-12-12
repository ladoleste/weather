package com.thevacationplanner.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.thevacationplanner.R
import com.thevacationplanner.dto.Weather

/**
 *Created by Anderson on 09/12/2017.
 */
class WeatherAdapter(private val list: List<Weather>, private val selectedItems: MutableList<Weather>) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val checkbox = holder.itemView.findViewById<CheckBox>(R.id.checkbox)

        checkbox.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                if (!selectedItems.contains(item)) selectedItems.add(item)
            } else selectedItems.remove(item)
        })

        checkbox.text = item.name
        checkbox.isChecked = selectedItems.contains(item)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}