package com.sss.monikaapps.feature.invoice.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sss.monikaapps.common.helper.GenerateRandomTextHelper.generateRandomId

@Entity(tableName = "invoice_table")
data class InvoiceEntity(
    @PrimaryKey val id: String = generateRandomId(),

    val customerId: String,
    val nomorNota: String,
    val dateNota: String,
    val dueDate: String,
    val outstandingNota: String,
    val nominalNota: String,
    val moneyPaid: Long,

    //  0 : Belum transaksi, 1 : Bayar, 2 : Tidak Bayar
    val status: Int = 0,
    val reasonId: Int = 0,
    val reason: String? = null,

    val entryTime: String? = null,

    val gpsLatUser: String? = null,
    val gpsLngUser: String? = null,
    val distanceDifference: String? = null,

    // 1 Cash, 2 transfer
    val paymentMethod: Int? = null,
    // id dari bank entity
    val idCoaBankReceipt : String? = null,

    val dateReceipt: String? = null,
    //  0 : Belum transaksi, 1 : Masih dilokal, 2 : Sudah sinkron
    val syncStatus: Int = 0,
)