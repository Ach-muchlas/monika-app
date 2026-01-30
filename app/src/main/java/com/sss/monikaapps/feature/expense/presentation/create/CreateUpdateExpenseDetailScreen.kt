package com.sss.monikaapps.feature.expense.presentation.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sss.monikaapps.common.component.CustomLoadingDialog
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_EXPENSES
import com.sss.monikaapps.common.constanta.NameFeatureConstant.EXPENSE
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.helper.GenerateRandomTextHelper.generateRandomId
import com.sss.monikaapps.common.helper.PhotoHelper
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
    dataNetAmount: String? = null,
    dataNote: String? = null,
    photoViewModel: PhotoViewModel = koinViewModel(),
    viewModel: ExpenseCreateAndUpdateViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val isEditMode = dataIdDetail != null

    var netAmount by rememberSaveable {
        mutableStateOf(dataNetAmount ?: "")
    }

    var note by rememberSaveable {
        mutableStateOf(dataNote ?: "")
    }

    var initialKilometer by remember { mutableStateOf("0") }
    var finalKilometer by remember { mutableStateOf("0") }


    val generatedId = rememberSaveable { generateRandomId() }
    val parentIdPhoto = trno + "_" + generatedId

    val photoHelper = remember {
        PhotoHelper(context, EXPENSE, FEATURE_EXPENSES.toString()) { newPhoto ->
            photoViewModel.addPhoto(
                parentId = parentIdPhoto,
                parentType = FEATURE_EXPENSES.toString(),
                parentFeature = FEATURE_EXPENSES,
                path = newPhoto
            )
        }
    }

    val launchCamera = photoHelper.rememberCameraLauncher()

    val photos by photoViewModel.observePhotos(parentIdPhoto, FEATURE_EXPENSES.toString())
        .collectAsState()

    val categories by viewModel.expenseCategories.observeAsState(emptyList())

    val selectedCategory by viewModel.selectedExpenseCategory.observeAsState()
    val createDetailResult by viewModel.createDetailExpenseResult.observeAsState()
    val updateDetailResult by viewModel.updateDetailExpenseResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchExpenseCategories()
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
                    onInitialKilometerChange = { initialKilometer = it },
                    finalKilometer = finalKilometer,
                    onFinalKilometerChange = { finalKilometer = it },

                    photos = photos.map { it.filePath },
                    onAddPhoto = { launchCamera() },
                    onDeletePhoto = { path ->
                        photos.firstOrNull { it.filePath == path }?.let {
                            photoViewModel.deletePhoto(it)
                        }
                    },
                    onSubmit = {
                        val categoryId = viewModel.getSelectedExpenseCategoryId()
                        val payloadCreate = ExpenseDetailRequest(
                            idCategoryExpense = categoryId.toString(),
                            netAmount = netAmount,
                            initialKilometer = initialKilometer,
                            finalKilometer = finalKilometer,
                            note = note,
                            photos = photos.map { File(it.filePath) })

                        val payloadEdit = ExpenseUpdateDetailRequest(
                            netAmount = netAmount,
                            note = note,
                            photos = photos.map { File(it.filePath) })

                        if (isEditMode) viewModel.updateExpenseDetail(
                            trno, dataIdDetail.toString(), payloadEdit
                        ) else viewModel.createExpenseDetail(trno, payloadCreate)
                    })
            }
        }

        createDetailResult?.let { result ->
            when (result.status) {
                StatusNetwork.LOADING -> {
                    CustomLoadingDialog("Loading mengirim data ke server")
                }

                StatusNetwork.SUCCESS -> {
                    LaunchedEffect(result) {
                        SnackbarManager.showSnackbar(
                            SnackbarData(result.data?.message.toString(), SnackbarType.SUCCESS)
                        )
                        navController.popBackStack()
                    }
                }

                StatusNetwork.ERROR -> {
                    LaunchedEffect(result) {
                        SnackbarManager.showSnackbar(
                            SnackbarData(result.message ?: "Terjadi kesalahan", SnackbarType.ERROR)
                        )
                    }
                }
            }
        }

        updateDetailResult?.let { result ->
            when (result.status) {
                StatusNetwork.LOADING -> {
                    CustomLoadingDialog("Loading mengirim data ke server")
                }

                StatusNetwork.SUCCESS -> {
                    LaunchedEffect(result) {
                        SnackbarManager.showSnackbar(
                            SnackbarData(result.data?.message.toString(), SnackbarType.SUCCESS)
                        )
                        navController.popBackStack()
                    }
                }

                StatusNetwork.ERROR -> {
                    LaunchedEffect(result) {
                        SnackbarManager.showSnackbar(
                            SnackbarData(result.message ?: "Terjadi kesalahan", SnackbarType.ERROR)
                        )
                    }
                }
            }
        }

    }
}
