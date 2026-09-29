package com.sss.monikaapps.common.helper

import android.content.Context
import android.util.Log
import java.io.File

object StorageHelper {
    fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            dir.list()?.forEach { child ->
                if (!deleteDir(File(dir, child))) return false
            }
        }
        return dir.delete()
    }

    fun clearAppCache(context: Context) {
        try {
            deleteDir(context.cacheDir)
        } catch (e: Exception) {
            Log.e("STORAGE_HELPER", "Failed to clear cache: ${e.message}")
        }
    }

    fun deleteAppStorage(context: Context) {
        context.filesDir?.let { deleteDir(it) }
        context.cacheDir?.let { deleteDir(it) }
        context.externalCacheDir?.let { deleteDir(it) }
        context.getExternalFilesDir(null)?.let { deleteDir(it) }
    }
}