package com.sss.monikaapps.feature.expense.presentation.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sss.monikaapps.common.component.CustomLoadingDialog
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.component.PhotoPickerBottomSheet
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_EXPENSES
import com.sss.monikaapps.common.constanta.NameFeatureConstant.EXPENSE
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.helper.GenerateRandomTextHelper.generateRandomId
import com.sss.monikaapps.common.helper.rememberPhotoPickerWithCompress
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.viewmodel.PhotoViewModel
import com.sss.monikaapps.feature.expense.domain.model.ExpenseDetailRequest
import com.sss.monikaapps.feature.expense.domain.model.ExpenseUpdateDetailRequest
import com.sss.monikaapps.feature.expense.presentation.create.component.ExpenseDetailForm
import org.koin.androidx.compose.koinViewModel
import java.io.File

@Composable
fun CreateAndUpdateExpenseDetailScreen(
    navController: NavController,
    trno: String,
    dataIdDetail: String? = null,
    dataInitialKm: String? = null,
    dataFinalKm: String? = null,
    dataNetAmount: String? = null,
    dataNote: String? = null,
    photoViewModel: PhotoViewModel = koinViewModel(),
    viewModel: ExpenseCreateAndUpdateViewModel = koinViewModel(),
) {
    val isEditMode = dataIdDetail != null

    var netAmount by rememberSaveable { mutableStateOf(dataNetAmount ?: "") }
    var note by rememberSaveable { mutableStateOf(dataNote ?: "") }

    val initialKilometer by viewModel.initialKm.collectAsState()
    val finalKilometer by viewModel.finalKm.collectAsState()

    val parentIdPhoto = remember {
        if (isEditMode) "${trno}_${dataIdDetail}"
        else "${trno}_${generateRandomId()}"
    }

    val photos by photoViewModel.observePhotos(parentIdPhoto, FEATURE_EXPENSES.toString())
        .collectAsState()

    val categories by viewModel.expenseCategories.observeAsState(emptyList())
    var showPhotoSheet by remember { mutableStateOf(false) }

    val selectedCategory by viewModel.selectedExpenseCategory.observeAsState()
    val createDetailResult by viewModel.createDetailExpenseResult.observeAsState()
    val updateDetailResult by viewModel.updateDetailExpenseResult.observeAsState()

    val (openCamera, openGallery) =
        rememberPhotoPickerWithCompress(
            feature = EXPENSE,
            typeFeature = FEATURE_EXPENSES.toString()
        ) { newPhotoPath ->
            photoViewModel.addPhoto(
                parentId = parentIdPhoto,
                parentType = FEATURE_EXPENSES.toString(),
                parentFeature = FEATURE_EXPENSES,
                path = newPhotoPath
            )
        }


    LaunchedEffect(Unit) {
        viewModel.fetchExpenseCategories()
    }

    LaunchedEffect(categories) {
        if (isEditMode && selectedCategory == null && dataIdDetail != null) {
            viewModel.setSelectedCategoryById(dataIdDetail)

            viewModel.setInitialKm(dataInitialKm ?: "0")
            viewModel.setFinalKm(dataFinalKm ?: "0")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLayout)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(Dimens.MediumMargin)
                .imePadding()
        ) {
            CustomTopBar(
                title = "Buat Detail Pengeluaran", onBackClick = { navController.popBackStack() })

            Spacer(modifier = Modifier.height(Dimens.MediumMargin))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                ExpenseDetailForm(
                    isEditMode = isEditMode,
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { viewModel.selectExpenseCategory(it) },

                    netAmount = netAmount,
                    onNetAmountChange = { netAmount = it },

                    note = note,
                    onNoteChange = { note = it },

                    initialKilometer = initialKilometer,
                    onInitialKilometerChange = { viewModel.setInitialKm(it) },

                    finalKilometer = finalKilometer,
                    onFinalKilometerChange = { viewModel.setFinalKm(it) },

                    photos = photos.map { it.filePath },

                    onAddPhoto = { showPhotoSheet = true },

                    onDeletePhoto = { path ->
                        photos.firstOrNull { it.filePath == path }?.let {
                            photoViewModel.deletePhoto(it)
                        }
                    },

                    onSubmit = {
                        val isBBM = selectedCategory?.name == "BBM"

                        val safeInitialKm = if (isBBM) initialKilometer else "0"
                        val safeFinalKm = if (isBBM) finalKilometer else "0"

                        val categoryId = viewModel.getSelectedExpenseCategoryId()

                        val payloadCreate = ExpenseDetailRequest(
                            idCategoryExpense = categoryId.toString(),
                            netAmount = netAmount,
                            initialKilometer = safeInitialKm,
                            finalKilometer = safeFinalKm,
                            note = note,
                            photos = photos.map { File(it.filePath) }
                        )

                        val payloadEdit = ExpenseUpdateDetailRequest(
                            netAmount = netAmount,
                            note = note,
                            initialKilometer = safeInitialKm,
                            finalKilometer = safeFinalKm,
                            photos = photos.map { File(it.filePath) }
                        )

                        if (isEditMode) {
                            viewModel.updateExpenseDetail(
                                trno,
                                dataIdDetail.toString(),
                                payloadEdit
                            )
                        } else {
                            viewModel.createExpenseDetail(trno, payloadCreate)
                        }
                    }
                )
            }
        }

        PhotoPickerBottomSheet(
            show = showPhotoSheet,
            onDismiss = { showPhotoSheet = false },
            onCamera = {
                showPhotoSheet = false
                openCamera()
            },
            onGallery = {
                showPhotoSheet = false
                openGallery()
            }
        )

        (createDetailResult ?: updateDetailResult)?.let { result ->
            when (result.status) {
                StatusNetwork.LOADING ->
                    CustomLoadingDialog("Mengirim data ke server")

                StatusNetwork.SUCCESS -> {
                    LaunchedEffect(result) {
                        SnackbarManager.showSnackbar(
                            SnackbarData(
                                result.data?.message.orEmpty(),
                                SnackbarType.SUCCESS
                            )
                        )
                        viewModel.clearDetailCreateState()
                        viewModel.clearDetailUpdateState()
                        navController.popBackStack()
                    }
                }

                StatusNetwork.ERROR -> {
                    LaunchedEffect(result) {
                        SnackbarManager.showSnackbar(
                            SnackbarData(
                                result.message ?: "Terjadi kesalahan",
                                SnackbarType.ERROR
                            )
                        )
                        viewModel.clearDetailCreateState()
                        viewModel.clearDetailUpdateState()
                    }
                }
            }
        }
    }
}
