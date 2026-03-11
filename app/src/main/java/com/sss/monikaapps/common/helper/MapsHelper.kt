package com.sss.monikaapps.common.helper

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object MapsHelper {

    fun openGoogleMaps(context: Context, lat: String, lng: String) {
        if (lat == "0" || lng == "0") return

        val gmmIntentUri = "google.navigation:q=$lat,$lng".toUri()
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps")
        }

        try {
            context.startActivity(mapIntent)
        } catch (e: Exception) {
            val fallbackIntent = Intent(
                Intent.ACTION_VIEW,
                "https://www.google.com/maps/dir/?api=1&destination=$lat,$lng".toUri()
            )
            context.startActivity(fallbackIntent)
        }
    }


    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0

        val dLat = (lat2 - lat1).toRadians()
        val dLon = (lon2 - lon1).toRadians()

        val a = sin(dLat / 2).pow(2) +
                cos(lat1.toRadians()) * cos(lat2.toRadians()) *
                sin(dLon / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return r * c * 1000
    }

    fun calculateFormattedDistance(
        customerLat: String?,
        customerLng: String?,
        userLat: String?,
        userLng: String?,
    ): String {
        val cLat = customerLat?.toDoubleOrNull() ?: 0.0
        val cLng = customerLng?.toDoubleOrNull() ?: 0.0
        val uLat = userLat?.toDoubleOrNull() ?: 0.0
        val uLng = userLng?.toDoubleOrNull() ?: 0.0

        if (cLat == 0.0 || uLat == 0.0) return "-"

        val distanceInMeters = calculateDistance(uLat, uLng, cLat, cLng)

        return if (distanceInMeters >= 1000) {
            val km = distanceInMeters / 1000

            String.format(java.util.Locale("id", "ID"), "%,.3f Kilometer", km)
        } else {
            String.format(java.util.Locale("id", "ID"), "%,.0f Meter", distanceInMeters)
        }
    }

    fun Double.toRadians(): Double = Math.toRadians(this)
}