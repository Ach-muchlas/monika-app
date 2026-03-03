package com.sss.monikaapps.feature.home.presentasi

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.R
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_ACTIVITIES
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_DOWNLOAD
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_EXPENSES
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_INVOICE
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_MASTER_DATA
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_SETTING
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_VISIT
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.manager.SessionManager
import com.sss.monikaapps.common.navigation.RouteDestination
import com.sss.monikaapps.feature.download.domain.usecase.GetInvoiceCountUseCase
import com.sss.monikaapps.feature.download.domain.usecase.InsertDownloadUseCase
import com.sss.monikaapps.feature.home.data.model.BlockSheetType
import com.sss.monikaapps.feature.home.data.model.HomeMenuItem
import com.sss.monikaapps.feature.home.data.sealed.HomeNavEvent
import com.sss.monikaapps.feature.home.domain.usecase.CheckPendingDataDownloadUseCase
import com.sss.monikaapps.feature.home.domain.usecase.ListTableConfigUseCase
import com.sss.monikaapps.feature.version_check.domain.usecase.FetchVersionAppsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val app: Application,
    private val insertDownloadUseCase: InsertDownloadUseCase,
    private val checkPendingDataDownloadUseCase: CheckPendingDataDownloadUseCase,
    private val fetchVersionAppsUseCase: FetchVersionAppsUseCase,
    private val listTableConfigUseCase: ListTableConfigUseCase,
    getInvoiceCountUseCase: GetInvoiceCountUseCase,
) : ViewModel() {

    private val sessionManager = SessionManager.getInstance()

    private val _user = MutableStateFlow(sessionManager.getDataUser())
    val user = _user.asStateFlow()

    val menuItems: StateFlow<List<HomeMenuItem>> =
        getInvoiceCountUseCase().map { count -> createMenuBasedOnData(count) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    private val _blockSheetState =
        MutableStateFlow(BlockSheetType.NONE)

    val blockSheetState: StateFlow<BlockSheetType> =
        _blockSheetState


    private val _navEvent = Channel<HomeNavEvent>(Channel.BUFFERED)
    val navEvent = _navEvent.receiveAsFlow()


    init {
        _user.value = sessionManager.getDataUser()
        initConfigDownload()
    }

    private fun createMenuBasedOnData(invoiceCount: Int): List<HomeMenuItem> {
        val items = mutableListOf<HomeMenuItem>()

        items.add(
            HomeMenuItem(
                FEATURE_ACTIVITIES,
                app.getString(R.string.text_feature_activity),
                app.getString(R.string.text_desc_feature_activity),
                R.drawable.icon_activity
            )
        )

        items.add(
            HomeMenuItem(
                FEATURE_DOWNLOAD,
                app.getString(R.string.text_feature_download),
                app.getString(R.string.text_desc_feature_download),
                R.drawable.icon_download_data
            )
        )

        items.add(
            HomeMenuItem(
                FEATURE_VISIT,
                app.getString(R.string.text_feature_visited),
                app.getString(R.string.text_desc_feature_visited),
                R.drawable.icon_visited
            )
        )

        if (invoiceCount > 0) {
            items.add(
                HomeMenuItem(
                    FEATURE_INVOICE,
                    app.getString(R.string.text_feature_invoice),
                    app.getString(R.string.text_desc_feature_invoice),
                    R.drawable.icon_tax
                )
            )
        }

        items.add(
            HomeMenuItem(
                FEATURE_EXPENSES,
                app.getString(R.string.text_feature_expanses),
                app.getString(R.string.text_desc_feature_expanses),
                R.drawable.icon_expanses
            )
        )
        items.add(
            HomeMenuItem(
                FEATURE_MASTER_DATA,
                app.getString(R.string.text_feature_master_data),
                app.getString(R.string.text_desc_feature_master_data),
                R.drawable.icon_data_mastering
            )
        )
        items.add(
            HomeMenuItem(
                FEATURE_SETTING,
                app.getString(R.string.text_feature_setting),
                app.getString(R.string.text_desc_feature_setting),
                R.drawable.icon_setting
            )
        )
        return items
    }

    fun clearUserSession() = sessionManager.clearSession()

    private fun initConfigDownload() {
        viewModelScope.launch {
            val defaultConfig = listTableConfigUseCase.invoke()
            insertDownloadUseCase(defaultConfig)
        }
    }

    fun onMenuClicked(idMenu: Int) {
        viewModelScope.launch {
            val pending = checkPendingDataDownloadUseCase().data ?: 0

            when {
                idMenu == FEATURE_DOWNLOAD -> {
                    _navEvent.send(
                        HomeNavEvent.Navigate(RouteDestination.HomeToDownload)
                    )
                }

                idMenu == FEATURE_SETTING -> {
                    _navEvent.send(
                        HomeNavEvent.Navigate(RouteDestination.HomeToSetting)
                    )
                }

                pending == 0 -> {
                    val destination = when (idMenu) {
                        FEATURE_ACTIVITIES -> RouteDestination.HomeToActivities
                        FEATURE_EXPENSES -> RouteDestination.HomeToExpanses
                        FEATURE_VISIT -> RouteDestination.HomeToVisit
                        FEATURE_MASTER_DATA -> RouteDestination.HomeToMaster
                        FEATURE_INVOICE -> RouteDestination.HomeToInvoice
                        else -> null
                    }

                    destination?.let {
                        _navEvent.send(
                            HomeNavEvent.Navigate(it)
                        )
                    }
                }

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

    fun decideBlockSheet(
        isDeveloperMode: Boolean,
        localVersion: String,
    ) {
        viewModelScope.launch {
            if (isDeveloperMode) {
                _blockSheetState.value = BlockSheetType.DEVELOPER_MODE
                return@launch
            }

            val result = fetchVersionAppsUseCase()

            if (result.status != StatusNetwork.SUCCESS) {
                _blockSheetState.value = BlockSheetType.NONE
                return@launch
            }

            val serverVersion = result.data?.data?.versionName

            if (serverVersion != null && serverVersion != localVersion) {
                _blockSheetState.value = BlockSheetType.VERSION_UPDATE
            } else {
                _blockSheetState.value = BlockSheetType.NONE
            }
        }
    }

}