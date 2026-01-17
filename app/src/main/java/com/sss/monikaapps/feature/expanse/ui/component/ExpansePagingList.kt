package com.sss.monikaapps.feature.expanse.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.sss.monikaapps.common.model.dataStatusExpanses
import com.sss.monikaapps.feature.expanse.data.response.DataItemExpanses
import com.sss.monikaapps.common.component.CustomCardListHeader
import com.sss.monikaapps.common.formatter.FormatterDate
import com.sss.monikaapps.utils.formatter.FormatterCurrency.formatCurrency

@Composable
fun ExpanseList(
    lazyPagingItems: LazyPagingItems<DataItemExpanses>,
    onClick: (trno: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        // Items
        items(
            count = lazyPagingItems.itemCount
        ) { index ->
            lazyPagingItems[index]?.let { data ->
                ExpansesListItem(
                    data = data,
                    onClick = { onClick(data.trno.toString()) }
                )
            }
        }

        // Loading pagination
        item {
            PagingLoadingItem(loadState = lazyPagingItems.loadState.append)
        }

        // Empty state
        if (lazyPagingItems.itemCount == 0 &&
            lazyPagingItems.loadState.refresh is LoadState.NotLoading
        ) {
            item {
                EmptyStateItem()
            }
        }
    }
}

@Composable
fun ExpansesListItem(
    data: DataItemExpanses,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val status = dataStatusExpanses.firstOrNull {
            it.id == data.isStatus
        }

        CustomCardListHeader(
            title = formatCurrency(amount = data.netAmount?.toLong() ?: 0),
            subtitle = FormatterDate.formatDateToIndoDisplay(data.date.toString()),
            showNote = true,
            note = data.note.orEmpty().ifBlank { "-" },
            status = status,
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
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        else -> Unit
    }
}

@Composable
fun EmptyStateItem(
    message: String = "Tidak ada aktivitas",
    modifier: Modifier = Modifier,
) {
    Text(
        text = message,
        color = Color.Gray,
        modifier = modifier.padding(16.dp)
    )
}