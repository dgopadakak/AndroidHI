package com.example.androidhi.firm

data class Pharmacy(
    val nameOfPharmacy: String,
    val address: String,
    val num: Int,
    val timeOpen: String,
    val timeClose: String,
    val parkingSpaces: Int,
    val isDelivery: Int,
    val comment: String
)
