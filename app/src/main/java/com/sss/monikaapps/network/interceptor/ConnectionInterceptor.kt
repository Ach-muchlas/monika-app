package com.sss.monikaapps.network.interceptor
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

class ConnectionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            chain.proceed(chain.request())
        } catch (e: UnknownHostException) {
            throw ConnectionLostException("Server tidak ditemukan. Periksa URL atau koneksi internet Anda.")
        } catch (e: SocketTimeoutException) {
            throw ConnectionLostException("Koneksi timeout! Server tidak merespons.")
        } catch (e: SSLException) {
            throw ConnectionLostException("Kesalahan sertifikat keamanan! Periksa koneksi Anda.")
        }
//        catch (e: IOException) {
//            throw ConnectionLostException("Koneksi terputus. Silakan periksa jaringan Anda.")
//        }
    }
}

class ConnectionLostException(message: String) : IOException(message)
