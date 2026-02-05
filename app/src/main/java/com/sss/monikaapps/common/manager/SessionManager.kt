package com.sss.monikaapps.common.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.sss.monikaapps.feature.login.data.response.DataItemUserLogin

class SessionManager private constructor() {

    private lateinit var userPref: SharedPreferences
    private lateinit var appPref: SharedPreferences

    fun init(context: Context) {
        userPref = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
        appPref = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
    }

    fun saveDataUser(dataUser: DataItemUserLogin?) {
        userPref.edit().apply {
            putString(KEY_EMPLOYEE_ID, dataUser?.employeeId)
            putString(KEY_EMPLOYEE_NAME, dataUser?.employeeName)
            putString(KEY_ID_DEPO, dataUser?.idDepo)
            putString(KEY_NAMA_DEPO, dataUser?.namaDepo)
            putString(KEY_ROLE_ID, dataUser?.idRole)
            putString(KEY_ROLE_NAME, dataUser?.roleName)
            putString(KEY_ROLE_LEVEL, dataUser?.roleLevel)
            putString(KEY_SUPERIOR_ID, dataUser?.idSuperior)
            putString(KEY_SUPERIOR_NAME, dataUser?.superiorName)
            putString(KEY_DATE_JOIN, dataUser?.dateJoin)
            putString(KEY_IMEI, dataUser?.imei)
            putString(KEY_TOKEN, dataUser?.token)
            putBoolean(KEY_LOGIN, true)
            apply()
        }
    }

    fun getDataUser(): DataItemUserLogin {
        return DataItemUserLogin(
            employeeId = userPref.getString(KEY_EMPLOYEE_ID, null),
            employeeName = userPref.getString(KEY_EMPLOYEE_NAME, null),
            idDepo = userPref.getString(KEY_ID_DEPO, null),
            namaDepo = userPref.getString(KEY_NAMA_DEPO, null),
            idRole = userPref.getString(KEY_ROLE_ID, null),
            roleName = userPref.getString(KEY_ROLE_NAME, null),
            roleLevel = userPref.getString(KEY_ROLE_LEVEL, null),
            idSuperior = userPref.getString(KEY_SUPERIOR_ID, null),
            superiorName = userPref.getString(KEY_SUPERIOR_NAME, null),
            dateJoin = userPref.getString(KEY_DATE_JOIN, null),
            imei = userPref.getString(KEY_IMEI, null),
            token = userPref.getString(KEY_TOKEN, null)
        )
    }


    fun isUserLogin(): Boolean =
        userPref.getBoolean(KEY_LOGIN, false)

    fun clearSession() {
        userPref.edit { clear() }
    }


    fun isFirstTime(): Boolean =
        appPref.getBoolean(KEY_IS_FIRST_TIME, true)

    fun setFirstTimeFalse() {
        appPref.edit { putBoolean(KEY_IS_FIRST_TIME, false) }
    }

    companion object {
        private const val USER_PREF = "user_pref"
        private const val APP_PREF = "app_pref"

        private const val KEY_IS_FIRST_TIME = "is_first_time"

        private const val KEY_EMPLOYEE_ID = "id_karyawan"
        private const val KEY_EMPLOYEE_NAME = "nama_karyawan"
        private const val KEY_ID_DEPO = "id_depo"
        private const val KEY_NAMA_DEPO = "nama_depo"
        private const val KEY_ROLE_ID = "id_jabatan"
        private const val KEY_ROLE_NAME = "nama_jabatan"
        private const val KEY_ROLE_LEVEL = "jabatan_level"
        private const val KEY_SUPERIOR_ID = "id_atasan"
        private const val KEY_SUPERIOR_NAME = "nama_atasan"
        private const val KEY_DATE_JOIN = "tanggal_bergabung"
        private const val KEY_IMEI = "imei"
        private const val KEY_TOKEN = "token"
        private const val KEY_LOGIN = "is_login"

        @Volatile
        private var instance: SessionManager? = null

        fun getInstance(): SessionManager =
            instance ?: synchronized(this) {
                instance ?: SessionManager().also { instance = it }
            }
    }
}
