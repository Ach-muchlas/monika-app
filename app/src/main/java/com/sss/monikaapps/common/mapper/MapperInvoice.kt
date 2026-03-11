package com.sss.monikaapps.common.mapper

import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import com.sss.monikaapps.feature.invoice.data.entity.InvoiceEntity
import com.sss.monikaapps.feature.invoice.data.entity.ReasonEntity
import com.sss.monikaapps.feature.invoice.data.response.DataItemCustomerInvoice
import com.sss.monikaapps.feature.invoice.data.response.DataItemNotaInvoice
import com.sss.monikaapps.feature.invoice.data.response.DataItemReason

object MapperInvoice {
    fun DataItemCustomerInvoice.toEntity(): CustomerInvoiceEntity {
        return CustomerInvoiceEntity(
            customerId = customerId ?: "",
            customerName = customerName ?: "",
            address = address ?: "-",
            phones = phones ?: "-",
            gpsLatCustomer = gpsLatitude ?: "0",
            gpsLngCustomer = gpsLongitude ?: "0",
        )
    }


    fun DataItemNotaInvoice.toEntity(): InvoiceEntity {
        return InvoiceEntity(
            customerId = customerId ?: "",
            nomorNota = nomorNota ?: "",
            dateNota = dateNota ?: "",
            outstandingNota = outstandingNota
                ?.toDoubleOrNull()
                ?.toLong()
                ?.toString() ?: "0",
            dueDate = dueDate ?: "",
            moneyPaid = 0,
        )
    }

    fun DataItemReason.toEntity(): ReasonEntity {
        return ReasonEntity(
            idReason = idreason ?: "",
            descReason = descreason ?: "",
            status = status ?: ""
        )
    }

}