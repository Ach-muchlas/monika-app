package com.sss.monikaapps.common.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.sss.monikaapps.feature.login.data.response.DataItemUserLogin

class SessionManager private constructor() {

    private lateinit var sharedPref: SharedPreferences

    fun init(context: Context) {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveDataUser(dataUser: DataItemUserLogin?) {
        sharedPref.edit().apply {
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
            employeeId = sharedPref.getString(KEY_EMPLOYEE_ID, null),
            employeeName = sharedPref.getString(KEY_EMPLOYEE_NAME, null),
            idDepo = sharedPref.getString(KEY_ID_DEPO, null),
            namaDepo = sharedPref.getString(KEY_NAMA_DEPO, null),
            idRole = sharedPref.getString(KEY_ROLE_ID, null),
            roleName = sharedPref.getString(KEY_ROLE_NAME, null),
            roleLevel = sharedPref.getString(KEY_ROLE_LEVEL, null),
            idSuperior = sharedPref.getString(KEY_SUPERIOR_ID, null),
            superiorName = sharedPref.getString(KEY_SUPERIOR_NAME, null),
            dateJoin = sharedPref.getString(KEY_DATE_JOIN, null),
            imei = sharedPref.getString(KEY_IMEI, null),
            token = sharedPref.getString(KEY_TOKEN, null)
        )
    }

    fun isUserLogin(): Boolean =
        sharedPref.getBoolean(KEY_LOGIN, false)

    fun clearData() {
        sharedPref.edit() { clear() }
    }

    companion object {
        private const val PREF_NAME = "user_pref"

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
