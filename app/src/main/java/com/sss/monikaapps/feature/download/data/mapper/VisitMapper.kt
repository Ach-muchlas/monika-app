package com.sss.monikaapps.feature.download.data.mapper

import com.sss.monikaapps.feature.visit.data.entity.VisitEntity
import com.sss.monikaapps.feature.visit.data.response.DataItemVisit

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
}