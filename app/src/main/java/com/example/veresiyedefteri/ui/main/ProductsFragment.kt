package com.example.veresiyedefteri.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.veresiyedefteri.MainActivity
import com.example.veresiyedefteri.R
import com.example.veresiyedefteri.adapter.ClientAdapter
import com.example.veresiyedefteri.adapter.ProductAdapter
import com.example.veresiyedefteri.dialog.InfoDialog
import com.example.veresiyedefteri.model.ClientRequest
import com.example.veresiyedefteri.model.ClientResponse
import com.example.veresiyedefteri.model.ProductRequest
import com.example.veresiyedefteri.model.ProductResponse
import com.google.firebase.database.*
import java.lang.Exception

/**
 * A placeholder fragment containing a simple view.
 */
class ProductsFragment : Fragment(), View.OnClickListener {

    private lateinit var productName: EditText
    private lateinit var productPrice: EditText
    private lateinit var btnSave: Button
    private lateinit var productAdapter: ProductAdapter
    private lateinit var rvProducts: RecyclerView
    private lateinit var products: MutableList<DataSnapshot>
    private lateinit var fbDatabase: DatabaseReference
    private lateinit var infoDialog: InfoDialog
    private var userKey = ""

    companion object {
        fun newInstance() = ProductsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_products, container, false)
        fbDatabase = FirebaseDatabase.getInstance().reference.child("products")

        initViews(root)
        initClickListeners()
        productAddListener()

        return root
    }

    private fun initViews(root: View) {
        productName = root.findViewById(R.id.etProductName)
        productPrice = root.findViewById(R.id.etProductPrice)
        btnSave = root.findViewById(R.id.btnAddProduct)
        rvProducts = root.findViewById(R.id.rvProducts)
        productAdapter = ProductAdapter(activity?.applicationContext, mutableListOf()) {

        }
    }

    private fun initClickListeners() {
        btnSave.setOnClickListener(this)
    }

    private fun productAddListener() {
        fbDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                setAdapterItems(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun setAdapterItems(dataSnapshot: DataSnapshot) {

        try {
            products = mutableListOf()
            for (item in dataSnapshot.children) {
                products.add(item)
            }
            productAdapter = ProductAdapter(activity?.applicationContext, products) { pos ->
                infoDialog = InfoDialog {
                    when (it) {
                        "edit" -> {
                            userKey = products[pos!!].key.toString()
                            btnSave.text = "Düzenle"
                            productName.setText(products[pos].getValue(ProductResponse::class.java)?.name)
                            productPrice.setText(products[pos].getValue(ProductResponse::class.java)?.price.toString())
                        }
                        "delete" -> {
                            deleteProduct(pos)
                        }
                    }
                }
                activity?.supportFragmentManager?.let { infoDialog.show(it, "InfoDialog") }
            }

            val linearLayoutManager =
                LinearLayoutManager(activity?.applicationContext)
            rvProducts.layoutManager = linearLayoutManager
            rvProducts.adapter = productAdapter
        } catch (e: Exception) {
            Log.e("sss", "hata: ${e.localizedMessage}")
        }


    }

    private fun deleteProduct(position: Int?) {
        position?.let { pos -> products[pos].key?.let { fbDatabase.child(it).removeValue() } }
    }

    private fun addProduct(name: String, price: Double) {

        fbDatabase.push().key?.let {
            val client = ProductRequest(name, price)
            val task = fbDatabase.child(it).setValue(client)
            task.addOnSuccessListener {
                (activity as MainActivity).showToast("Ürün Eklendi")
            }.addOnFailureListener {
                (activity as MainActivity).showToast("Hata! Lütfen daha sonra tekrar dene")
                Log.e("sss", it.localizedMessage)
            }
        }


    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnAddProduct -> {
                try {
                    if (productName.text.isNotEmpty() && productPrice.text.isNotEmpty()) {
                        if (btnSave.text != "Ekle") {
                            updateProduct(
                                productName.text.toString(),
                                productPrice.text.toString().toDouble()
                            )
                        } else {
                            addProduct(
                                productName.text.toString(),
                                productPrice.text.toString().toDouble()
                            )
                        }
                        btnSave.text = "Ekle"
                        productName.text = null
                        productPrice.text = null
                    } else {
                        (activity as MainActivity).showToast("Lütfen Ürün Adını Girin")
                    }
                } catch (e: Exception) {
                    Log.e("sss", "hata: ${e.localizedMessage}")
                }

            }
        }
    }

    private fun updateProduct(name: String, price: Double) {
        val map = HashMap<String, Any>().apply {
            put("name", name)
            put("price", price)
        }
        fbDatabase.child(userKey).updateChildren(map).addOnSuccessListener {
            (activity as MainActivity).showToast("Başarılı")
        }.addOnFailureListener {
            (activity as MainActivity).showToast("Başarısız")
            Log.e("sss", "hata: ${it.localizedMessage}")
        }

    }
}