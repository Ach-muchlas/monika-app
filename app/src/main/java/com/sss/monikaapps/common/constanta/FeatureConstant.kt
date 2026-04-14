package com.sss.monikaapps.common.constanta

object HomeFeatureConstant {
    const val FEATURE_ACTIVITIES = 1
    const val FEATURE_VISIT = 2
    const val FEATURE_DOWNLOAD = 3
    const val FEATURE_EXPENSES = 4
    const val FEATURE_MASTER_DATA = 5
    const val FEATURE_SETTING = 6
    const val FEATURE_INVOICE = 7

    const val FEATURE_UPDATE_DATA = 8
}

object FeatureActivityConstant {
    const val CHECK_IN = "1"
    const val CHECK_OUT = "2"
}

object NameFeatureConstant {
    const val ACTIVITY = "activity"
    const val EXPENSE = "expense"
    const val VISIT = "visit"
    const val INVOICE = "invoice"
}

object TableNameConstant {
    const val VISIT_TABLE = "visit_table"
    const val CUSTOMER_INVOICE_TABLE = "customer_invoice_table"
    const val NOTA_INVOICE_TABLE = "nota_invoice_table"
    const val REASON_INVOICE_TABLE = "reason_table"
    const val BANK_RECEIPT_TABLE = "bank_receipt_table"
}

object TableIdConstant {
    const val VISIT = 2
    const val CUSTOMER_INVOICE = 3
    const val NOTA_INVOICE = 4
    const val REASON_INVOICE = 5
    const val BANK_RECEIPT = 6
}

object UpdateFeatureConstant {
    const val UPDATE_DATA_INVOICE = 1
}


object InvoiceStatusPayment{
    const val NOT_PAID = 7
    const val RECEIPT = 8
    const val PAID_TRANSFER = 9

    const val BG_CHECK = 10
}