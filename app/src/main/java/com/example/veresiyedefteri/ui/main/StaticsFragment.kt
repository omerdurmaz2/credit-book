package com.example.veresiyedefteri.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.veresiyedefteri.MainActivity
import com.example.veresiyedefteri.R
import com.example.veresiyedefteri.adapter.SellsAdapter
import com.example.veresiyedefteri.adapter.StaticsAdapter
import com.example.veresiyedefteri.dialog.ConfirmationDialog
import com.example.veresiyedefteri.dialog.TotalDebtDialog
import com.example.veresiyedefteri.model.ClientResponse
import com.example.veresiyedefteri.model.SellsResponse
import com.example.veresiyedefteri.model.StaticsModel
import com.google.firebase.database.*


class StaticsFragment : Fragment() {

    private lateinit var rvStatics: RecyclerView
    private lateinit var staticsAdapter: StaticsAdapter
    private lateinit var fbSellsTable: DatabaseReference
    private lateinit var fbClientsTable: DatabaseReference
    private lateinit var sells: MutableList<DataSnapshot>
    private lateinit var clients: MutableList<DataSnapshot>
    private lateinit var totalDebtDialog: TotalDebtDialog
    private lateinit var confirmationDialog: ConfirmationDialog


    companion object {
        fun newInstance() = StaticsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_statics, container, false)

        initViews(root)
        initDatabases()
        tableChangeListener()

        return root
    }

    private fun initViews(root: View) {
        rvStatics = root.findViewById(R.id.rvStatics)
    }

    private fun bindStatics() {
        try {

            val staticList: MutableList<StaticsModel> = mutableListOf()
            clients.forEach { client ->
                var totalPrice: Double = 0.toDouble()
                sells.forEach { sell ->
                    val name = client.getValue(ClientResponse::class.java)?.name
                    val row = sell.getValue(SellsResponse::class.java)
                    if (name == row?.client) totalPrice += row?.price!!
                }
                val staticsModel =
                    client.getValue(ClientResponse::class.java)?.name?.let {
                        StaticsModel(
                            it,
                            totalPrice
                        )
                    }
                staticsModel?.let { staticList.add(it) }
                totalPrice = 0.toDouble()
            }
            staticsAdapter = StaticsAdapter(activity?.applicationContext, staticList) { pos ->
                totalDebtDialog = TotalDebtDialog {
                    if (it == "yes") {
                        confirmationDialog = ConfirmationDialog { confirmation ->
                            if (confirmation == "yes") {
                                deleteTotalDebt(staticList[pos!!].name)
                            }
                        }
                        activity?.supportFragmentManager?.let { it1 ->
                            confirmationDialog.show(
                                it1,
                                "ConfirmationDialog"
                            )
                        }
                    }
                }
                activity?.supportFragmentManager?.let { it1 ->
                    totalDebtDialog.show(
                        it1,
                        "TotalDebtDialog"
                    )
                }
            }
            val linearLayoutManager =
                LinearLayoutManager(activity?.applicationContext)
            rvStatics.layoutManager = linearLayoutManager
            rvStatics.adapter = staticsAdapter
        } catch (e: Exception) {
            Log.e("sss", e.localizedMessage)
        }

    }

    private fun deleteTotalDebt(clientName: String) {
        sells.forEach {
            if (it.getValue(SellsResponse::class.java)?.client == clientName) {
                it.key?.let { it1 -> fbSellsTable.child(it1).removeValue() }
            }
        }
    }

    private fun initDatabases() {
        fbSellsTable = FirebaseDatabase.getInstance().reference.child("sells")
        fbClientsTable = FirebaseDatabase.getInstance().reference.child("clients")
    }

    private fun tableChangeListener() {
        sells = mutableListOf()
        fbSellsTable.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sells.clear()
                for (item in snapshot.children) {
                    sells.add(item)
                }
                getClients()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("sss", error.details)
            }
        })

    }

    private fun getClients() {
        clients = mutableListOf()
        fbClientsTable.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                clients.clear()
                for (item in snapshot.children) {
                    clients.add(item)
                }
                bindStatics()

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("sss", error.details)
            }
        })
    }



}