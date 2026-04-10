package com.sss.monikaapps.feature.update_data.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.R
import com.sss.monikaapps.common.constanta.UpdateFeatureConstant.UPDATE_DATA_INVOICE
import com.sss.monikaapps.common.navigation.RouteDestination
import com.sss.monikaapps.feature.home.data.model.HomeMenuItem
import com.sss.monikaapps.feature.home.data.sealed.HomeNavEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class UpdateDataViewModel(
    private val app: Application,
) : ViewModel() {
    private val _menuItems = MutableStateFlow(createMenuBasedOnData())
    val menuItems: StateFlow<List<HomeMenuItem>> = _menuItems

    private val _navEvent = Channel<HomeNavEvent>(Channel.BUFFERED)
    val navEvent = _navEvent.receiveAsFlow()

    private fun createMenuBasedOnData(): List<HomeMenuItem> {
        val items = mutableListOf<HomeMenuItem>()

        items.add(
            HomeMenuItem(
                UPDATE_DATA_INVOICE,
                app.getString(R.string.text_feature_invoice),
                app.getString(R.string.text_desc_feature_update_invoice),
                R.drawable.icon_activity
            )
        )

        return items
    }


    fun onMenuClicked(idMenu: Int) {
        viewModelScope.launch {
            val destination = when (idMenu) {
                UPDATE_DATA_INVOICE -> RouteDestination.UpdateDataToUpdateInvoice
                else -> null
            }

            Log.e("CHECK_NAV", "Navigation : $destination")
            destination?.let {
                _navEvent.send(HomeNavEvent.Navigate(it))
            }
        }
    }
}
