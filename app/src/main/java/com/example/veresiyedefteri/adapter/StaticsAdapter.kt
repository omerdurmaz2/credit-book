package com.example.veresiyedefteri.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.veresiyedefteri.R
import com.example.veresiyedefteri.model.ClientResponse
import com.example.veresiyedefteri.model.StaticsModel
import com.google.firebase.database.DataSnapshot


class StaticsAdapter(
    val context: Context?,
    private val staticList: MutableList<StaticsModel>?,
    val clickListener: (Int?) -> Unit
) :
    RecyclerView.Adapter<StaticsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(
            static: StaticsModel?,
        ) {
            val tvClientName = itemView.findViewById<TextView>(R.id.tvStaticsClientName)
            val tvTotalPrice = itemView.findViewById<TextView>(R.id.tvStaticsTotalDebpt)
            tvClientName.text = static?.name
            tvTotalPrice.text = static?.totalPrice.toString() + " ₺"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_static, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return staticList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.bind(
            staticList?.get(position)
        )

        holder.itemView.setOnClickListener {
            clickListener(position)
        }

    }

}
