package com.sss.monikaapps.common.navigation

import android.net.Uri
import com.sss.monikaapps.common.constanta.ArgumentsConstant.CUSTOMER_ID
import com.sss.monikaapps.common.constanta.ArgumentsConstant.FINAL_KM
import com.sss.monikaapps.common.constanta.ArgumentsConstant.ID_EXPENSE
import com.sss.monikaapps.common.constanta.ArgumentsConstant.ID_INVOICE
import com.sss.monikaapps.common.constanta.ArgumentsConstant.ID_MOBILE_ACTIVITY
import com.sss.monikaapps.common.constanta.ArgumentsConstant.ID_VISIT
import com.sss.monikaapps.common.constanta.ArgumentsConstant.INIT_KM
import com.sss.monikaapps.common.constanta.ArgumentsConstant.NET_AMOUNT
import com.sss.monikaapps.common.constanta.ArgumentsConstant.NOMOR_NOTA
import com.sss.monikaapps.common.constanta.ArgumentsConstant.NOTE
import com.sss.monikaapps.common.constanta.ArgumentsConstant.TRNO_ACTIVITY
import com.sss.monikaapps.common.constanta.ArgumentsConstant.TRNO_EXPENSE
import com.sss.monikaapps.common.constanta.ArgumentsConstant.TYPE_ACTIVITY
import com.sss.monikaapps.common.constanta.ArgumentsConstant.TYPE_VISIT
import com.sss.monikaapps.common.constanta.ArgumentsConstant.URL_PHOTO


object Routes {
    const val LOGIN = "login"
    const val CONNECTION = "connection"
    const val CONNECTION_SETTING = "connection_setting"
    const val HOME = "home"
    const val ACTIVITIES = "activities"
    const val EXPANSES = "expanses"
    const val VISIT = "visit"
    const val DOWNLOAD = "download"
    const val SETTING = "setting"
    const val RESULT_DOWNLOAD = "result_download"
    const val MASTER_DATA = "master_data"

    const val INVOICE = "invoice"
    const val MASTER_DATA_EXPENSE = "master_data_expense"

    const val CREATE_ACTIVITY = "create_activity/{$TYPE_ACTIVITY}"

    const val UPDATE_ACTIVITY =
        "update_activity/{$TRNO_ACTIVITY}/{$ID_MOBILE_ACTIVITY}/{$TYPE_ACTIVITY}"
    const val DETAIL_ACTIVITIES =
        "detail_activities/{$TRNO_ACTIVITY}/{$ID_MOBILE_ACTIVITY}"

    const val DETAIL_EXPENSES = "detail_expanses/{$TRNO_EXPENSE}"
    const val DETAIL_PHOTO = "detail_photo/{$URL_PHOTO}"
    const val CREATE_EXPENSE_HEADER = "create_expense_header"
    const val CREATE_EXPENSE_DETAIL = "create_expense_detail/{$TRNO_EXPENSE}"
    const val UPDATE_EXPENSE_DETAIL =
        "update_expense_detail/{$TRNO_EXPENSE}/{$ID_EXPENSE}/{$NET_AMOUNT}/{$NOTE}/{$INIT_KM}/{$FINAL_KM}"

    const val DETAIL_VISIT = "detail_visit/{$ID_VISIT}"
    const val CHECK_IN_VISIT = "check_in_visit/{$ID_VISIT}/{$TYPE_VISIT}"
    const val DETAIL_INVOICE = "detail_invoice/{$CUSTOMER_ID}"
    const val PAYMENT_INVOICE = "payment_invoice/{$ID_INVOICE}/{$NOMOR_NOTA}/{$CUSTOMER_ID}"

    fun detailActivities(trno: String, idMobile: String): String {
        return "detail_activities/$trno/$idMobile"
    }

    fun createActivityOrUpdate(typeActivity: String): String = "create_activity/$typeActivity"
    fun checkOutActivities(trno: String, idMobile: String, typeActivity: String): String {
        return "update_activity/$trno/$idMobile/$typeActivity"
    }

    fun expenseToDetailExpense(trno: String): String = "detail_expanses/$trno"
    fun createExpenseDetail(trno: String) = "create_expense_detail/$trno"
    fun updateExpenseDetail(
        trno: String,
        idExpense: String,
        netAmount: String,
        note: String,
        initKm: String,
        finalKm: String,
    ) = "update_expense_detail/$trno/$idExpense/$netAmount/$note/$initKm/$finalKm"

    fun detailVisit(idVisit: String): String = "detail_visit/$idVisit"
    fun checkInVisit(idVisit: String, type: String) = "check_in_visit/$idVisit/$type"
    fun detailPhoto(url: String): String = "detail_photo/${Uri.encode(url)}"

    fun detailInvoice(customerId: String): String = "detail_invoice/$customerId"
    fun paymentInvoice(idInvoice: String, nomorNota: String, customerId: String): String =
        "payment_invoice/$idInvoice/$nomorNota/$customerId"
}