package com.sss.monikaapps.common.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhotoHelper(
    private val context: Context,
    private val featureName: String,
    private val typeFeature: String,
    private val onPhotoTaken: (String) -> Unit,
) {

    private var photoFile: File? = null
    private var photoUri: Uri? = null

    @Composable
    fun rememberCameraLauncher(): () -> Unit {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->

            val file = photoFile  // ✅ COPY KE LOCAL VAL

            if (success && file != null) {
                compressImage(context, file)
                onPhotoTaken(file.absolutePath) // 🔥 SIMPAN PATH FILE
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

        return File(
            storageDir,
            "${typeFeature}_${timeStamp}.jpg"
        )
    }

    private fun compressImage(context: Context, file: File) {
        val bitmap = BitmapFactory.decodeFile(file.path) ?: return

        val maxWidth = 1024
        val scale = maxWidth.toFloat() / bitmap.width
        val newHeight = (bitmap.height * scale).toInt()

        val resizedBitmap = bitmap.scale(maxWidth, newHeight)

        FileOutputStream(file).use {
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 60, it)
        }

        bitmap.recycle()
        resizedBitmap.recycle()
    }
}
