package com.example.veresiyedefteri.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.veresiyedefteri.MainActivity
import com.example.veresiyedefteri.R
import com.example.veresiyedefteri.adapter.ClientAdapter
import com.example.veresiyedefteri.dialog.InfoDialog
import com.example.veresiyedefteri.model.ClientRequest
import com.example.veresiyedefteri.model.ClientResponse
import com.example.veresiyedefteri.model.ProductResponse
import com.google.firebase.database.*
import java.lang.Exception

/**
 * A placeholder fragment containing a simple view.
 */
class ClientsFragment : Fragment(), View.OnClickListener {
    private lateinit var clientName: EditText
    private lateinit var btnSave: Button
    private lateinit var clientAdapter: ClientAdapter
    private lateinit var rvClients: RecyclerView
    private lateinit var clients: MutableList<DataSnapshot>
    private lateinit var fbDatabase: DatabaseReference
    private lateinit var infoDialog: InfoDialog
    private var userKey = ""


    companion object {
        fun newInstance() = ClientsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_clients, container, false)
        fbDatabase = FirebaseDatabase.getInstance().reference.child("clients")

        initViews(root)
        initClickListeners()
        clientAddListener()

        return root
    }

    private fun initViews(root: View) {
        clientName = root.findViewById(R.id.etClientName)
        btnSave = root.findViewById(R.id.btnAddClient)
        rvClients = root.findViewById(R.id.rvClients)
        clientAdapter = ClientAdapter(activity?.applicationContext, mutableListOf()) {

        }
    }

    private fun initClickListeners() {
        btnSave.setOnClickListener(this)
    }

    private fun clientAddListener() {
        fbDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                setAdapterItems(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("sss", "Hata bir sorun oluştu ${error.details}")
            }
        })
    }

    private fun setAdapterItems(dataSnapshot: DataSnapshot) {

        try {
            clients = mutableListOf()
            for (item in dataSnapshot.children) {
                clients.add(item)
            }
            clientAdapter = ClientAdapter(activity?.applicationContext, clients) { pos ->

                infoDialog = InfoDialog {
                    when (it) {
                        "edit" -> {
                            userKey = clients[pos!!].key.toString()
                            btnSave.text = "Düzenle"
                            clientName.setText(clients[pos].getValue(ClientResponse::class.java)?.name)
                        }
                        "delete" -> {
                            deleteUser(pos)
                        }
                    }
                }
                activity?.supportFragmentManager?.let { infoDialog.show(it, "InfoDialog") }
            }
            val linearLayoutManager =
                LinearLayoutManager(activity?.applicationContext)
            rvClients.layoutManager = linearLayoutManager
            rvClients.adapter = clientAdapter
        } catch (e: Exception) {
            Log.e("eee", "Hata: ${e.localizedMessage}")
        }


    }

    private fun deleteUser(position: Int?) {
        position?.let { pos -> clients[pos].key?.let { fbDatabase.child(it).removeValue() } }
    }

    private fun addUser(name: String) {

        fbDatabase.push().key?.let {
            val client = ClientRequest(name)
            val task = fbDatabase.child(it).setValue(client)
            task.addOnSuccessListener {
                (activity as MainActivity).showToast("Kişi Eklendi")
            }.addOnFailureListener {
                (activity as MainActivity).showToast("Hata! Lütfen daha sonra tekrar dene")
                Log.e("sss", it.localizedMessage)
            }
        }


    }

    private fun updateUser(name: String) {
        val map = HashMap<String, Any>().apply {
            put("name", name)
        }
        fbDatabase.child(userKey).updateChildren(map).addOnSuccessListener {
            (activity as MainActivity).showToast("Başarılı")
        }.addOnFailureListener {
            (activity as MainActivity).showToast("Başarısız")

        }

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnAddClient -> {
                try {
                    if (clientName.text.isNotEmpty()) {
                        if (btnSave.text != "Ekle") {
                            updateUser(clientName.text.toString())
                        } else {
                            addUser(clientName.text.toString())
                        }
                        btnSave.text = "Ekle"
                        clientName.text = null
                    } else {
                        (activity as MainActivity).showToast("Lütfen Müşteri Adını Girin")
                    }

                } catch (e: Exception) {
                    Log.e("sss", "Hata:${e.localizedMessage}")
                }
            }
        }
    }
}