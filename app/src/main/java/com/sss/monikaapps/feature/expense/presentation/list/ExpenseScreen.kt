package com.sss.monikaapps.feature.expense.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomFilterChip
import com.sss.monikaapps.common.component.CustomFloatingActionButton
import com.sss.monikaapps.common.component.CustomSearch
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.model.dataStatusExpanses
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.feature.expense.presentation.list.component.ExpanseList
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExpansesScreen(
    navController: NavController,
    viewModel: ExpensesViewModel = koinViewModel(),
    onClick: (trno: String) -> Unit,
    onClickToAddExpanse: () -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf("7") }

    val lazyPagingItems = viewModel.expansesResult.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLayout)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLayout)
                .padding(Dimens.MediumMargin)
        ) {

            CustomTopBar(
                title = stringResource(R.string.list_expanses), onBackClick = {
                    navController.popBackStack()
                })

            Spacer(modifier = Modifier.height(Dimens.MediumMargin))

            CustomSearch(
                query = searchQuery, onQueryChange = { searchQuery = it })
            Spacer(modifier = Modifier.height(Dimens.SmallMargin))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(Dimens.ExtraExtraSmallCornerRadius)
            ) {
                items(dataStatusExpanses) { item ->
                    CustomFilterChip(
                        text = item.title, selected = selected == item.id, onClick = {
                            selected = item.id
                            viewModel.setStatus(item.id.toInt())
                        })
                }
            }

            Spacer(modifier = Modifier.height(Dimens.MediumMargin))

            // Handle loading initial
            if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                ExpanseList(
                    lazyPagingItems = lazyPagingItems, onClick = onClick
                )
            }

            // Handle error
            HandlePagingExpanseError(lazyPagingItems.loadState)
        }

        CustomFloatingActionButton(
            onClick = onClickToAddExpanse, modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 20.dp, bottom = 20.dp
                )
        )
    }
}
