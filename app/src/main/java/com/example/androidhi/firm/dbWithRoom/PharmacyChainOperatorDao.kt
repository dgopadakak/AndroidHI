package com.example.androidhi.firm.dbWithRoom

import androidx.room.*
import com.example.androidhi.firm.PharmacyChainOperator

@Dao
interface PharmacyChainOperatorDao
{
    @Query("SELECT * FROM PharmacyChainOperator")
    fun getAll(): List<PharmacyChainOperator?>?

    @Query("SELECT * FROM PharmacyChainOperator WHERE id = :id")
    fun getById(id: Int): PharmacyChainOperator

    @Insert
    fun insert(go: PharmacyChainOperator?)

    @Delete
    fun delete(go: PharmacyChainOperator?)
}