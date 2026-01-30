package com.sss.monikaapps.common.repository.location

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.sss.monikaapps.common.result.LocationError
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.sss.monikaapps.common.result.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
class LocationRepositoryImpl(
    private val fusedLocation: FusedLocationProviderClient,
) : LocationRepository {

    @SuppressLint("MissingPermission")
    override suspend fun getUserLocation(timeout: Long): Result<Pair<Double, Double>> =
        suspendCancellableCoroutine { cont ->

            val request = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                0
            ).setWaitForAccurateLocation(true)
                .setMaxUpdates(1)
                .build()

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    fusedLocation.removeLocationUpdates(this)
                    val loc = result.lastLocation
                    if (loc != null && cont.isActive) {
                        cont.resume(Result.success(loc.latitude to loc.longitude))
                    } else if (cont.isActive) {
                        cont.resume(Result.error(null, LocationError.NoLocation.code))
                    }
                }
            }

            fusedLocation.requestLocationUpdates(request, callback, Looper.getMainLooper())

            // Timeout
            CoroutineScope(Dispatchers.IO).launch {
                delay(timeout)
                if (cont.isActive) {
                    fusedLocation.removeLocationUpdates(callback)
                    cont.resume(Result.error(null, LocationError.Timeout.code))
                }
            }
        }
}
