package com.sss.monikaapps.common.helper

import org.json.JSONObject
import retrofit2.Response

object ResponseHelper {

    fun parseErrorResponse(response: Response<*>): String {
        return response.errorBody()?.let {
            JSONObject(it.string()).getString("message")
        } ?: "Unknown error"
    }

}