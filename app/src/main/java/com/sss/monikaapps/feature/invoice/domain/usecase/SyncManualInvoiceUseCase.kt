package com.sss.monikaapps.feature.invoice.domain.usecase

import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.invoice.domain.repository.InvoiceRepository
import com.sss.monikaapps.network.domain.NetworkChecker

class SyncManualInvoiceUseCase(
    private val repository: InvoiceRepository,
    private val networkChecker: NetworkChecker,
) {
    suspend operator fun invoke(): Result<String> {
        if (!networkChecker.isConnected()) {
            return Result.error(null, "Tidak terkoneksi internet")
        }

        val pendingList = repository.getPaymentInvoiceNotSync()

        if (pendingList.isEmpty()) {
            return Result.success("Tidak ada data yang perlu disinkronkan")
        }

        var successCount = 0
        var failedCount = 0

        for (invoice in pendingList) {
            try {
                // 3. PUSH KE REMOTE
               val result = repository.submitPaymentInvoiceRemote(invoice.nomorNota, invoice.customerId)

                if (result.status == StatusNetwork.SUCCESS) {
                    successCount++
                } else {
                    failedCount++
                }

            } catch (e: Exception) {
                failedCount++
            }
        }

        return Result.success(
            "Sinkronisasi selesai. Berhasil: $successCount, Gagal: $failedCount"
        )

    }

}