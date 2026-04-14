package com.sss.monikaapps.common.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.location.Geocoder
import android.net.Uri
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class PhotoHelper(
    private val context: Context,
    private val featureName: String,
    private val typeFeature: String,
    private val enableWatermark: Boolean = false,
    private val getLocation: (suspend () -> Pair<Double, Double>?)? = null,

    private val onPhotoTaken: (String) -> Unit,
) {

    private var photoFile: File? = null
    private var photoUri: Uri? = null

    @Composable
    fun rememberCameraLauncher(): () -> Unit {
        val scope = rememberCoroutineScope()

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->

            val file = photoFile

            if (success && file != null) {
                scope.launch {
                    processPhoto(file)
                    onPhotoTaken(file.absolutePath)
                }
            }
        }

        return {
            val file = createImageFile(context, featureName, typeFeature)
            photoFile = file

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
            photoUri = uri

            launcher.launch(uri)
        }
    }

    private fun createImageFile(context: Context, feature: String, typeFeature: String): File {
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.getDefault()).format(Date())

        val storageDir = File(context.filesDir, "photo/$feature")
        if (!storageDir.exists()) storageDir.mkdirs()

        return File(storageDir, "${typeFeature}_${timeStamp}.jpg")
    }

    // 🔥 CORE PIPELINE
    private suspend fun processPhoto(file: File) {
        val bitmap = BitmapFactory.decodeFile(file.path) ?: return

        val finalBitmap = if (enableWatermark && getLocation != null) {
            try {
                val location = getLocation()

                val text = if (location != null) {
                    val (lat, lng) = location
                    val address = getAddress(context, lat, lng)

                    """
                    $address
                    Lat: $lat
                    Lng: $lng
                    ${SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date())}
                    """.trimIndent()
                } else {
                    "Lokasi tidak tersedia"
                }

                addTextToBitmap(bitmap, text)

            } catch (_: Exception) {
                bitmap // fallback
            }
        } else {
            bitmap // 🔥 tidak pakai watermark
        }

        val resized = resizeBitmap(finalBitmap, 1024)

        FileOutputStream(file).use {
            resized.compress(Bitmap.CompressFormat.JPEG, 60, it)
        }

        if (finalBitmap != bitmap) finalBitmap.recycle()
        bitmap.recycle()
        resized.recycle()
    }

    // 🔹 Geocoder (background)
    private suspend fun getAddress(context: Context, lat: Double, lng: Double): String {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(lat, lng, 1)

                addresses?.firstOrNull()?.getAddressLine(0)
                    ?: "Alamat tidak ditemukan"
            } catch (_: Exception) {
                "Gagal mendapatkan alamat"
            }
        }
    }

    // 🔹 Draw text ke bitmap
    fun addTextToBitmap(bitmap: Bitmap, text: String): Bitmap {
        val result = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)

        val padding = 20f

        val paint = TextPaint().apply {
            color = Color.WHITE
            textSize = 40f
            isAntiAlias = true
            setShadowLayer(5f, 0f, 0f, Color.BLACK)
        }

        val maxWidth = bitmap.width - (padding * 2)

        val staticLayout = StaticLayout.Builder
            .obtain(text, 0, text.length, paint, maxWidth.toInt())
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(10f, 1f)
            .setIncludePad(false)
            .build()

        // 🔥 posisikan dari bawah
        val textHeight = staticLayout.height
        val x = padding
        val y = bitmap.height - textHeight - padding

        canvas.save()
        canvas.translate(x, y)
        staticLayout.draw(canvas)
        canvas.restore()

        return result
    }

    // 🔹 Resize biar ringan
    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int): Bitmap {
        val scale = maxWidth.toFloat() / bitmap.width
        val newHeight = (bitmap.height * scale).toInt()
        return bitmap.scale(maxWidth, newHeight)
    }
}
