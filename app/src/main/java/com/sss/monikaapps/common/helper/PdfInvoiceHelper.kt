package com.sss.monikaapps.common.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.sss.monikaapps.common.constanta.ApiConstant
import com.sss.monikaapps.common.formatter.FormatterCurrency.formatCurrency
import com.sss.monikaapps.common.formatter.FormatterDate.formatDate
import com.sss.monikaapps.common.manager.SessionManager
import com.sss.monikaapps.feature.invoice.data.response.DataItemInvoice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import androidx.core.graphics.scale

object PdfInvoiceHelper {
    suspend fun generateMonitoringPdf(
        context: Context,
        data: List<DataItemInvoice>,
        date: String,
        collName: String,
    ): File? = withContext(Dispatchers.IO) {

        val token = SessionManager.getInstance().getDataUser().token ?: ""
        val baseUrl = ApiConstant.urlDomain()

        val pdf = PdfDocument()
        val pageWidth = 842 // Landscape
        val pageHeight = 595
        val margin = 30f

        var pageNumber = 1
        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        var page = pdf.startPage(pageInfo)
        var canvas = page.canvas

        val bold = Paint().apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textSize = 9f
        }

        val normal = Paint().apply {
            textSize = 8f
        }

        val italic = Paint().apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            textSize = 8f
        }

        // Definisi Posisi X Kolom agar Rapi
        val xDate = margin
        val xNota = 95f
        val xJth = 180f
        val xNominal = 265f
        val xPaid = 355f
        val xSisa = 445f
        val xPayment = 535f
        val xSisaTagihan = 625f
        val xDist = 715f

        var y = drawHeader(canvas, margin, date, collName, pageWidth)

        fun newPage() {
            pdf.finishPage(page)
            pageNumber++
            pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
            page = pdf.startPage(pageInfo)
            canvas = page.canvas
            y = drawHeader(canvas, margin, date, collName, pageWidth)
        }

        val grouped = data.groupBy { it.customerId }

        grouped.forEach { (customerId, invoices) ->
            if (y > pageHeight - 100) newPage()

            val first = invoices.firstOrNull() ?: return@forEach

            canvas.drawText(
                "$customerId - ${first.customerName}    ${first.customerAddress ?: ""}",
                margin,
                y,
                bold
            )
            y += 15f

            invoices.forEach { inv ->
                if (y > pageHeight - 120) newPage()

                val pay =
                    (inv.amount?.toDoubleOrNull() ?: 0.0) - (inv.outstandingNota?.toDoubleOrNull()
                        ?: 0.0)

                val sisaTagihan = (inv.outstandingNota?.toDoubleOrNull() ?: 0.0) - (inv.payment?.toDoubleOrNull() ?: 0.0)
                // Baris Data
                canvas.drawText(inv.dateNota ?: "-", xDate, y, normal)
                canvas.drawText(inv.nomorNota ?: "-", xNota, y, normal)
                canvas.drawText(inv.dueDate ?: "-", xJth, y, normal)

                canvas.drawText(
                    formatCurrency(inv.amount?.toDoubleOrNull()?.toLong() ?: 0L),
                    xNominal,
                    y,
                    normal
                )
                canvas.drawText(
                    formatCurrency(pay.toLong()), xPaid, y, normal
                )
                canvas.drawText(
                    formatCurrency(
                        inv.outstandingNota?.toDoubleOrNull()?.toLong() ?: 0L
                    ), xSisa, y, normal
                )

                canvas.drawText(
                    formatCurrency(inv.payment?.toDoubleOrNull()?.toLong() ?: 0L),
                    xPayment,
                    y,
                    normal
                )
                canvas.drawText(
                    formatCurrency(sisaTagihan.toLong()),
                    xSisaTagihan,
                    y,
                    normal
                )
                canvas.drawText(inv.distanceDiff ?: "0", xDist, y, normal)

                y += 12f

                // ALASAN (Jika ada)
                inv.descReason?.takeIf { it.isNotBlank() }?.let { reason ->
                    canvas.drawText("Alasan: $reason", margin + 10f, y, italic)
                    y += 12f
                }

                // FOTO
                inv.foto?.takeIf { it.isNotEmpty() }?.let { photos ->
                    val photoWidth = 100f
                    val photoHeight = 75f
                    val spacing = 5f
                    var xPhoto = margin + 10f

                    photos.forEach { foto ->
                        if (xPhoto + photoWidth > pageWidth - margin) {
                            xPhoto = margin + 10f
                            y += photoHeight + spacing
                        }

                        val url = "${baseUrl}monika-view-image/${foto.folderName}/${foto.path}"
                        val bitmap = loadBitmap(url, token)
                        bitmap?.let {
                            val scaled = it.scale(photoWidth.toInt(), photoHeight.toInt())
                            canvas.drawBitmap(scaled, xPhoto, y, null)
                            xPhoto += photoWidth + spacing
                        }
                    }
                    y += photoHeight + 15f
                }

                y += 5f // Spasi antar nota
            }
            y += 10f // Spasi antar customer
        }

        pdf.finishPage(page)

        val name =
            "Monitoring_Tagihan_${collName}_${formatDate(date)}_${System.currentTimeMillis()}.pdf"

        // REKOMENDASI: Gunakan folder Download publik
        val downloadDir =
            android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS)
        if (!downloadDir.exists()) {
            downloadDir.mkdirs()
        }

        val file = File(downloadDir, name)

        return@withContext try {
            val outputStream = FileOutputStream(file)
            pdf.writeTo(outputStream)
            outputStream.flush()
            outputStream.close()
            pdf.close()
            file
        } catch (e: Exception) {
            pdf.close()
            null
        }
    }

    private fun drawHeader(
        canvas: Canvas,
        margin: Float,
        date: String,
        collName: String,
        width: Int,
    ): Float {
        val titlePaint = Paint().apply {
            textSize = 14f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val headerPaint = Paint().apply {
            textSize = 9f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        var y = margin + 10f
        canvas.drawText("PT. SAKTISETIA SANTOSA", margin, y, titlePaint)
        y += 18f
        canvas.drawText("Monitoring Tagihan Harian", margin, y, titlePaint)
        y += 18f
        canvas.drawText("Date : $date", margin, y, headerPaint)
        canvas.drawText("Collector : $collName", 250f, y, headerPaint)

        y += 10f
        canvas.drawLine(margin, y, width - margin, y, headerPaint)
        y += 12f

        // Label Kolom
        canvas.drawText("Tanggal", margin, y, headerPaint)
        canvas.drawText("No. Nota", 95f, y, headerPaint)
        canvas.drawText("Jth Temp", 180f, y, headerPaint)
        canvas.drawText("Nominal", 265f, y, headerPaint)
        canvas.drawText("Terbayar", 355f, y, headerPaint)
        canvas.drawText("Sisa Nota", 445f, y, headerPaint)
        canvas.drawText("Bayar", 535f, y, headerPaint)
        canvas.drawText("Sisa Tagihan", 625f, y, headerPaint)
        canvas.drawText("Selisih Jarak", 715f, y, headerPaint)

        y += 5f
        canvas.drawLine(margin, y, width - margin, y, headerPaint)
        return y + 15f
    }

    private fun loadBitmap(urlString: String, token: String): Bitmap? {
        return try {

            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty("Authorization", "Bearer $token")
            connection.connect()

            val options = BitmapFactory.Options().apply {
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }

            BitmapFactory.decodeStream(connection.inputStream, null, options)

        } catch (e: Exception) {
            null
        }
    }
}