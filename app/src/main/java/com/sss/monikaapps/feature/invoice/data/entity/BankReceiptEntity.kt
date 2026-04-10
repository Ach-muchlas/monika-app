package com.sss.monikaapps.feature.invoice.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sss.monikaapps.common.helper.GenerateRandomTextHelper.generateRandomId

@Entity(tableName = "bank_receipt_table")
data class BankReceiptEntity(
    @PrimaryKey val id: String = generateRandomId(),

    val idCoa: String,
    val bankName: String,
    val codeBank : String,
)
