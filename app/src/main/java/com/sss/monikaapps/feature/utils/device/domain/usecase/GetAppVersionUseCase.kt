package com.sss.monikaapps.feature.utils.device.domain.usecase

import com.sss.monikaapps.feature.utils.device.domain.repository.DeviceInfoRepository

class GetAppVersionUseCase(
    private val repository: DeviceInfoRepository
) {
    suspend operator fun invoke(): String = repository.getAppVersion()
}
