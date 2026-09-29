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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.graphics.scale
@Composable
fun rememberPhotoPickerWithCompress(
    feature: String,
    typeFeature: String,
    onPhotoPicked: (String) -> Unit
): Pair<() -> Unit, () -> Unit> {

    val context = LocalContext.current
    var photoFile by remember { mutableStateOf<File?>(null) }

    // 📷 CAMERA
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            val file = photoFile
            if (success && file != null) {
                compressImage(file)
                onPhotoPicked(file.absolutePath)
            }
        }

    // 🖼️ GALLERY
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val file = createImageFile(context, feature, typeFeature)
                copyUriToFile(context, uri, file)
                compressImage(file)
                onPhotoPicked(file.absolutePath)
            }
        }

    val openCamera = {
        val file = createImageFile(context, feature, typeFeature)
        photoFile = file

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        cameraLauncher.launch(uri)
    }

    val openGallery = {
        galleryLauncher.launch("image/*")
    }

    return openCamera to openGallery
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


private fun compressImage(file: File) {
    val bitmap = BitmapFactory.decodeFile(file.absolutePath) ?: return

    val maxWidth = 1024
    val scale = maxWidth.toFloat() / bitmap.width
    val newHeight = (bitmap.height * scale).toInt()

    val resized = bitmap.scale(maxWidth, newHeight)

    FileOutputStream(file).use {
        resized.compress(Bitmap.CompressFormat.JPEG, 60, it)
    }

    bitmap.recycle()
    resized.recycle()
}

private fun copyUriToFile(context: Context, uri: Uri, file: File) {
    context.contentResolver.openInputStream(uri)?.use { input ->
        FileOutputStream(file).use { output ->
            input.copyTo(output)
        }
    }
}
