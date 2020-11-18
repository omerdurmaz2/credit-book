package com.example.veresiyedefteri.model

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class SellsResponse {
    var client: String? = null
    var product: String? = null
    var price: Double? = null
    var piece: Int? = null
    var date: String? = null
}
