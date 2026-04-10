package com.sss.monikaapps.feature.invoice.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sss.monikaapps.feature.invoice.data.entity.BankReceiptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BankReceiptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBankReceipt(data: List<BankReceiptEntity>)

    @Query("SELECT COUNT(*) FROM bank_receipt_table")
    suspend fun countDataBankReceipt(): Int

    @Query("DELETE FROM bank_receipt_table")
    suspend fun deleteDataBankReceipt(): Int

    @Query("SELECT * FROM bank_receipt_table")
    fun observerBankReceipt(): Flow<List<BankReceiptEntity>>

}