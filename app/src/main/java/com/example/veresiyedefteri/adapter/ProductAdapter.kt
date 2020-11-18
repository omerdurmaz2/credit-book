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
import com.example.veresiyedefteri.model.ProductResponse
import com.google.firebase.database.DataSnapshot


class ProductAdapter(
    val context: Context?,
    private val productList: MutableList<DataSnapshot>?,
    val clickListener: (Int?) -> Unit
) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(
            product: ProductResponse?,
        ) {
            val tvName = itemView.findViewById<TextView>(R.id.tvItemdProductName)
            val tvPrice = itemView.findViewById<TextView>(R.id.tvItemProductPrice)
            tvName.text = product?.name
            tvPrice.text = product?.price.toString() + "₺"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_product, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return productList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            productList?.get(position)?.getValue(ProductResponse::class.java)
        )

        holder.itemView.setOnClickListener {
            clickListener(position)
        }

    }

}
