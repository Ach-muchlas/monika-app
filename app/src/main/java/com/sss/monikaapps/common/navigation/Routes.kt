package com.sss.monikaapps.common.navigation

import com.sss.monikaapps.utils.constanta.ArgumentsConstant.ID_MOBILE_ACTIVITY
import com.sss.monikaapps.utils.constanta.ArgumentsConstant.LOCATION_DATA
import com.sss.monikaapps.utils.constanta.ArgumentsConstant.TRNO_ACTIVITY
import com.sss.monikaapps.utils.constanta.ArgumentsConstant.TRNO_EXPANSE
import com.sss.monikaapps.utils.constanta.ArgumentsConstant.TYPE_ACTIVITY

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val ACTIVITIES = "activities"
    const val EXPANSES = "expanses"

    const val CREATE_ACTIVITY = "create_activity/{$TYPE_ACTIVITY}"
    const val UPDATE_ACTIVITY =
        "create_activity/{$TRNO_ACTIVITY}/{$ID_MOBILE_ACTIVITY}/{$TYPE_ACTIVITY}"
    const val DETAIL_ACTIVITIES =
        "detail_activities/{$TRNO_ACTIVITY}/{$ID_MOBILE_ACTIVITY}/{$LOCATION_DATA}"

    const val DETAIL_EXPANSES = "detail_expanses/{$TRNO_EXPANSE}"

    fun detailActivities(trno: String, idMobile: String, locationData: Int): String {
        return "detail_activities/$trno/$idMobile/$locationData"
    }

    fun createActivityOrUpdate(typeActivity: String): String {
        return "create_activity/$typeActivity"
    }

    fun checkOutActivities(trno: String, idMobile: String, typeActivity: String): String {
        return "create_activity/$trno/$idMobile/$typeActivity"
    }

    fun expanseToDetailExpanse(trno: String) : String = "detail_expanses/$trno"

}