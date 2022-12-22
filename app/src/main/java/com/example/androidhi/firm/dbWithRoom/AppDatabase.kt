package com.example.androidhi.firm.dbWithRoom

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidhi.firm.PharmacyChainOperator

@Database(entities = [ PharmacyChainOperator::class ], version=4, exportSchema = false)
abstract class AppDatabase: RoomDatabase()
{
    public abstract fun groupOperatorDao(): PharmacyChainOperatorDao
}