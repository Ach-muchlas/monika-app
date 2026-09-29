package com.sss.monikaapps.feature.expense.presentation.list.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.sss.monikaapps.common.component.CustomCardListHeader
import com.sss.monikaapps.common.component.CustomNotFoundAnimation
import com.sss.monikaapps.common.formatter.FormatterCurrency.formatCurrency
import com.sss.monikaapps.common.formatter.FormatterDate.formatDateToIndoDisplay
import com.sss.monikaapps.common.model.dataStatusExpanses
import com.sss.monikaapps.feature.expense.data.response.DataItemExpenses
@Composable
fun ExpanseList(
    lazyPagingItems: LazyPagingItems<DataItemExpenses>,
    onClick: (trno: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {

        // LIST
        LazyColumn {
            items(lazyPagingItems.itemCount) { index ->
                lazyPagingItems[index]?.let { data ->
                    ExpansesListItem(
                        data = data,
                        onClick = { onClick(data.trno.toString()) }
                    )
                }
            }
            
            item {
                PagingLoadingItem(loadState = lazyPagingItems.loadState.append)
            }
        }
        if (
            lazyPagingItems.itemCount == 0 &&
            lazyPagingItems.loadState.refresh is LoadState.NotLoading
        ) {
            CustomNotFoundAnimation(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


@Composable
fun ExpansesListItem(
    data: DataItemExpenses,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val status = dataStatusExpanses.firstOrNull {
            it.id == data.isStatus
        }

        CustomCardListHeader(
            title = formatCurrency(amount = data.netAmount?.toLong() ?: 0),
            subtitle = formatDateToIndoDisplay(data.date.toString()),
            showNote = true,
            note = data.note.orEmpty().ifBlank { "-" },
            status = status,
            sizeIcon = 60.dp,
            onClick = onClick
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun PagingLoadingItem(
    loadState: LoadState,
    modifier: Modifier = Modifier,
) {
    when (loadState) {
        is LoadState.Loading -> {
            CustomNotFoundAnimation()
        }

        else -> Unit
    }
}