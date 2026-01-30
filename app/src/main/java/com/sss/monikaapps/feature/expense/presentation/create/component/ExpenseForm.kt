package com.sss.monikaapps.feature.expense.presentation.create.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomPrimaryButton
import com.sss.monikaapps.common.component.CustomTextField
import com.sss.monikaapps.common.formatter.FormatterDate
import com.sss.monikaapps.common.theme.BodyBitterBold
import com.sss.monikaapps.common.theme.BodyPopBold
import com.sss.monikaapps.common.theme.BodyPopRegular
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Gray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseForm(
    onDateChange: (String) -> Unit,
    note: String,
    onNoteChange: (String) -> Unit,
    onSubmit: () -> Unit = {},
) {
    var selectedDateMillis by remember {
        mutableStateOf<Long?>(null)
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var showDateError by remember { mutableStateOf(false) }

    val formattedDateDisplay = remember(selectedDateMillis) {
        FormatterDate.formatTimestampToIndoDisplay(selectedDateMillis)
    }

    LaunchedEffect(selectedDateMillis) {
        if (selectedDateMillis != null) {
            val dateValue = FormatterDate.formatTimestampToDateString(selectedDateMillis)
            onDateChange(dateValue)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.SmallMargin),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Dimens.ExtraSmallMargin))

        Text(
            text = "Form Transaksi Pengeluaran",
            style = BodyBitterBold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Dimens.ExtraLargeMargin))

        Text(
            text = "Tanggal Transaksi *",
            style = BodyPopBold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

        CustomTextField(
            value = formattedDateDisplay,
            onValueChange = {},
            hint = "Pilih tanggal transaksi",
            readOnly = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Pick date",
                    tint = Gray
                )
            },
            onClick = {
                showDateError = false
                showDatePicker = true
            }
        )

        if (showDateError) {
            Text(
                text = "Tanggal wajib dipilih",
                color = Color.Red,
                style = BodyPopRegular,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp)
            )
        }

        Spacer(Modifier.height(Dimens.MediumMargin))

        Text(
            text = stringResource(R.string.text_description_activity),
            style = BodyPopBold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

        CustomTextField(
            value = note,
            onValueChange = {
                onNoteChange(it)
            },
            modifier = Modifier.height(80.dp),
            isMultiline = true,
            hint = "Masukan Note Pengeluaran"
        )


        Spacer(Modifier.height(Dimens.ExtraExtraLargeMargin))

        CustomPrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Simpan",
            onClick = {
                var hasError = false

                if (selectedDateMillis == null) {
                    showDateError = true
                    hasError = true
                }

                if (!hasError) {
                    onSubmit()
                }
            }
        )

        Spacer(Modifier.height(Dimens.LargeMargin))
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis ?: System.currentTimeMillis(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= System.currentTimeMillis()
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            showDateError = false
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Pilih")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}