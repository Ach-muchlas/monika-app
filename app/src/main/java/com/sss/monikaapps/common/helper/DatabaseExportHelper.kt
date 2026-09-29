package com.sss.monikaapps.common.helper

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File

object DatabaseExportHelper {
    fun exportUsingMediaStore(
        context: Context,
        dbFile: File,
    ): File? {
        val resolver = context.contentResolver

        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, "app_database.db")
            put(MediaStore.Downloads.MIME_TYPE, "application/octet-stream")
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val uri = resolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            contentValues
        ) ?: return null

        resolver.openOutputStream(uri)?.use { output ->
            dbFile.inputStream().copyTo(output)
        }

        contentValues.clear()
        contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
        resolver.update(uri, contentValues, null, null)

        val tempFile = File(context.cacheDir, "app_database_temp.db")
        dbFile.inputStream().use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return tempFile
    }

    fun exportUsingLegacyMethod(
        context: Context,
        dbFile: File,
    ): File {
        val exportFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "app_database.db"
        )

        dbFile.inputStream().use { input ->
            exportFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        MediaScannerConnection.scanFile(
            context,
            arrayOf(exportFile.absolutePath),
            arrayOf("application/octet-stream"),
            null
        )

        return exportFile
    }
}
