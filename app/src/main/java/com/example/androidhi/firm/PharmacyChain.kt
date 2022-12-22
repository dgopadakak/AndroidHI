package com.example.androidhi.firm

data class PharmacyChain(
    val name: String,
    var listOfPharmacies: ArrayList<Pharmacy> = ArrayList()
)
