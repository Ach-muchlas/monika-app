package com.sss.monikaapps.feature.utils.device.domain.usecase

import com.sss.monikaapps.feature.utils.device.domain.repository.DeviceInfoRepository

class GetDeviceIdUseCase(
    private val repository: DeviceInfoRepository,
) {
    suspend operator fun invoke(): String = repository.getDeviceId()
}
