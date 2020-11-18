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
import com.google.firebase.database.DataSnapshot


class ClientAdapter(
    val context: Context?,
    private val clientList: MutableList<DataSnapshot>?,
    val clickListener: (Int?) -> Unit
) :
    RecyclerView.Adapter<ClientAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(
            client: String?,
        ) {
            val tvClient = itemView.findViewById<TextView>(R.id.tvItemClientClientName)
            tvClient.text = client

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_client, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return clientList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.bind(
            clientList?.get(position)?.getValue(ClientResponse::class.java)?.name
        )

        holder.itemView.setOnClickListener {
            clickListener(position)
        }

    }

}
