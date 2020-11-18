package com.example.veresiyedefteri

import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.example.veresiyedefteri.adapter.SpinnerAdapter
import com.example.veresiyedefteri.model.ClientResponse
import com.example.veresiyedefteri.ui.main.*
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        sectionsPagerAdapter.addFragment(ProductsFragment.newInstance(), "ÜRÜN")
        sectionsPagerAdapter.addFragment(ClientsFragment.newInstance(), "MÜŞTERİ")
        sectionsPagerAdapter.addFragment(SellFragment.newInstance(), "SATIŞ")
        sectionsPagerAdapter.addFragment(StaticsFragment.newInstance(), "TOPLAM")
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val tabs: TabLayout = findViewById(R.id.tabs)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
        viewPager.currentItem = 2

    }






    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
    }
}