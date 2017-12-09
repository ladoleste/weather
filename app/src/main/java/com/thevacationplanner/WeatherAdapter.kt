package com.thevacationplanner

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.thevacationplanner.data.Weather

/**
 *Created by Anderson on 09/12/2017.
 */
class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    private lateinit var list: List<Weather>

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val checkbox = holder.itemView.findViewById<CheckBox>(R.id.checkbox)

        checkbox.setOnCheckedChangeListener({ _, isChecked -> item.selected = isChecked })

        checkbox.text = item.name
        checkbox.isChecked = item.selected
    }

    fun setItems(newItems: List<Weather>) {
        list = newItems
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun getSelectedItems(): ArrayList<Weather> {
        return ArrayList(list.filter { it.selected })
    }
}