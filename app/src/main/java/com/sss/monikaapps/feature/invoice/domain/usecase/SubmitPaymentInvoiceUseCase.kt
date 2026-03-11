package com.sss.monikaapps.feature.invoice.domain.usecase

import android.content.Context
import androidx.compose.ui.semantics.error
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.helper.NetworkHelper
import com.sss.monikaapps.feature.invoice.domain.repository.InvoiceRepository
import com.sss.monikaapps.common.result.Result

class SubmitPaymentInvoiceUseCase(
    private val repository: InvoiceRepository,
    private val context: Context,
) {
    suspend operator fun invoke(
        nota: String,
        customerId: String,
        moneyPaid: Long,
        status: Int,
        reasonId: Int,
        descReason: String,
        gpsLat: String,
        gpsLng: String,
    ): Result<String> {

        // 1. SIMPAN LOKAL DULU (Wajib Berhasil)
        val localResult = repository.submitPaymentInvoiceLocal(
            nota, customerId, moneyPaid, status, reasonId, descReason, gpsLat, gpsLng
        )

        if (localResult <= 0) {
            return Result.error(null, "Gagal menyimpan data ke HP")
        }

        // 2. CEK INTERNET
        if (!NetworkHelper.isInternetAvailable(context)) {
            return Result.success("Data tersimpan di HP (Offline)")
        }

        // 3. PUSH KE REMOTE
        val remoteResult = repository.submitPaymentInvoiceRemote(nota, customerId)

        return remoteResult
//        return if (remoteResult.status == StatusNetwork.SUCCESS) {
//            Result.success("Data berhasil tersimpan dan tersinkron")
//        } else {
//            Result.error(null, "Data tersimpan di HP, gagal kirim ke server")
//        }
    }
}