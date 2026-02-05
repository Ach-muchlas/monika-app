package com.sss.monikaapps.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import com.sss.monikaapps.common.theme.BodyPopMedium
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> CustomDropdownTextField(
    label: String,
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedItem: T?,
    enabled: Boolean = true,
    onItemSelected: (T) -> Unit,
    itemText: (T) -> String,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (enabled) {
                expanded = !expanded
            }
        },
        modifier = modifier
            .alpha(if (enabled) 1f else 0.9f)
    ) {

        CustomTextField(
            value = selectedItem?.let { itemText(it) } ?: "",
            onValueChange = {},
            hint = label,
            readOnly = true,
            onClick = {
                if (enabled) expanded = true
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded && enabled,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            itemText(item),
                            style = BodyPopMedium,
                        )
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    },
                    colors =
                        MenuDefaults.itemColors( // TAMBAHKAN: Paksa warna item
                        textColor = Color.Black,
                        leadingIconColor = Color.Black,
                        trailingIconColor = Color.Black
                    )
                )
            }
        }
    }
}