package com.sss.monikaapps.feature.activity.data.validator

import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_IN
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_OUT
import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity
import com.sss.monikaapps.feature.activity.data.local.ActivityLocalDataSource

class ActivityValidator(
    private val local: ActivityLocalDataSource,
) {

    suspend fun validateCheckInForSubmit(payload: ActivityEntity): String? {
        if (local.countStillCheckIn() != 0)
            return "Aktivitas sebelumnya belum dicheck out."

        return baseValidation(payload, CHECK_IN)
    }

    suspend fun validateCheckInForSync(payload: ActivityEntity): String? {
        // ❗ TIDAK BOLEH cek countStillCheckIn
        return baseValidation(payload, CHECK_IN)
    }

    private suspend fun baseValidation(payload: ActivityEntity, type: String): String? {
        if (payload.title.isBlank()) return "Judul tidak boleh kosong"
        if (payload.description.isBlank()) return "Deskripsi tidak boleh kosong"

        val photo = local.fetchPhotosByParentIdAndParentType(payload.id, type)
        if (photo.isEmpty()) return "Minimal 1 foto harus ditambahkan"

        return null
    }


    suspend fun validateCheckOut(trno: String): String? {
        val photo = local.fetchPhotosByParentIdAndParentType(trno, CHECK_OUT)
        if (photo.isEmpty()) return "Minimal 1 foto harus ditambahkan"
        return null
    }

}
