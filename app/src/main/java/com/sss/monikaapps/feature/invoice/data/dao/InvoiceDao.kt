package com.sss.monikaapps.feature.invoice.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import com.sss.monikaapps.feature.invoice.data.entity.InvoiceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomerInvoiceBatch(data: List<CustomerInvoiceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotaInvoiceBatch(data: List<InvoiceEntity>)

    @Query("SELECT COUNT(*) FROM customer_invoice_table")
    suspend fun countCustomerInvoice(): Int

    @Query("SELECT COUNT(*) FROM invoice_table")
    suspend fun countNotaInvoice(): Int

    @Query("SELECT COUNT(*) FROM customer_invoice_table")
    fun observeInvoiceCount(): Flow<Int>

    @Query("SELECT * FROM customer_invoice_table ORDER BY syncStatus ASC, customerName ASC")
    fun observeCustomerInvoice(): Flow<List<CustomerInvoiceEntity>>

    @Query("""
        SELECT * FROM customer_invoice_table 
        WHERE customerName LIKE '%' || :query || '%' OR customerId LIKE '%' || :query || '%'
        ORDER BY syncStatus ASC, customerName ASC
    """)
    fun searchCustomerInvoice(query: String) : Flow<List<CustomerInvoiceEntity>>

    @Query("DELETE FROM customer_invoice_table")
    suspend fun clearCustomerInvoice()

    @Query("DELETE FROM invoice_table")
    suspend fun clearNotaInvoice()

}