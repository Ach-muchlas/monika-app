package com.sss.monikaapps.feature.invoice.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomLoadingView
import com.sss.monikaapps.common.component.CustomNotFoundAnimation
import com.sss.monikaapps.common.component.CustomSearch
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.feature.invoice.presentation.component.CardListInvoice
import org.koin.androidx.compose.koinViewModel

@Composable
fun InvoiceListScreen(
    navController: NavController,
    viewModel: InvoiceViewModel = koinViewModel(),
) {

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val result by viewModel.customerInvoice.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLayout)
            .padding(Dimens.MediumMargin)
    ) {
        CustomTopBar(
            title = stringResource(R.string.list_invoice),
            showRightIcon = true,
            iconRight = R.drawable.icon_sync,
            iconSize = 30,
            onBackClick = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(Dimens.MediumMargin))

        CustomSearch(
            query = searchQuery,
            onQueryChange = viewModel::onSearchQueryChanged
        )

        Spacer(modifier = Modifier.height(Dimens.MediumMargin))

        when (result.status) {
            StatusNetwork.LOADING -> {
                CustomLoadingView()
            }

            StatusNetwork.SUCCESS -> {
                val invoices = result.data.orEmpty()
                if (invoices.isEmpty()) {
                    CustomNotFoundAnimation()
                } else {
                    LazyColumn {
                        items(invoices) { item ->
                            CardListInvoice(data = item) {}
                        }
                    }
                }
            }

            StatusNetwork.ERROR -> {
                CustomNotFoundAnimation()
            }
        }
    }

}