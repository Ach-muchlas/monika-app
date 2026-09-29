package com.sss.monikaapps.common.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import kotlinx.coroutines.launch
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

    private fun processPhoto(file: File) {
        val bitmap = BitmapFactory.decodeFile(file.path) ?: return

        val resized = resizeBitmap(bitmap, 1024)

        FileOutputStream(file).use {
            resized.compress(Bitmap.CompressFormat.JPEG, 60, it)
        }

        bitmap.recycle()
        resized.recycle()
    }

    // 🔹 Resize biar ringan
    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int): Bitmap {
        val scale = maxWidth.toFloat() / bitmap.width
        val newHeight = (bitmap.height * scale).toInt()
        return bitmap.scale(maxWidth, newHeight)
    }
}
