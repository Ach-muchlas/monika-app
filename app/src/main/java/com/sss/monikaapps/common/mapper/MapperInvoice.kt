package com.sss.monikaapps.common.mapper

import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import com.sss.monikaapps.feature.invoice.data.entity.InvoiceEntity
import com.sss.monikaapps.feature.invoice.data.response.DataItemCustomerInvoice
import com.sss.monikaapps.feature.invoice.data.response.DataItemNotaInvoice

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


    fun DataItemNotaInvoice.toEntity() : InvoiceEntity{
        return InvoiceEntity(
            customerId = customerId ?:"",
            nomorNota = nomorNota ?: "",
            dateNota = dateNota ?: "",
            outstandingNota = outstandingNota ?: "",
            dueDate = dueDate ?: "",
            moneyPaid = 0,
        )
    }
}