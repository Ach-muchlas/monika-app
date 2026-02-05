package com.sss.monikaapps.network

import com.sss.monikaapps.common.constanta.ApiConstant.APP_VERSION
import com.sss.monikaapps.common.constanta.ApiConstant.AUTH
import com.sss.monikaapps.common.constanta.ApiConstant.CHECK_DATA_DOWNLOAD_VISIT
import com.sss.monikaapps.common.constanta.ApiConstant.CHECK_IN_ACTIVITY
import com.sss.monikaapps.common.constanta.ApiConstant.CHECK_IN_VISIT
import com.sss.monikaapps.common.constanta.ApiConstant.CHECK_OUT_ACTIVITY
import com.sss.monikaapps.common.constanta.ApiConstant.CHECK_OUT_VISIT
import com.sss.monikaapps.common.constanta.ApiConstant.CREATE_DETAIL_EXPENSE
import com.sss.monikaapps.common.constanta.ApiConstant.CREATE_HEADER_EXPENSE
import com.sss.monikaapps.common.constanta.ApiConstant.DELETE_DETAIL_EXPENSE
import com.sss.monikaapps.common.constanta.ApiConstant.DELETE_HEADER_EXPENSE
import com.sss.monikaapps.common.constanta.ApiConstant.FETCH_DATA_ACTIVITIES
import com.sss.monikaapps.common.constanta.ApiConstant.FETCH_DATA_EXPENSES
import com.sss.monikaapps.common.constanta.ApiConstant.FETCH_DETAIL_ACTIVITY
import com.sss.monikaapps.common.constanta.ApiConstant.FETCH_DETAIL_EXPENSE
import com.sss.monikaapps.common.constanta.ApiConstant.FETCH_DOWNLOAD_VISIT
import com.sss.monikaapps.common.constanta.ApiConstant.FETCH_MASTERING_EXPENSE
import com.sss.monikaapps.common.constanta.ApiConstant.SEND_EMAIL
import com.sss.monikaapps.common.constanta.ApiConstant.SUBMIT_EXPENSE
import com.sss.monikaapps.common.constanta.ApiConstant.UN_SUBMIT_EXPENSE
import com.sss.monikaapps.common.constanta.ApiConstant.UPDATE_DETAIL_EXPENSE
import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.feature.activity.data.response.ActivitiesResponse
import com.sss.monikaapps.feature.activity.data.response.DetailActivityResponse
import com.sss.monikaapps.feature.connection.data.response.VersionResponse
import com.sss.monikaapps.feature.expense.data.response.DetailExpanseResponse
import com.sss.monikaapps.feature.expense.data.response.ExpansesResponse
import com.sss.monikaapps.feature.login.data.response.LoginResponse
import com.sss.monikaapps.feature.mastering.data.response.MasteringExpenseResponse
import com.sss.monikaapps.feature.visit.data.response.VisitDownloadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // user login
    @FormUrlEncoded
    @POST(AUTH)
    suspend fun loginUser(
        @Field("employee_id") employeeId: String,
        @Field("imei") imei: String,
        @Field("app_version") appVersion: String,
    ): Response<LoginResponse>

    @GET(APP_VERSION)
    suspend fun fetchAppVersion(): Response<VersionResponse>

    // fetch data activities
    @GET("${FETCH_DATA_ACTIVITIES}/{tanggal}")
    suspend fun fetchDataActivities(
        @Path("tanggal") date: String,
    ): Response<ActivitiesResponse>

    @GET("${FETCH_DETAIL_ACTIVITY}/{trno}")
    suspend fun fetchDetailActivity(
        @Path("trno") trno: String,
    ): Response<DetailActivityResponse>

    // fetch data expanses
    @GET("${FETCH_DATA_EXPENSES}/{status}")
    suspend fun fetchDataExpanses(
        @Path("status") status: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Response<ExpansesResponse>

    @GET("${FETCH_DETAIL_EXPENSE}/{trno}")
    suspend fun fetchDetailExpanse(
        @Path("trno") trno: String,
    ): Response<DetailExpanseResponse>

    @POST("${SUBMIT_EXPENSE}/{trno}")
    suspend fun submitExpense(
        @Path("trno") trno: String,
    ): Response<DefaultAddResponse>

    @POST("${UN_SUBMIT_EXPENSE}/{trno}")
    suspend fun unSubmitExpense(
        @Path("trno") trno: String,
    ): Response<DefaultAddResponse>

    // check in
    @Multipart
    @POST(CHECK_IN_ACTIVITY)
    suspend fun checkInActivity(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part photos: List<MultipartBody.Part>?,
    ): Response<DefaultAddResponse>

    // check out
    @Multipart
    @POST("${CHECK_OUT_ACTIVITY}/{trno}")
    suspend fun checkOutActivity(
        @Path("trno") trno: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part photos: List<MultipartBody.Part>?,
    ): Response<DefaultAddResponse>

    // tambah pengeluaran header
    @FormUrlEncoded
    @POST(CREATE_HEADER_EXPENSE)
    suspend fun createExpenseHeader(
        @Field("tanggal") date: String,
        @Field("note") note: String,
    ): Response<DefaultAddResponse>

    @DELETE("${DELETE_HEADER_EXPENSE}/{trno}")
    suspend fun deleteExpenseHeader(
        @Path("trno") trno: String,
    ): Response<DefaultAddResponse>

    @Multipart
    @POST("${CREATE_DETAIL_EXPENSE}/{trno}")
    suspend fun createExpenseDetail(
        @Path("trno") trno: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part photos: List<MultipartBody.Part>?,
    ): Response<DefaultAddResponse>

    @Multipart
    @POST("${UPDATE_DETAIL_EXPENSE}/{trno}/{id}")
    suspend fun updateExpenseDetail(
        @Path("trno") trno: String,
        @Path("id") idExpense: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part photos: List<MultipartBody.Part>?,
    ): Response<DefaultAddResponse>

    @DELETE("${DELETE_DETAIL_EXPENSE}/{trno}/{id}")
    suspend fun deleteExpenseDetail(
        @Path("trno") trno: String,
        @Path("id") idDetail: String,
    ): Response<DefaultAddResponse>

    @GET(FETCH_DOWNLOAD_VISIT)
    suspend fun fetchDownloadVisit(): Response<VisitDownloadResponse>

    @GET("${CHECK_DATA_DOWNLOAD_VISIT}/{totalData}")
    suspend fun checkDataDownloadVisit(
        @Path("totalData") totalData: Int,
    ): Response<DefaultAddResponse>

    @Multipart
    @POST(CHECK_IN_VISIT)
    suspend fun checkInVisit(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part photos: List<MultipartBody.Part>?,
    ): Response<DefaultAddResponse>

    @Multipart
    @POST("${CHECK_OUT_VISIT}/{trno}")
    suspend fun checkOutVisit(
        @Path("trno") trno: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part photos: List<MultipartBody.Part>?,
    ): Response<DefaultAddResponse>

    @GET(FETCH_MASTERING_EXPENSE)
    suspend fun fetchMasteringExpense(): Response<MasteringExpenseResponse>

    @Multipart
    @POST(SEND_EMAIL)
    suspend fun sendEmail(
        @Part("reason") reason: RequestBody,
        @Part file: MultipartBody.Part,
    ): Response<DefaultAddResponse>
}