package com.sss.monikaapps.feature.activity.data.validator

import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity
import com.sss.monikaapps.feature.activity.data.local.ActivityLocalDataSource

class ActivityValidator(
    private val local: ActivityLocalDataSource,
) {
    suspend fun validateCheckIn(payload: ActivityEntity): String? {
        if (local.countStillCheckIn() != 0)
            return "Aktivitas sebelumnya belum dicheck out."

        if (payload.title.isBlank()) return "Judul tidak boleh kosong"
        if (payload.description.isBlank()) return "Deskripsi tidak boleh kosong"

        val photo = local.getPhotos(payload.id)
        if (photo.isEmpty()) return "Minimal 1 foto harus ditambahkan"

        return null
    }
}
