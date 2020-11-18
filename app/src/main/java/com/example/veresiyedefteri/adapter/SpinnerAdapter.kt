package com.example.veresiyedefteri.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.veresiyedefteri.R

class SpinnerAdapter(
    val context: Context,
    private val list: MutableList<String>? = mutableListOf()
) : BaseAdapter() {
    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_spinner_layout, p2, false)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        tvTitle.text = list?.get(p0).toString()
        return view
    }

    override fun getItem(p0: Int): Any {
        return p0.toString()
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list?.size!!
    }
}