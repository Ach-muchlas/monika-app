package com.sss.monikaapps.common.helper

import java.util.UUID

object GenerateRandomTextHelper {
    fun generateRandomId(): String {
        return UUID.randomUUID().toString()
    }

}