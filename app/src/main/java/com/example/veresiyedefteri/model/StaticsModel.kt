package com.example.veresiyedefteri.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class StaticsModel(
    var name: String,
    var totalPrice: Double
)
