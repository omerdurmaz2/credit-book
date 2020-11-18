package com.example.veresiyedefteri.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.veresiyedefteri.MainActivity
import com.example.veresiyedefteri.R
import com.example.veresiyedefteri.adapter.SellsAdapter
import com.example.veresiyedefteri.adapter.SpinnerAdapter
import com.example.veresiyedefteri.dialog.InfoDialog
import com.example.veresiyedefteri.model.ClientResponse
import com.example.veresiyedefteri.model.ProductResponse
import com.example.veresiyedefteri.model.SellsRequest
import com.example.veresiyedefteri.model.SellsResponse
import com.google.firebase.database.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * A placeholder fragment containing a simple view.
 */
class SellFragment : Fragment(), View.OnClickListener {

    private lateinit var spProducts: Spinner
    private lateinit var spClients: Spinner
    private lateinit var etPNumber: EditText
    private lateinit var btnSave: Button
    private lateinit var btnPlus: Button
    private lateinit var btnMinus: Button
    private lateinit var sellAdapter: SellsAdapter
    private lateinit var rvSells: RecyclerView
    private lateinit var clients: MutableList<DataSnapshot>
    private lateinit var products: MutableList<DataSnapshot>
    private lateinit var sells: MutableList<DataSnapshot>
    private lateinit var fbClientTable: DatabaseReference
    private lateinit var fbProductTable: DatabaseReference
    private lateinit var fbSellTable: DatabaseReference
    private lateinit var infoDialog: InfoDialog
    private var sellKey = ""
    lateinit var productNameList: MutableList<String>
    lateinit var clientNameList: MutableList<String>
    private var count = 1

    companion object {
        fun newInstance() = SellFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sell, container, false)


        initDatabases()
        initViews(root)
        initClickListeners()


        getClients()
        getProducts()
        sellAddListener()

        return root
    }

    private fun initDatabases() {
        fbClientTable = FirebaseDatabase.getInstance().reference.child("clients")
        fbProductTable = FirebaseDatabase.getInstance().reference.child("products")
        fbSellTable = FirebaseDatabase.getInstance().reference.child("sells")
    }

    private fun initViews(root: View) {
        spProducts = root.findViewById(R.id.spProducts)
        spClients = root.findViewById(R.id.spClients)
        btnSave = root.findViewById(R.id.btnAddSell)
        rvSells = root.findViewById(R.id.rvSells)
        etPNumber = root.findViewById(R.id.etProductNumber)
        btnPlus = root.findViewById(R.id.btnPlusValue)
        btnMinus = root.findViewById(R.id.btnMinusValue)
        sellAdapter = SellsAdapter(activity?.applicationContext, mutableListOf()) {

        }
    }

    private fun initClickListeners() {
        btnSave.setOnClickListener(this)
        btnPlus.setOnClickListener(this)
        btnMinus.setOnClickListener(this)
    }

    private fun getClients() {
        clients = mutableListOf()
        clientNameList = mutableListOf<String>()
        fbClientTable.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                clientNameList.clear()
                for (item in snapshot.children) {
                    clients.add(item)
                    item.getValue(ClientResponse::class.java)?.name?.let { clientNameList.add(it) }
                }
                val spinnerAdapter =
                    activity?.baseContext?.let { SpinnerAdapter(it, clientNameList) }
                spClients.adapter = spinnerAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("sss", "hata: ${error.message}")
            }
        })

    }

    private fun getProducts() {
        products = mutableListOf()
        productNameList = mutableListOf<String>()
        fbProductTable.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productNameList.clear()
                for (item in snapshot.children) {
                    products.add(item)
                    item.getValue(ProductResponse::class.java)?.name?.let { productNameList.add(it) }
                }
                val spinnerAdapter = context?.let { SpinnerAdapter(it, productNameList) }
                spProducts.adapter = spinnerAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("sss", "hata: ${error.message}")

            }
        })


    }

    private fun sellAddListener() {
        fbSellTable.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                setAdapterItems(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("sss", "hata: ${error.message}")

            }
        })
    }

    private fun setAdapterItems(dataSnapshot: DataSnapshot) {
        try {
            sells = mutableListOf()

            for (item in dataSnapshot.children) {
                sells.add(item)
            }
            sells.reverse()
            sellAdapter = SellsAdapter(activity?.applicationContext, sells) { pos ->
                infoDialog = InfoDialog { dialogResult ->
                    when (dialogResult) {
                        "edit" -> {
                            sellKey = sells[pos!!].key.toString()
                            btnSave.text = "Düzenle"
                            spProducts.setSelection(
                                productNameList.indexOf(
                                    sells[pos].getValue(
                                        SellsResponse::class.java
                                    )?.product
                                )
                            )
                            spClients.setSelection(
                                clientNameList.indexOf(
                                    sells[pos].getValue(
                                        SellsResponse::class.java
                                    )?.client
                                )
                            )
                            etPNumber.setText(
                                sells[pos].getValue(
                                    SellsResponse::class.java
                                )?.piece!!
                            )


                        }
                        "delete" -> {
                            deleteSell(pos)
                        }
                    }
                }
                activity?.supportFragmentManager?.let { infoDialog.show(it, "InfoDialog") }
            }
            val linearLayoutManager =
                LinearLayoutManager(activity?.applicationContext)
            rvSells.layoutManager = linearLayoutManager
            rvSells.adapter = sellAdapter
        } catch (e: Exception) {
            Log.e("sss", "hata: ${e.localizedMessage}")
        }


    }

    private fun deleteSell(position: Int?) {
        position?.let { pos -> sells[pos].key?.let { fbSellTable.child(it).removeValue() } }
    }


    private fun addSell(client: Int, product: Int, piece: Int) {

        fbSellTable.push().key?.let {
            val sell =
                products[product].getValue(ProductResponse::class.java)?.price
                    ?.let { it1 ->
                        SellsRequest(
                            clients[client].getValue(ProductResponse::class.java)?.name.toString(),
                            products[product].getValue(ProductResponse::class.java)?.name.toString(),
                            piece = piece,
                            it1 * piece,
                            getDate()
                        )
                    }
            val task = fbSellTable.child(it).setValue(sell)
            task.addOnSuccessListener {
                (activity as MainActivity).showToast("Ürün Eklendi")
            }.addOnFailureListener {
                (activity as MainActivity).showToast("Hata! Lütfen daha sonra tekrar dene")
                Log.e("sss", it.localizedMessage)
            }
        }
    }

    private fun updateSell(client: Int, product: Int, piece: Int) {

        val sell =
            products[product].getValue(ProductResponse::class.java)?.price
                ?.let { it1 ->
                    SellsRequest(
                        clients[client].getValue(ProductResponse::class.java)?.name.toString(),
                        products[product].getValue(ProductResponse::class.java)?.name.toString(),
                        piece = piece,
                        it1 * piece,
                        getDate()
                    )
                }
        val map = HashMap<String, Any>().apply {
            sell?.client?.let { put("client", it) }
            sell?.product?.let { put("product", it) }
            sell?.piece?.let { put("piece", it) }
            sell?.price?.let { put("price", it) }
            sell?.date?.let { put("date", it) }
        }
        fbSellTable.child(sellKey).updateChildren(map).addOnSuccessListener {
            (activity as MainActivity).showToast("Başarılı")
        }.addOnFailureListener {
            (activity as MainActivity).showToast("Başarısız")
            Log.e("sss", it.localizedMessage)
        }

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnAddSell -> {

                try {
                    if (spClients.selectedItemPosition != -1 && spProducts.selectedItemPosition != -1) {

                        if (btnSave.text != "Ekle") {
                            updateSell(
                                spClients.selectedItemPosition,
                                spProducts.selectedItemPosition,
                                etPNumber.text.toString().toInt(),
                            )
                        } else {
                            addSell(
                                spClients.selectedItemPosition,
                                spProducts.selectedItemPosition,
                                etPNumber.text.toString().toInt(),
                            )
                        }
                        sellKey = ""
                        btnSave.text = "Ekle"
                        count = 1
                        etPNumber.setText(count.toString())
                        spProducts.setSelection(0)
                        spClients.setSelection(0)
                    } else {
                        (activity as MainActivity).showToast("Lütfen Müşteri ve Ürün Seçin")
                    }


                } catch (e: Exception) {
                    Log.e("sss", "hata:${e.localizedMessage}")
                }

            }
            R.id.btnPlusValue -> {
                count++
                etPNumber.setText(count.toString())
            }
            R.id.btnMinusValue -> {
                if (count > 1) count--
                etPNumber.setText(count.toString())
            }
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun getDate(): String {
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        return simpleDateFormat.format(Date())
    }
}