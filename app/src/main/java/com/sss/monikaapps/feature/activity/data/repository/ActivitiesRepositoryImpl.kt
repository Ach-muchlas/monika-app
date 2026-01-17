package com.sss.monikaapps.feature.activity.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.sss.monikaapps.feature.activity.data.dao.ActivityDao
import com.sss.monikaapps.common.db.dao.PhotoDao
import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity
import com.sss.monikaapps.common.db.entity.LogEntity
import com.sss.monikaapps.network.ApiService
import com.sss.monikaapps.feature.activity.domain.model.ActivityCheckInRequest
import com.sss.monikaapps.feature.activity.domain.model.ActivityCheckOutRequest
import com.sss.monikaapps.feature.activity.domain.model.toMultipartBody
import com.sss.monikaapps.feature.activity.domain.model.toMultipartImageParts
import com.sss.monikaapps.feature.activity.data.response.DataItemActivities
import com.sss.monikaapps.feature.activity.data.response.DataItemDetailActivity
import com.sss.monikaapps.common.formatter.FormatterDate.getCurrentDate
import com.sss.monikaapps.common.helper.NetworkHelper.isInternetAvailable
import com.sss.monikaapps.common.helper.ResponseHelper
import com.sss.monikaapps.common.mapper.MapperActivity
import com.sss.monikaapps.common.result.Result
import kotlinx.coroutines.Dispatchers
import java.io.File

class ActivitiesRepositoryImpl(
    private val context: Context,
    private val apiService: ApiService,
    private val dao: ActivityDao,
    private val photoDao: PhotoDao,
) : ActivitiesRepository {

    override fun fetchDataActivities(): LiveData<Result<List<DataItemActivities>>> =
        liveData(Dispatchers.IO) {
            emit(Result.loading(null))

            // 1️⃣ Ambil data lokal dulu
            val fetchDataLocal = dao.fetchDataActivityLocalDatabase()
            val localData = MapperActivity.mapperActivities(fetchDataLocal)

            emit(Result.success(localData))

            try {
                val fetchDataServer = apiService.fetchDataActivities(getCurrentDate())

                if (fetchDataServer.isSuccessful) {
                    val serverData = fetchDataServer.body()?.data.orEmpty().map {
                        it.copy(locationData = 1)
                    }

                    val combined = (localData + serverData).distinctBy { it.trnoMobile }

                    emit(Result.success(combined))
                }
            } catch (e: Exception) {
                emit(Result.error(null, e.message ?: "ERROR OCCURRED"))
            }
        }


    override fun fetchDetailActivity(trno: String): LiveData<Result<DataItemDetailActivity>> =
        liveData(Dispatchers.IO) {
            emit(Result.loading(null))
            try {
                val response = apiService.fetchDetailActivity(trno)

                if (response.isSuccessful) {
                    emit(Result.success(response.body()?.data))
                } else {
                    emit(Result.error(null, ResponseHelper.parseErrorResponse(response)))
                }
            } catch (e: Exception) {
                emit(Result.error(null, e.message ?: "ERROR OCCURRED"))
            }
        }

    override fun fetchDetailActivityLocalDatabase(
        trno: String,
        employeeId: String,
    ): LiveData<Result<DataItemDetailActivity>> = liveData {
        emit(Result.loading(null))
        try {
            val header = dao.fetchDataDetailActivityLocalDatabase(trno)
            val photo = MapperActivity.mapperPhotoEntityToPhotoResponse(
                photoDao.getPhotosByParentId(trno)
            )
            val data = DataItemDetailActivity(
                header = MapperActivity.mapperDetailHeaderActivities(
                    header, employeeId
                ), fotoActivity = photo
            )

            emit(Result.success(data))
        } catch (e: Exception) {
            emit(Result.error(null, e.message ?: "ERROR OCCURRED"))
        }
    }

    override fun createActivity(payload: ActivityEntity): LiveData<Result<String>> =
        liveData(Dispatchers.IO) {
            emit(Result.loading(null))

            try {
                val photo = photoDao.getPhotosByParentId(payload.id)
                val dataStillCheckIn = dao.countStillCheckIn()

                if (dataStillCheckIn != 0) {
                    emit(Result.error(null, "Aktivitas sebelumnya belum dicheck out."))
                    return@liveData
                }

                if (payload.title.isBlank()) {
                    emit(Result.error(null, "Judul tidak boleh kosong"))
                    return@liveData
                }

                if (payload.description.isBlank()) {
                    emit(Result.error(null, "Deskripsi tidak boleh kosong"))
                    return@liveData
                }

                if (photo.isEmpty()) {
                    emit(Result.error(null, "Minimal 1 foto harus ditambahkan"))
                    return@liveData
                }

                insertLog(
                    "Check In Aktivitas",
                    "Menyimpan aktivitas ke database lokal. ID = ${payload.id}"
                )

                dao.insertActivity(payload)
                dao.markCheckInIsDoneInLocal(payload.id)
                if (!isInternetAvailable(context)) {
                    insertLog(
                        "Check In Aktivitas",
                        "Tidak ada koneksi internet. Data hanya disimpan secara lokal. ID = ${payload.id}"
                    )
                    emit(
                        Result.success(
                            "Aktivitas berhasil disimpan di perangkat. Silakan lakukan sinkronisasi ulang untuk mengirim data ke server."
                        )
                    )
                    return@liveData
                }

                try {
                    insertLog(
                        "Check In Aktivitas", "Mencoba mengirim data ke server. ID = ${payload.id}"
                    )

                    val dataRequest = dataLocalCheckIn(payload.id)

                    val response = apiService.checkInActivity(
                        dataRequest.toMultipartBody(), dataRequest.toMultipartImageParts()
                    )

                    val dataTrno = response.body()?.data

                    if (!dataTrno.isNullOrBlank()) {
                        dao.markCheckInIsSyncInServer(payload.id, dataTrno)

                        insertLog(
                            "Check In Aktivitas",
                            "Berhasil sync ke server. LocalID = ${payload.id}, ServerID = $dataTrno"
                        )

                        emit(
                            Result.success(
                                "Aktivitas berhasil disimpan dan dikirim ke server."
                            )
                        )
                    } else {

                        insertLog(
                            "Check In Aktivitas",
                            "Server tidak mengembalikan ID. Data tetap disimpan lokal. ID = ${payload.id}"
                        )

                        emit(
                            Result.success(
                                "Aktivitas berhasil disimpan di perangkat, namun belum berhasil dikirim ke server. Silakan lakukan sinkronisasi ulang."
                            )
                        )
                    }

                } catch (apiError: Exception) {

                    insertLog(
                        "Check In Aktivitas",
                        "Gagal mengirim ke server. Data tetap disimpan lokal. ID = ${payload.id}. Error = ${apiError.message}"
                    )

                    emit(
                        Result.success(
                            "Aktivitas berhasil disimpan di perangkat, namun gagal dikirim ke server. Silakan lakukan sinkronisasi ulang."
                        )
                    )
                }

            } catch (e: Exception) {
                emit(Result.error(null, e.message ?: "Terjadi kesalahan"))
            }
        }


    override fun checkOutActivity(
        trno: String,
        idMobile: String,
        endTime: String,
        latitude: String,
        longitude: String,
    ): LiveData<Result<String>> = liveData(Dispatchers.IO) {
        emit(Result.loading(null))
        try {
            val photo = photoDao.getPhotosByParentId(idMobile)

            if (photo.isEmpty()) {
                emit(
                    Result.error(
                        null,
                        "Minimal satu foto harus ditambahkan sebelum melakukan check-out."
                    )
                )
                return@liveData
            }

            insertLog(
                "Check-out Aktivitas",
                "Menyimpan data check-out ke database lokal. ID Lokal = $idMobile"
            )

            dao.updateActivity(
                idActivity = idMobile,
                checkOutTime = endTime,
                latitude = latitude,
                longitude = longitude
            )

            dao.markCheckOutIsDoneInLocal(idMobile)

            if (!isInternetAvailable(context)) {
                insertLog(
                    "Check-out Aktivitas",
                    "Tidak ada koneksi internet. Data hanya disimpan secara lokal. ID Lokal = $idMobile"
                )
                emit(
                    Result.success(
                        "Check-out berhasil disimpan di perangkat. Silakan lakukan sinkronisasi untuk mengirim data ke server."
                    )
                )
                return@liveData
            }

            try {
                insertLog(
                    "Check-out Aktivitas",
                    "Mencoba mengirim data ke server. ID Lokal = $idMobile"
                )

                val dataRequest = dataLocalCheckOut(
                    idMobile,
                    timeEnd = endTime,
                    latitude = latitude,
                    longitude = longitude
                )

                val response = apiService.checkOutActivity(
                    trno = trno,
                    dataRequest.toMultipartBody(),
                    dataRequest.toMultipartImageParts()
                )

                val dataTrno = response.body()?.data

                if (!dataTrno.isNullOrBlank()) {
                    dao.markCheckOutIsSyncInServer(idMobile)

                    insertLog(
                        "Check-out Aktivitas",
                        "Berhasil disinkronkan ke server. ID Lokal = $idMobile, ID Server = $dataTrno"
                    )

                    emit(
                        Result.success(
                            "Check-out berhasil disimpan dan berhasil dikirim ke server."
                        )
                    )
                } else {

                    insertLog(
                        "Check-out Aktivitas",
                        "Server tidak mengembalikan ID. Data tetap disimpan secara lokal. ID Lokal = $idMobile"
                    )

                    emit(
                        Result.success(
                            "Check-out berhasil disimpan di perangkat, tetapi belum berhasil dikirim ke server. Silakan lakukan sinkronisasi ulang."
                        )
                    )
                }

            } catch (apiError: Exception) {

                insertLog(
                    "Check-out Aktivitas",
                    "Gagal mengirim data ke server. Data tetap disimpan secara lokal. ID Lokal = $idMobile. Error = ${apiError.message}"
                )

                emit(
                    Result.success(
                        "Check-out berhasil disimpan di perangkat, tetapi gagal dikirim ke server. Silakan lakukan sinkronisasi ulang."
                    )
                )
            }

        } catch (e: Exception) {
            emit(Result.error(null, e.message ?: "Terjadi kesalahan saat memproses check-out."))
        }
    }

    override fun syncManualActivities(): LiveData<Result<String>> = liveData(Dispatchers.IO) {
        emit(Result.loading(null))
    }


    private suspend fun insertLog(title: String, desc: String) {
        dao.insertLogActivity(
            LogEntity(
                title = title, description = desc, typeFeature = "Aktifitas"
            )
        )
    }

    private suspend fun dataLocalCheckIn(payloadId: String): ActivityCheckInRequest {
        val payload = dao.fetchDataDetailActivityLocalDatabase(payloadId)
        val photos = photoDao.getPhotosByParentId(payloadId)

        val photo = photos.map { File(it.filePath) }

        return ActivityCheckInRequest(
            title = payload.title,
            description = payload.description,
            startAt = payload.startAt.toString(),
            startLatitude = payload.startLatitude.toString(),
            startLongitude = payload.startLongitude.toString(),
            trnoMobile = payload.id,
            photoActivity = photo,
        )
    }

    private suspend fun dataLocalCheckOut(
        idMobile: String,
        timeEnd: String,
        latitude: String,
        longitude: String,
    ): ActivityCheckOutRequest {

        val photos = photoDao.getPhotosByParentId(idMobile)

        val photo = photos.map { File(it.filePath) }

        return ActivityCheckOutRequest(
            endAt = timeEnd,
            endLatitude = latitude,
            endLongitude = longitude,
            trnoMobile = idMobile,
            photoActivity = photo,
        )
    }

}