package com.sss.monikaapps.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.android.gms.location.LocationServices
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
import com.sss.monikaapps.feature.activity.domain.usecase.CreateActivityUseCase
import com.sss.monikaapps.feature.activity.domain.usecase.FetchActivitiesUseCase
import com.sss.monikaapps.feature.activity.domain.usecase.FetchDetailActivityUseCase
import com.sss.monikaapps.feature.activity.presentation.ActivitiesViewModel
import com.sss.monikaapps.feature.activity.utils.NetworkChecker
import com.sss.monikaapps.feature.activity.utils.NetworkCheckerImpl
import com.sss.monikaapps.feature.download.data.local.DownloadLocalDataSource
import com.sss.monikaapps.feature.download.data.local.DownloadLocalDataSourceImpl
import com.sss.monikaapps.feature.download.data.remote.DownloadRemoteDataSource
import com.sss.monikaapps.feature.download.data.remote.DownloadRemoteDataSourceImpl
import com.sss.monikaapps.feature.download.domain.repository.DownloadRepository
import com.sss.monikaapps.feature.download.domain.repository.DownloadRepositoryImpl
import com.sss.monikaapps.feature.download.domain.usecase.DownloadUseCase
import com.sss.monikaapps.feature.download.domain.usecase.FetchConfigDownloadUseCase
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
import com.sss.monikaapps.feature.expense.domain.usecase.UpdateExpenseDetailUseCase
import com.sss.monikaapps.feature.expense.presentation.create.ExpenseCreateAndUpdateViewModel
import com.sss.monikaapps.feature.expense.presentation.detail.ExpenseDetailViewModel
import com.sss.monikaapps.feature.expense.presentation.list.ExpensesViewModel
import com.sss.monikaapps.feature.home.presentasi.HomeViewModel
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
import com.sss.monikaapps.feature.utils.device.domain.repository.DeviceInfoRepository
import com.sss.monikaapps.feature.utils.device.domain.repository.DeviceInfoRepositoryImpl
import com.sss.monikaapps.feature.utils.device.domain.usecase.GetAppVersionUseCase
import com.sss.monikaapps.feature.utils.device.domain.usecase.GetDeviceIdUseCase
import com.sss.monikaapps.feature.visit.data.local.VisitLocalDataSource
import com.sss.monikaapps.feature.visit.data.local.VisitLocalDataSourceImpl
import com.sss.monikaapps.feature.visit.domain.repository.VisitRepository
import com.sss.monikaapps.feature.visit.domain.repository.VisitRepositoryImpl
import com.sss.monikaapps.feature.visit.domain.usecase.FetchVisitDetailUseCase
import com.sss.monikaapps.feature.visit.domain.usecase.FetchVisitLocalDatabaseUseCase
import com.sss.monikaapps.feature.visit.presentation.detail.VisitDetailViewModel
import com.sss.monikaapps.feature.visit.presentation.list.VisitViewModel
import com.sss.monikaapps.network.ApiConfig
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val apiModule = module {
        single { ApiConfig.getApiService() }
    }

    val databaseModule = module {
        single {
            Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app_database")
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE).fallbackToDestructiveMigration()
                .build().also { AppDatabaseManager.setDatabase(it) }
        }
        single { get<AppDatabase>().photoDao() }
        single { get<AppDatabase>().visitDao() }
        single { get<AppDatabase>().configDownload() }
        single { get<AppDatabase>().activityDao() }
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
        single<DownloadRepository> { DownloadRepositoryImpl(get(), get()) }
        single<VisitRepository> { VisitRepositoryImpl(get()) }

        single<PhotoRepository> { PhotoRepositoryImpl(get()) }
        single<LocationRepository> { LocationRepositoryImpl(get()) }
        single<DeviceInfoRepository> { DeviceInfoRepositoryImpl(androidContext()) }
    }

    val network = module {
        single { NetworkCheckerImpl(androidContext()) }
    }

    val dataSource = module {
        single<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(get()) }
        single<ActivityRemoteDataSource> { ActivityRemoteDataSourceImpl(get()) }
        single<ActivityLocalDataSource> { ActivityLocalDataSourceImpl(get(), get()) }
        single<ExpensesRemoteDataSource> { ExpensesRemoteDataSourceImpl(get()) }
        single<MasteringRemoteSource> { MasteringRemoteSourceImpl(get()) }
        single<DownloadLocalDataSource> { DownloadLocalDataSourceImpl(get(), get()) }
        single<DownloadRemoteDataSource> { DownloadRemoteDataSourceImpl(get()) }
        single<VisitLocalDataSource> { VisitLocalDataSourceImpl(get()) }
    }

    val useCase = module {
        single { LoginUseCase(get()) }
        single { ActivityValidator(get()) }
        single<NetworkChecker> { NetworkCheckerImpl(androidContext()) }
        single {
            CreateActivityUseCase(
                get<ActivitiesRepository>(), get<ActivityValidator>(), get<NetworkChecker>()
            )
        }
        single { FetchActivitiesUseCase(get()) }
        single { FetchDetailActivityUseCase(get()) }
        single { FetchExpensesUseCase(get()) }
        single { FetchDetailExpenseUseCase(get()) }
        single { GetAppVersionUseCase(get()) }
        single { GetDeviceIdUseCase(get()) }
        single { CreateExpenseHeaderUseCase(get()) }
        single { CreateExpenseDetailUseCase(get()) }
        single { UpdateExpenseDetailUseCase(get()) }
        single { FetchMasteringExpenseUseCase(get()) }
        single { DeleteExpenseHeaderUseCase(get()) }
        single { DeleteExpenseDetailUseCase(get()) }
        single { DownloadUseCase(get()) }
        single { FetchConfigDownloadUseCase(get()) }
        single { FetchVisitLocalDatabaseUseCase(get()) }
        single { FetchVisitDetailUseCase(get()) }
    }

    val viewModelModule = module {
        viewModel { AuthViewModel(get(), get(), get()) }
        viewModel { ActivitiesViewModel(get(), get(), get()) }
        viewModel { ExpensesViewModel(get()) }
        viewModel { ExpenseDetailViewModel(get(), get(), get()) }
        viewModel { ExpenseCreateAndUpdateViewModel(get(), get(), get(), get()) }
        viewModel { HomeViewModel() }
        viewModel { PhotoViewModel(get()) }
        viewModel { LocationViewModel(get()) }
        viewModel { MasteringViewModel(get()) }
        viewModel { DownloadViewModel(get(), get()) }
        viewModel { VisitViewModel(get()) }
        viewModel { VisitDetailViewModel(get()) }
    }
}