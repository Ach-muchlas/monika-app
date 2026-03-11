package com.sss.monikaapps.feature.invoice.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sss.monikaapps.feature.activity.data.response.PhotoItem
import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import com.sss.monikaapps.feature.invoice.data.entity.InvoiceEntity
import com.sss.monikaapps.feature.invoice.data.entity.ReasonEntity
import com.sss.monikaapps.feature.invoice.domain.model.PaymentInvoiceData
import com.sss.monikaapps.feature.invoice.domain.model.PaymentInvoiceRequestDataLocal
import com.sss.monikaapps.feature.invoice.domain.model.PhotoPaymentInvoice
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

    @Query(
        """
        SELECT a.*, MIN(b.status) as syncStatus 
        FROM customer_invoice_table a
        LEFT JOIN invoice_table b ON a.customerId = b.customerId
        GROUP BY a.customerId
        ORDER BY syncStatus ASC, a.customerName ASC
    """
    )
    fun observeCustomerInvoice(): Flow<List<CustomerInvoiceEntity>>

    @Query(
        """
       SELECT a.*, MIN(b.status) as syncStatus 
        FROM customer_invoice_table a
        LEFT JOIN invoice_table b ON a.customerId = b.customerId
        WHERE a.customerName LIKE '%' || :query || '%' OR a.customerId LIKE '%' || :query || '%'
        GROUP BY a.customerId
        ORDER BY syncStatus ASC, a.customerName ASC
    """
    )
    fun searchCustomerInvoice(query: String): Flow<List<CustomerInvoiceEntity>>

    @Query("""
        SELECT a.*, 
            CASE WHEN MAX(b.syncStatus) = 1 THEN 3 ELSE MIN(b.status) END as syncStatus
        FROM customer_invoice_table a
        LEFT JOIN invoice_table b ON a.customerId = b.customerId
        WHERE (a.customerName LIKE '%' || :query || '%' OR a.customerId LIKE '%' || :query || '%')
          AND (
              :status = 7
              OR (:status = 3 AND b.syncStatus = 1)
              OR (:status != 3 AND b.status = :status)
          )
        GROUP BY a.customerId
        ORDER BY b.syncStatus ASC, a.customerName ASC
    """)
    fun observeFilteredInvoice(query: String, status: Int): Flow<List<CustomerInvoiceEntity>>
    @Query("SELECT * FROM customer_invoice_table WHERE customerId = :customerId")
    fun observeCustomerInvoiceByCustomerId(customerId: String): Flow<CustomerInvoiceEntity>

    @Query("SELECT * FROM invoice_table WHERE customerId = :customerId")
    fun observerNotaInvoiceByCustomerId(customerId: String): Flow<List<InvoiceEntity>>

    @Query("SELECT filePath as path, parentId as id, parentType as tipe FROM photo_table WHERE parentId = :idNotaInvoice")
    fun observerPhotoNota(idNotaInvoice: String): Flow<List<PhotoItem>>

    @Query("DELETE FROM customer_invoice_table")
    suspend fun clearCustomerInvoice()

    @Query("DELETE FROM invoice_table")
    suspend fun clearNotaInvoice()

    /*
        INVOICE ENTITY
     */
    @Query("UPDATE invoice_table SET syncStatus = 2 WHERE nomorNota = :nota AND customerId = :customerId")
    suspend fun markIsSync(nota: String, customerId: String)

    @Query("SELECT COUNT(*) FROM invoice_table WHERE syncStatus = 1")
    fun countDataNotSync(): Flow<Int>

    @Query("SELECT COUNT(*) FROM invoice_table WHERE status = 0")
    fun countDataPending(): Flow<Int>

    /*
        INVOICE (MIX)
     */
    @Query(
        """
        SELECT a.customerId, b.customerName, a.nomorNota, a.outstandingNota 
        FROM invoice_table a
        JOIN customer_invoice_table b on a.customerId = b.customerId
        WHERE nomorNota = :nota
    """
    )
    fun observerPaymentDataInvoice(nota: String): Flow<PaymentInvoiceData>

    @Query(
        """
        SELECT a.id as idNota,a.nomorNota, a.customerId, b.customerName, b.address as customerAddress, 
            b.phones as customerPhone, b.gpsLatCustomer as customerLat, b.gpsLngCustomer as customerLng,
            a.gpsLatUser as userLat, a.gpsLngUser as userLng, a.status as isStatus, a.entryTime, 
            a.outstandingNota, a.moneyPaid as payment,(SELECT createAd FROM config_download_data_table LIMIT 1) as dateDownload, 
            a.reasonId as idReason, a.reason as descReason, a.distanceDifference
        FROM invoice_table a 
        JOIN customer_invoice_table b on a.customerId = b.customerId
        WHERE a.nomorNota = :nota AND a.customerId = :customerId
    """
    )
    suspend fun observerPaymentRequest(
        nota: String,
        customerId: String,
    ): PaymentInvoiceRequestDataLocal


    @Query(
        """
        SELECT a.id as idNota,a.nomorNota, a.customerId, b.customerName, b.address as customerAddress, 
            b.phones as customerPhone, b.gpsLatCustomer as customerLat, b.gpsLngCustomer as customerLng,
            a.gpsLatUser as userLat, a.gpsLngUser as userLng, a.status as isStatus, a.entryTime, 
            a.outstandingNota, a.moneyPaid as payment, (SELECT createAd FROM config_download_data_table LIMIT 1) as dateDownload, 
            a.reasonId as idReason, a.reason as descReason, a.distanceDifference
        FROM invoice_table a 
        JOIN customer_invoice_table b on a.customerId = b.customerId
        WHERE a.status <> 0 AND a.syncStatus = 1
    """
    )
    suspend fun observerPaymentNotSync(): List<PaymentInvoiceRequestDataLocal>

    @Query(
        """
        UPDATE invoice_table 
        SET moneyPaid = :moneyPaid, status = :status, reasonId = :reasonId, 
            reason = :descReason, entryTime = :entryTime, gpsLatUser = :gpsLat, gpsLngUser = :gpsLng, distanceDifference = :distanceDifference ,syncStatus = 1
        WHERE nomorNota = :nota AND customerId = :customerId
    """
    )
    suspend fun paidInvoice(
        nota: String,
        customerId: String,
        moneyPaid: Long,
        status: Int,
        reasonId: Int,
        descReason: String,
        entryTime: String,
        gpsLat: String,
        gpsLng: String,
        distanceDifference : String
    ): Int

    /*
        REASON INVOICE
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReasonInvoiceBatch(data: List<ReasonEntity>)

    @Query("SELECT * FROM reason_table WHERE status = 3 ORDER BY descReason ASC")
    fun observeReasonInvoice(): Flow<List<ReasonEntity>>

    @Query("SELECT COUNT(*) FROM reason_table")
    suspend fun countReasonInvoice(): Int

    @Query("DELETE FROM reason_table")
    suspend fun clearReasonInvoice()


    /*
        PHOTO
     */
    @Query("Select filePath, createdAt FROM photo_table WHERE parentId = :idNota")
    suspend fun getPhotoPaymentInvoice(idNota: String): List<PhotoPaymentInvoice>
}