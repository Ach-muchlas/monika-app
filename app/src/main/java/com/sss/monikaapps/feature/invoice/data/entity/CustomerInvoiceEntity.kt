package com.sss.monikaapps.feature.invoice.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sss.monikaapps.common.helper.GenerateRandomTextHelper.generateRandomId
import java.io.File

@Entity(tableName = "customer_invoice_table")
data class CustomerInvoiceEntity(
    @PrimaryKey val id: String = generateRandomId(),

    val customerId: String,
    val customerName: String,
    val address: String,
    val phones: String,
    val gpsLatCustomer: String,
    val gpsLngCustomer: String,

    val syncStatus: Int = 0
)