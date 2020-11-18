package com.example.veresiyedefteri.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.veresiyedefteri.R
import com.example.veresiyedefteri.model.ClientResponse
import com.example.veresiyedefteri.model.SellsResponse
import com.google.firebase.database.DataSnapshot


class SellsAdapter(
    val context: Context?,
    private val sellList: MutableList<DataSnapshot>?,
    val clickListener: (Int?) -> Unit
) :
    RecyclerView.Adapter<SellsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(
            resp: SellsResponse?,
        ) {
            val tvClientName = itemView.findViewById<TextView>(R.id.tvSellsClientName)
            val tvProductName = itemView.findViewById<TextView>(R.id.tvSellsProductName)
            val tvProductPiece = itemView.findViewById<TextView>(R.id.tvSellsProductPiece)
            val tvTotalPrice = itemView.findViewById<TextView>(R.id.tvSellsProductTotalPrice)
            val tvDate = itemView.findViewById<TextView>(R.id.tvSellsDate)

            tvClientName.text = resp?.client
            tvProductName.text = resp?.product
            tvProductPiece.text = resp?.piece.toString()
            tvTotalPrice.text = resp?.price.toString() + "₺"
            tvDate.text = resp?.date
            tvDate.tooltipText = resp?.date

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_sell, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return sellList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.bind(
            sellList?.get(position)?.getValue(SellsResponse::class.java)
        )

        holder.itemView.setOnClickListener {
            clickListener(position)
        }

    }

}
