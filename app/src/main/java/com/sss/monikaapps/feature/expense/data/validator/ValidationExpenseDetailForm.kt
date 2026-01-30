package com.sss.monikaapps.feature.expense.data.validator

import com.sss.monikaapps.feature.mastering.data.response.DataItemMasteringExpense

data class ValidationExpenseDetailForm(
    val categoryError: String? = null,
    val netAmountError: String? = null,
    val photoError: String? = null,
    val initialKilometerError: String? = null,
    val finalKilometerError: String? = null,
) {
    val hasError: Boolean
        get() = categoryError != null ||
                netAmountError != null ||
                photoError != null ||
                initialKilometerError != null ||
                finalKilometerError != null
}

fun validateInputExpenseDetail(
    isEditMode: Boolean,
    selectedCategory: DataItemMasteringExpense?, // misal model category
    netAmount: String,
    photos: List<String>,
    initialKilometer: String,
    finalKilometer: String,
): ValidationExpenseDetailForm {

    var categoryError: String? = null
    var netAmountError: String? = null
    var photoError: String? = null
    var initialKilometerError: String? = null
    var finalKilometerError: String? = null

    // Validasi kategori
    if (!isEditMode) {
        if (selectedCategory?.id.isNullOrBlank()) {
            categoryError = "Kategori pengeluaran tidak boleh kosong"
        }
    }

    if (netAmount.isBlank()) {
        netAmountError = "Jumlah pengeluaran tidak boleh kosong"
    }

    // Validasi foto
    if (selectedCategory?.name == "BBM" && photos.size < 2) {
        photoError = "Minimal 2 foto harus ditambahkan. Foto nota dan Foto Kilometer"
    } else if (selectedCategory?.isRequiredFoto == "1" && photos.isEmpty()) {
        photoError = "Minimal 1 foto harus ditambahkan"
    }

    // Validasi kilometer untuk BBM
    if (selectedCategory?.name == "BBM") {
        if (initialKilometer.toIntOrNull() == null || initialKilometer == "0") {
            initialKilometerError = "Kilometer awal tidak boleh 0"
        }
        if (finalKilometer.toIntOrNull() == null || finalKilometer == "0") {
            finalKilometerError = "Kilometer akhir tidak boleh 0"
        }
    }

    return ValidationExpenseDetailForm(
        categoryError = categoryError,
        netAmountError = netAmountError,
        photoError = photoError,
        initialKilometerError = initialKilometerError,
        finalKilometerError = finalKilometerError
    )
}
