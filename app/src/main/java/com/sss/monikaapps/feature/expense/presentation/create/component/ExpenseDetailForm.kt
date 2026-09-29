package com.sss.monikaapps.feature.expense.presentation.create.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.common.component.CustomDropdownTextField
import com.sss.monikaapps.common.component.CustomPrimaryButton
import com.sss.monikaapps.common.component.CustomTextField
import com.sss.monikaapps.common.helper.ThousandsSeparatorTransformationHelper
import com.sss.monikaapps.common.theme.BodyBitterBold
import com.sss.monikaapps.common.theme.BodyPopBold
import com.sss.monikaapps.common.theme.BodyPopRegular
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.LightGray
import com.sss.monikaapps.feature.expense.data.validator.validateInputExpenseDetail
import com.sss.monikaapps.feature.mastering.data.response.DataItemMasteringExpense
import com.sss.monikaapps.feature.photo.CustomMultiPhotoCard

@Composable
fun ExpenseDetailForm(
    isEditMode: Boolean,
    categories: List<DataItemMasteringExpense>,
    selectedCategory: DataItemMasteringExpense?,
    onCategorySelected: (DataItemMasteringExpense) -> Unit,
    netAmount: String,
    onNetAmountChange: (String) -> Unit,
    note: String,
    onNoteChange: (String) -> Unit,
    initialKilometer: String,
    onInitialKilometerChange: (String) -> Unit,
    finalKilometer: String,
    onFinalKilometerChange: (String) -> Unit,
    photos: List<String>,
    onAddPhoto: () -> Unit,
    onDeletePhoto: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    var categoryError by remember { mutableStateOf<String?>(null) }
    var netAmountError by remember { mutableStateOf<String?>(null) }
    var initialKilometerError by remember { mutableStateOf<String?>(null) }
    var finalKilometerError by remember { mutableStateOf<String?>(null) }
    var photoError by remember { mutableStateOf<String?>(null) }
    val isBBM = selectedCategory?.name == "BBM"
    val isOutTownMeal = selectedCategory?.name == "Uang Makan Luar Kota"

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val textTitlePhoto = when (selectedCategory?.name) {
        "BBM" -> "Foto Km Dan Foto Nota *"
        "Entertain" -> "Foto Bukti *"
        else -> "Foto Nota *"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.SmallMargin),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Dimens.ExtraSmallMargin))

        Text(
            text = "Form Detail Pengeluaran",
            style = BodyBitterBold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Dimens.ExtraLargeMargin))

        if (!isEditMode) {
            Text(
                text = "Kategori Pengeluaran *",
                style = BodyPopBold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

            CustomDropdownTextField(
                label = "Pilih Kategori Pengeluaran",
                items = categories,
                selectedItem = selectedCategory,
                onItemSelected = {
                    categoryError = null
                    onCategorySelected(it)
                },
                itemText = { it.name ?: "-" })

            categoryError?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    style = BodyPopRegular,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                )
            }
        } else {
            Text(
                text = "Kategori Pengeluaran *",
                style = BodyPopBold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

            CustomTextField(
                value = selectedCategory?.name.toString(),
                onValueChange = {},
                backgroundColor = LightGray,
                hint = "Kategori Pengeluaran",
                readOnly = true
            )
        }

        if (isBBM) {
            Spacer(Modifier.height(Dimens.MediumMargin))

            Text(
                text = "Kilometer Awal *", style = BodyPopBold, modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

            CustomTextField(
                value = initialKilometer,
                onValueChange = { newValue ->
                    val clean = newValue.filter { it.isDigit() }
                    initialKilometerError = null
                    onInitialKilometerChange(clean)
                },
                hint = "Masukan kilomter awal",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = ThousandsSeparatorTransformationHelper(),
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )

            initialKilometerError?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    style = BodyPopRegular,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                )
            }

            Spacer(Modifier.height(Dimens.MediumMargin))

            Text(
                text = "Kilometer Akhir *", style = BodyPopBold, modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

            CustomTextField(
                value = finalKilometer,
                onValueChange = { newValue ->
                    val clean = newValue.filter { it.isDigit() }
                    finalKilometerError = null
                    onFinalKilometerChange(clean)
                },
                hint = "Masukan kilomter akhir",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = ThousandsSeparatorTransformationHelper(),
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )

            finalKilometerError?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    style = BodyPopRegular,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                )
            }
        }

        Spacer(Modifier.height(Dimens.MediumMargin))

        Text(
            text = "Jumlah Pengeluaran *", style = BodyPopBold, modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

        CustomTextField(
            value = netAmount,
            onValueChange = { newValue ->
                val clean = newValue.filter { it.isDigit() }
                netAmountError = null
                onNetAmountChange(clean)
            },
            hint = "Masukkan jumlah pengeluaran",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = ThousandsSeparatorTransformationHelper(),
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        )

        netAmountError?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                style = BodyPopRegular,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp)
            )
        }

        Spacer(Modifier.height(Dimens.MediumMargin))

        Text(
            text = "Catatan", style = BodyPopBold, modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

        CustomTextField(
            value = note, onValueChange = {
                onNoteChange(it)
            }, modifier = Modifier.height(80.dp), isMultiline = true, hint = "Masukan catatan"
        )

        Spacer(Modifier.height(Dimens.MediumMargin))

        if (!isOutTownMeal) {
            Text(
                text = textTitlePhoto,
                style = BodyPopBold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

            CustomMultiPhotoCard(
                photos = photos,
                title = "Masukkan foto ",
                onAddPhoto = {
                    focusManager.clearFocus(force = true)
                    keyboardController?.hide()
                    photoError = null
                    onAddPhoto()
                }, onDeletePhoto = onDeletePhoto
            )
            photoError?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    style = BodyPopRegular,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                )
            }
        }

        Spacer(Modifier.height(Dimens.ExtraExtraLargeMargin))

        CustomPrimaryButton(
            modifier = Modifier.fillMaxWidth(), text = "Simpan", onClick = {
                val errors = validateInputExpenseDetail(
                    isEditMode = isEditMode,
                    selectedCategory = selectedCategory,
                    netAmount = netAmount,
                    photos = photos,
                    initialKilometer = initialKilometer,
                    finalKilometer = finalKilometer
                )

                categoryError = errors.categoryError
                netAmountError = errors.netAmountError
                photoError = errors.photoError
                initialKilometerError = errors.initialKilometerError
                finalKilometerError = errors.finalKilometerError

                if (!errors.hasError) {
                    onSubmit()
                }
            })


        Spacer(Modifier.height(Dimens.LargeMargin))
    }
}