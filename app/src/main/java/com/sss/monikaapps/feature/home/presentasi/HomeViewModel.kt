package com.sss.monikaapps.feature.home.presentasi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_ACTIVITIES
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_DOWNLOAD
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_EXPENSES
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_MASTER_DATA
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_SETTING
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_VISIT
import com.sss.monikaapps.common.constanta.TableNameConstant.VISIT_TABLE
import com.sss.monikaapps.common.manager.SessionManager
import com.sss.monikaapps.common.navigation.RouteDestination
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity
import com.sss.monikaapps.feature.download.domain.usecase.InsertDownloadUseCase
import com.sss.monikaapps.feature.home.data.sealed.HomeNavEvent
import com.sss.monikaapps.feature.home.domain.usecase.CheckPendingDataDownloadUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val insertDownloadUseCase: InsertDownloadUseCase,
    private val checkPendingDataDownloadUseCase: CheckPendingDataDownloadUseCase,
) : ViewModel() {

    private val sessionManager = SessionManager.getInstance()

    private val _user = MutableStateFlow(sessionManager.getDataUser())
    val user = _user.asStateFlow()

    fun clearUserSession() = sessionManager.clearSession()

    init {
        _user.value = sessionManager.getDataUser()
        initConfigDownload()
    }

    private fun initConfigDownload() {
        viewModelScope.launch {
            val defaultConfig = listOf(
                ConfigDownloadDataEntity(
                    id = FEATURE_VISIT,
                    tableName = VISIT_TABLE,
                    totalDataMobile = 0,
                    totalDataServer = 0,
                    statusTotalDownload = false
                ),
            )
            insertDownloadUseCase(defaultConfig)
        }
    }

    private val _navEvent = Channel<HomeNavEvent>(Channel.BUFFERED)
    val navEvent = _navEvent.receiveAsFlow()

    fun onMenuClicked(idMenu: Int) {
        viewModelScope.launch {
            val pending = checkPendingDataDownloadUseCase().data ?: 0

            when {
                // ✅ DOWNLOAD selalu boleh
                idMenu == FEATURE_DOWNLOAD -> {
                    _navEvent.send(
                        HomeNavEvent.Navigate(RouteDestination.HomeToDownload)
                    )
                }

                // ✅ SETTING selalu boleh
                idMenu == FEATURE_SETTING -> {
                    _navEvent.send(
                        HomeNavEvent.Navigate(RouteDestination.HomeToSetting)
                    )
                }

                // ✅ Menu lain hanya jika pending = 0
                pending == 0 -> {
                    val destination = when (idMenu) {
                        FEATURE_ACTIVITIES -> RouteDestination.HomeToActivities
                        FEATURE_EXPENSES -> RouteDestination.HomeToExpanses
                        FEATURE_VISIT -> RouteDestination.HomeToVisit
                        FEATURE_MASTER_DATA -> RouteDestination.HomeToMaster
                        else -> null
                    }

                    destination?.let {
                        _navEvent.send(
                            HomeNavEvent.Navigate(it)
                        )
                    }
                }

                // 🚫 BLOCK
                else -> {
                    _navEvent.send(
                        HomeNavEvent.Blocked(
                            "Download data belum lengkap"
                        )
                    )
                }
            }
        }
    }

}