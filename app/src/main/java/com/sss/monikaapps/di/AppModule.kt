package com.sss.monikaapps.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.android.gms.location.LocationServices
import com.sss.monikaapps.common.manager.ServerManager
import com.sss.monikaapps.common.manager.SessionManager
import com.sss.monikaapps.common.repository.location.LocationRepository
import com.sss.monikaapps.common.repository.location.LocationRepositoryImpl
import com.sss.monikaapps.common.repository.photo.PhotoRepository
import com.sss.monikaapps.common.repository.photo.PhotoRepositoryImpl
import com.sss.monikaapps.common.viewmodel.LocationViewModel
import com.sss.monikaapps.common.viewmodel.PhotoViewModel
import com.sss.monikaapps.database.AppDatabase
import com.sss.monikaapps.database.AppDatabaseManager
import com.sss.monikaapps.feature.activity.data.local.ActivityLocalDataSource
import com.sss.monikaapps.feature.activity.data.local.ActivityLocalDataSourceImpl
import com.sss.monikaapps.feature.activity.data.remote.ActivityRemoteDataSource
import com.sss.monikaapps.feature.activity.data.remote.ActivityRemoteDataSourceImpl
import com.sss.monikaapps.feature.activity.data.validator.ActivityValidator
import com.sss.monikaapps.feature.activity.domain.repository.ActivitiesRepository
import com.sss.monikaapps.feature.activity.domain.repository.ActivitiesRepositoryImpl
import com.sss.monikaapps.feature.activity.domain.usecase.CountDataNotSyncUseCase
import com.sss.monikaapps.feature.activity.domain.usecase.CreateActivityUseCase
import com.sss.monikaapps.feature.activity.domain.usecase.FetchActivitiesUseCase
import com.sss.monikaapps.feature.activity.domain.usecase.FetchDetailActivityUseCase
import com.sss.monikaapps.feature.activity.presentation.ActivitiesViewModel
import com.sss.monikaapps.feature.connection.data.local.ConnectionLocalDataSource
import com.sss.monikaapps.feature.connection.data.local.ConnectionLocalDataSourceImpl
import com.sss.monikaapps.feature.connection.domain.repository.ConnectionRepository
import com.sss.monikaapps.feature.connection.domain.repository.ConnectionRepositoryImpl
import com.sss.monikaapps.feature.connection.domain.usecase.ChangeServerUseCase
import com.sss.monikaapps.feature.connection.domain.usecase.FetchServerUrlUseCase
import com.sss.monikaapps.feature.connection.presentation.ConnectionViewModel
import com.sss.monikaapps.feature.device.domain.repository.DeviceInfoRepository
import com.sss.monikaapps.feature.device.domain.repository.DeviceInfoRepositoryImpl
import com.sss.monikaapps.feature.device.domain.usecase.GetAppVersionUseCase
import com.sss.monikaapps.feature.device.domain.usecase.GetDeviceIdUseCase
import com.sss.monikaapps.feature.device.domain.usecase.GetSystemOperationUseCase
import com.sss.monikaapps.feature.device.presentation.DeviceViewModel
import com.sss.monikaapps.feature.download.data.local.DownloadLocalDataSource
import com.sss.monikaapps.feature.download.data.local.DownloadLocalDataSourceImpl
import com.sss.monikaapps.feature.download.data.remote.DownloadRemoteDataSource
import com.sss.monikaapps.feature.download.data.remote.DownloadRemoteDataSourceImpl
import com.sss.monikaapps.feature.download.domain.repository.DownloadRepository
import com.sss.monikaapps.feature.download.domain.repository.DownloadRepositoryImpl
import com.sss.monikaapps.feature.download.domain.usecase.DownloadUseCase
import com.sss.monikaapps.feature.download.domain.usecase.FetchConfigDownloadUseCase
import com.sss.monikaapps.feature.download.domain.usecase.GetCountInvoiceAndVisitUseCase
import com.sss.monikaapps.feature.download.domain.usecase.InsertDownloadUseCase
import com.sss.monikaapps.feature.download.presentation.DownloadViewModel
import com.sss.monikaapps.feature.expense.data.remote.ExpensesRemoteDataSource
import com.sss.monikaapps.feature.expense.data.remote.ExpensesRemoteDataSourceImpl
import com.sss.monikaapps.feature.expense.domain.repository.ExpensesRepository
import com.sss.monikaapps.feature.expense.domain.repository.ExpensesRepositoryImpl
import com.sss.monikaapps.feature.expense.domain.usecase.CreateExpenseDetailUseCase
import com.sss.monikaapps.feature.expense.domain.usecase.CreateExpenseHeaderUseCase
import com.sss.monikaapps.feature.expense.domain.usecase.DeleteExpenseDetailUseCase
import com.sss.monikaapps.feature.expense.domain.usecase.DeleteExpenseHeaderUseCase
import com.sss.monikaapps.feature.expense.domain.usecase.FetchDetailExpenseUseCase
import com.sss.monikaapps.feature.expense.domain.usecase.FetchExpensesUseCase
import com.sss.monikaapps.feature.expense.domain.usecase.SubmitExpenseUseCase
import com.sss.monikaapps.feature.expense.domain.usecase.UnSubmitExpenseUseCase
import com.sss.monikaapps.feature.expense.domain.usecase.UpdateExpenseDetailUseCase
import com.sss.monikaapps.feature.expense.presentation.create.ExpenseCreateAndUpdateViewModel
import com.sss.monikaapps.feature.expense.presentation.detail.ExpenseDetailViewModel
import com.sss.monikaapps.feature.expense.presentation.list.ExpensesViewModel
import com.sss.monikaapps.feature.home.domain.usecase.CheckPendingDataDownloadUseCase
import com.sss.monikaapps.feature.home.domain.usecase.GetCountInvoicePendingUseCase
import com.sss.monikaapps.feature.home.domain.usecase.ListTableConfigUseCase
import com.sss.monikaapps.feature.home.presentasi.HomeViewModel
import com.sss.monikaapps.feature.invoice.data.local.InvoiceLocalDataSource
import com.sss.monikaapps.feature.invoice.data.local.InvoiceLocalDataSourceImpl
import com.sss.monikaapps.feature.invoice.data.remote.InvoiceRemoteDataSource
import com.sss.monikaapps.feature.invoice.data.remote.InvoiceRemoteDataSourceImpl
import com.sss.monikaapps.feature.invoice.domain.repository.InvoiceRepository
import com.sss.monikaapps.feature.invoice.domain.repository.InvoiceRepositoryImpl
import com.sss.monikaapps.feature.invoice.domain.usecase.FetchBankReceiptUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.GetDataGeneratePdfUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.GetDetailInvoiceUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.GetListCustomerInvoiceUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.GetPaymentDataInvoiceUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.GetReasonUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.SubmitPaymentInvoiceUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.SyncManualInvoiceUseCase
import com.sss.monikaapps.feature.invoice.presentation.detail.DetailInvoiceViewModel
import com.sss.monikaapps.feature.invoice.presentation.generate_pdf.GeneratePdfViewModel
import com.sss.monikaapps.feature.invoice.presentation.list.InvoiceListViewModel
import com.sss.monikaapps.feature.invoice.presentation.payment.PaymentInvoiceViewModel
import com.sss.monikaapps.feature.login.data.remote.AuthRemoteDataSource
import com.sss.monikaapps.feature.login.data.remote.AuthRemoteDataSourceImpl
import com.sss.monikaapps.feature.login.domain.repository.AuthRepository
import com.sss.monikaapps.feature.login.domain.repository.AuthRepositoryImpl
import com.sss.monikaapps.feature.login.domain.usecase.LoginUseCase
import com.sss.monikaapps.feature.login.presentation.AuthViewModel
import com.sss.monikaapps.feature.mastering.data.remote.MasteringRemoteSource
import com.sss.monikaapps.feature.mastering.data.remote.MasteringRemoteSourceImpl
import com.sss.monikaapps.feature.mastering.domain.repository.MasteringRepository
import com.sss.monikaapps.feature.mastering.domain.repository.MasteringRepositoryImpl
import com.sss.monikaapps.feature.mastering.domain.usecase.FetchMasteringExpenseUseCase
import com.sss.monikaapps.feature.mastering.presentation.MasteringViewModel
import com.sss.monikaapps.feature.result_download.data.remote.BackupRemoteDataSource
import com.sss.monikaapps.feature.result_download.data.remote.BackupRemoteDataSourceImpl
import com.sss.monikaapps.feature.result_download.domain.repository.BackupRemoteRepository
import com.sss.monikaapps.feature.result_download.domain.repository.BackupRemoteRepositoryImpl
import com.sss.monikaapps.feature.result_download.domain.usecase.SendEmailUseCase
import com.sss.monikaapps.feature.result_download.presentation.ResultDownloadViewModel
import com.sss.monikaapps.feature.update_data.presentation.UpdateDataViewModel
import com.sss.monikaapps.feature.update_data_invoice.domain.repository.UpdateDataInvoiceRepository
import com.sss.monikaapps.feature.update_data_invoice.domain.repository.UpdateDataInvoiceRepositoryImpl
import com.sss.monikaapps.feature.update_data_invoice.domain.usecase.UpdateDataInvoiceUseCase
import com.sss.monikaapps.feature.update_data_invoice.presentation.UpdateDataInvoiceViewModel
import com.sss.monikaapps.feature.version_check.data.remote.VersionRemoteDataSource
import com.sss.monikaapps.feature.version_check.data.remote.VersionRemoteDataSourceImpl
import com.sss.monikaapps.feature.version_check.domain.repository.VersionRepository
import com.sss.monikaapps.feature.version_check.domain.repository.VersionRepositoryImpl
import com.sss.monikaapps.feature.version_check.domain.usecase.FetchVersionAppsUseCase
import com.sss.monikaapps.feature.version_check.persentation.VersionViewModel
import com.sss.monikaapps.feature.visit.data.local.VisitLocalDataSource
import com.sss.monikaapps.feature.visit.data.local.VisitLocalDataSourceImpl
import com.sss.monikaapps.feature.visit.data.remote.VisitRemoteDataSource
import com.sss.monikaapps.feature.visit.data.remote.VisitRemoteDataSourceImpl
import com.sss.monikaapps.feature.visit.domain.repository.VisitRepository
import com.sss.monikaapps.feature.visit.domain.repository.VisitRepositoryImpl
import com.sss.monikaapps.feature.visit.domain.usecase.CheckInVisitUseCase
import com.sss.monikaapps.feature.visit.domain.usecase.CheckOutVisitUseCase
import com.sss.monikaapps.feature.visit.domain.usecase.FetchVisitDetailUseCase
import com.sss.monikaapps.feature.visit.domain.usecase.FetchVisitLocalDatabaseUseCase
import com.sss.monikaapps.feature.visit.domain.usecase.SyncManualVisitUseCase
import com.sss.monikaapps.feature.visit.presentation.detail.VisitDetailViewModel
import com.sss.monikaapps.feature.visit.presentation.list.VisitViewModel
import com.sss.monikaapps.feature.visit.presentation.update.UpdateVisitViewModel
import com.sss.monikaapps.network.ApiConfig
import com.sss.monikaapps.network.domain.NetworkChecker
import com.sss.monikaapps.network.domain.NetworkCheckerImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val apiModule = module {
        single { ApiConfig.getApiService() }
    }

    val databaseModule = module {
        single {
            Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.DB_NAME)
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .fallbackToDestructiveMigration(false)
                .build().also { AppDatabaseManager.setDatabase(it) }
        }
        single { get<AppDatabase>().photoDao() }
        single { get<AppDatabase>().visitDao() }
        single { get<AppDatabase>().configDownload() }
        single { get<AppDatabase>().activityDao() }
        single { get<AppDatabase>().invoiceDao() }
        single { get<AppDatabase>().bankReceiptDao() }
    }

    val repositoryModule = module {

        single {
            LocationServices.getFusedLocationProviderClient(androidContext())
        }
        single { SessionManager.getInstance() }

        single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
        single<ActivitiesRepository> { ActivitiesRepositoryImpl(get(), get(), get(), get()) }
        single<ExpensesRepository> { ExpensesRepositoryImpl(get()) }
        single<MasteringRepository> { MasteringRepositoryImpl(get()) }
        single<DownloadRepository> {
            DownloadRepositoryImpl(androidContext(), get(), get(), get(), get())
        }
        single<VisitRepository> { VisitRepositoryImpl(get(), get()) }
        single<ConnectionRepository> { ConnectionRepositoryImpl(get(), get()) }
        single<BackupRemoteRepository> { BackupRemoteRepositoryImpl(get()) }
        single<VersionRepository> { VersionRepositoryImpl(get()) }
        single<InvoiceRepository> { InvoiceRepositoryImpl(get(), get(), get()) }
        single<UpdateDataInvoiceRepository> { UpdateDataInvoiceRepositoryImpl(get(), get(), get()) }

        single<PhotoRepository> { PhotoRepositoryImpl(get()) }
        single<LocationRepository> { LocationRepositoryImpl(get()) }
        single<DeviceInfoRepository> { DeviceInfoRepositoryImpl(androidContext()) }
    }

    val network = module {
        single { NetworkCheckerImpl(androidContext()) }
    }

    val dataSource = module {
        single { ServerManager.getInstance() }

        single<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(get()) }
        single<ActivityRemoteDataSource> { ActivityRemoteDataSourceImpl(get()) }
        single<ActivityLocalDataSource> { ActivityLocalDataSourceImpl(get(), get()) }
        single<ExpensesRemoteDataSource> { ExpensesRemoteDataSourceImpl(get()) }
        single<MasteringRemoteSource> { MasteringRemoteSourceImpl(get()) }
        single<DownloadLocalDataSource> { DownloadLocalDataSourceImpl(get(), get(), get(), get()) }
        single<DownloadRemoteDataSource> { DownloadRemoteDataSourceImpl(get()) }
        single<VisitLocalDataSource> { VisitLocalDataSourceImpl(get(), get()) }
        single<VisitRemoteDataSource> { VisitRemoteDataSourceImpl(get()) }
        single<InvoiceLocalDataSource> { InvoiceLocalDataSourceImpl(get(), get()) }
        single<InvoiceRemoteDataSource> { InvoiceRemoteDataSourceImpl(get()) }

        single<ConnectionLocalDataSource> { ConnectionLocalDataSourceImpl(get()) }
        single<VersionRemoteDataSource> { VersionRemoteDataSourceImpl(get()) }
        single<BackupRemoteDataSource> { BackupRemoteDataSourceImpl(get()) }
    }

    val useCase = module {
        single<NetworkChecker> { NetworkCheckerImpl(androidContext()) }

        single { LoginUseCase(get()) }
        single { ActivityValidator(get()) }
        single { CreateActivityUseCase(get(), get(), get()) }
        single { FetchActivitiesUseCase(get()) }
        single { FetchDetailActivityUseCase(get()) }
        single { FetchExpensesUseCase(get()) }
        single { FetchDetailExpenseUseCase(get()) }
        single { GetAppVersionUseCase(get()) }
        single { GetDeviceIdUseCase(get()) }
        single { GetSystemOperationUseCase(get()) }
        single { CreateExpenseHeaderUseCase(get()) }
        single { CreateExpenseDetailUseCase(get()) }
        single { UpdateExpenseDetailUseCase(get()) }
        single { FetchMasteringExpenseUseCase(get()) }
        single { SubmitExpenseUseCase(get()) }
        single { UnSubmitExpenseUseCase(get()) }
        single { DeleteExpenseHeaderUseCase(get()) }
        single { DeleteExpenseDetailUseCase(get()) }
        single { DownloadUseCase(get()) }
        single { FetchConfigDownloadUseCase(get()) }
        single { FetchVisitLocalDatabaseUseCase(get()) }
        single { FetchVisitDetailUseCase(get()) }
        single { CheckInVisitUseCase(get(), get()) }
        single { CheckOutVisitUseCase(get(), get()) }
        single { InsertDownloadUseCase(get()) }
        single { ChangeServerUseCase(get()) }
        single { FetchServerUrlUseCase(get()) }
        single { SendEmailUseCase(get()) }
        single { SyncManualVisitUseCase(get(), get()) }
        single { CheckPendingDataDownloadUseCase(get()) }
        single { FetchVersionAppsUseCase(get()) }
        single { ListTableConfigUseCase(get()) }
        single { GetCountInvoiceAndVisitUseCase(get()) }
        single { GetListCustomerInvoiceUseCase(get()) }
        single { GetDetailInvoiceUseCase(get()) }
        single { GetPaymentDataInvoiceUseCase(get()) }
        single { GetReasonUseCase(get()) }
        single { GetCountInvoicePendingUseCase(get()) }
        single { SubmitPaymentInvoiceUseCase(get(), androidContext()) }
        single { SyncManualInvoiceUseCase(get(), get()) }
        single { GetDataGeneratePdfUseCase(get()) }
        single { CountDataNotSyncUseCase(get()) }
        single { UpdateDataInvoiceUseCase(get()) }
        single { FetchBankReceiptUseCase(get()) }
    }

    val viewModelModule = module {
        viewModel { AuthViewModel(get(), get(), get()) }
        viewModel { ActivitiesViewModel(get(), get(), get()) }
        viewModel { ExpensesViewModel(get()) }
        viewModel { ExpenseDetailViewModel(get(), get(), get(), get(), get()) }
        viewModel { ExpenseCreateAndUpdateViewModel(get(), get(), get(), get()) }
        viewModel { HomeViewModel(androidApplication(), get(), get(), get(), get(), get(), get()) }
        viewModel { PhotoViewModel(get()) }
        viewModel { LocationViewModel(get()) }
        viewModel { MasteringViewModel(get()) }
        viewModel { DownloadViewModel(get(), get(), get(), get(), get(), get(), get()) }
        viewModel { VisitViewModel(get()) }
        viewModel { VisitDetailViewModel(get()) }
        viewModel { UpdateVisitViewModel(get(), get(), get()) }
        viewModel { ConnectionViewModel(get(), get()) }
        viewModel { DeviceViewModel(get(), get(), get()) }
        viewModel { ResultDownloadViewModel(get(), get()) }
        viewModel { VersionViewModel() }
        viewModel { InvoiceListViewModel(get(), get(), get()) }
        viewModel { DetailInvoiceViewModel(get(), get()) }
        viewModel { PaymentInvoiceViewModel(get(), get(), get(), get(), get()) }
        viewModel { GeneratePdfViewModel(get(), get(), get()) }
        viewModel { UpdateDataViewModel(get()) }
        viewModel { UpdateDataInvoiceViewModel(get(), get()) }
    }
}