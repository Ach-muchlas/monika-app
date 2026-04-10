package com.sss.monikaapps.feature.update_data_invoice.data.model

import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import com.sss.monikaapps.feature.invoice.data.entity.InvoiceEntity

data class UpdateDataInvoiceResponse(
    val listCustomerInvoice: List<CustomerInvoiceEntity>,
    val listNotaInvoice: List<InvoiceEntity>,
)