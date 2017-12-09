package com.thevacationplanner

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *Created by Anderson on 09/12/2017.
 */
class SpAdapter : RecyclerView.Adapter<SpAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpAdapter.ViewHolder {
        return SpAdapter.ViewHolder(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_spinner_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

    }


    override fun getItemCount(): Int {
        return 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}