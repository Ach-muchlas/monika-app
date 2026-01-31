package com.sss.monikaapps.feature.download.data.mapper

import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_IN
import com.sss.monikaapps.common.db.entity.PhotoEntity
import com.sss.monikaapps.common.model.Status
import com.sss.monikaapps.feature.activity.data.response.PhotoItem
import com.sss.monikaapps.feature.visit.data.entity.VisitEntity
import com.sss.monikaapps.feature.visit.data.response.DataItemVisit
import com.sss.monikaapps.feature.visit.domain.model.CheckInVisitRequest
import com.sss.monikaapps.feature.visit.domain.model.CheckOutVisitRequest
import java.io.File

object VisitMapper {
    fun serverToEntity(data: List<DataItemVisit>?): List<VisitEntity> {
        return data?.map { customer ->
            VisitEntity(
                customerId = customer.custID ?: "",
                customerName = customer.custName ?: "",
                address = customer.address ?: "",
                desa = customer.desa ?: "",
                kecamatan = customer.kecamatan ?: "",
                kota = customer.kota ?: "",
                provinsi = customer.provinsi ?: "",
                customerLatitude = customer.gpsLatitude ?: "",
                customerLongitude = customer.gpsLongitude ?: "",
            )
        } ?: emptyList()
    }

    fun photoEntityToPhotoItem(data: List<PhotoEntity>?): List<PhotoItem> {
        return data?.map { photo ->
            PhotoItem(
                id = photo.id,
                tipe = photo.parentFeature.toString(),
                path = photo.filePath
            )
        } ?: emptyList()
    }

    fun toCheckInVisitRequest(
        entity: VisitEntity,
        photos: List<PhotoEntity>,
    ): CheckInVisitRequest {

        return CheckInVisitRequest(
            description = entity.description,
            startAt = entity.startAt.toString(),
            startLatitude = entity.startLatitude.toString(),
            startLongitude = entity.startLongitude.toString(),
            trnoMobile = entity.id,
            customerId = entity.customerId,
            customerName = entity.customerName,
            customerAddress = entity.address,
            customerLat = entity.customerLatitude,
            customerLng = entity.customerLongitude,
            customerKel = entity.desa,
            customerKec = entity.kecamatan,
            customerKab = entity.kota,
            customerProv = entity.provinsi,
            photos = photos.map { File(it.filePath) }
        )
    }

    fun toCheckOutVisitRequest(
        entity: VisitEntity,
        photos: List<PhotoEntity>,
    ): CheckOutVisitRequest {

        return CheckOutVisitRequest(
            endAt = entity.endAt.toString(),
            endLatitude = entity.endLatitude.toString(),
            endLongitude = entity.endLongitude.toString(),
            trnoMobile = entity.id,
            photos = photos.map { File(it.filePath) }
        )
    }

    fun Status.resolveTimeVisit(header: VisitEntity?): String {
        return if (id == CHECK_IN)
            header?.startAt.orEmpty()
        else
            header?.endAt.orEmpty()
    }

    fun Status.resolveLatVisit(header: VisitEntity?): String {
        return if (id == CHECK_IN)
            header?.startLatitude.orEmpty()
        else
            header?.endLatitude?.toString().orEmpty()
    }

    fun Status.resolveLngVisit(header: VisitEntity?): String {
        return if (id == CHECK_IN)
            header?.startLatitude?.toString().orEmpty()
        else
            header?.endLongitude?.toString().orEmpty()
    }


}