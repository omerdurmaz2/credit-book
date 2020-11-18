package com.example.veresiyedefteri.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class SellsRequest(
    var client: String,
    var product: String,
    var piece: Int,
    var price: Double,
    var date: String
)
