package com.example.veresiyedefteri.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ProductRequest(
    var name: String,
    var price: Double
)
