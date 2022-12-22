package com.example.androidhi.firm.dbWithRoom

import androidx.room.TypeConverter
import com.example.androidhi.firm.PharmacyChain
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class PharmacyChainOperatorConverter
{
    @TypeConverter
    fun fromGO(pharmacyChains: ArrayList<PharmacyChain>): String
    {
        val gsonBuilder = GsonBuilder()
        val gson: Gson = gsonBuilder.create()
        return gson.toJson(pharmacyChains)
    }

    @TypeConverter
    fun toGO(data: String): ArrayList<PharmacyChain>
    {
        val gsonBuilder = GsonBuilder()
        val gson: Gson = gsonBuilder.create()
        return try {
            val type: Type = object : TypeToken<ArrayList<PharmacyChain>>() {}.type
            gson.fromJson(data, type)
        } catch (e: Exception) {
            ArrayList()
        }
    }
}