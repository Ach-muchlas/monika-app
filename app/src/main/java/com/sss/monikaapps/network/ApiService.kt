package com.sss.monikaapps.network

import com.sss.monikaapps.feature.activity.data.response.ActivitiesResponse
import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.feature.activity.data.response.DetailActivityResponse
import com.sss.monikaapps.feature.expanse.data.response.DetailExpanseResponse
import com.sss.monikaapps.feature.expanse.data.response.ExpansesResponse
import com.sss.monikaapps.feature.login.data.response.LoginResponse
import com.sss.monikaapps.utils.constanta.ApiConstant.AUTH
import com.sss.monikaapps.utils.constanta.ApiConstant.CHECK_IN_ACTIVITY
import com.sss.monikaapps.utils.constanta.ApiConstant.CHECK_OUT_ACTIVITY
import com.sss.monikaapps.utils.constanta.ApiConstant.FETCH_DATA_ACTIVITIES
import com.sss.monikaapps.utils.constanta.ApiConstant.FETCH_DATA_EXPANSES
import com.sss.monikaapps.utils.constanta.ApiConstant.FETCH_DETAIL_ACTIVITY
import com.sss.monikaapps.utils.constanta.ApiConstant.FETCH_DETAIL_EXPANSE
import com.sss.monikaapps.utils.constanta.ApiConstant.FETCH_MASTERING_EXPANSE
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
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
    @GET("${FETCH_DATA_EXPANSES}/{status}")
    suspend fun fetchDataExpanses(
        @Path("status") status: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Response<ExpansesResponse>

    @GET("${FETCH_DETAIL_EXPANSE}/{trno}")
    suspend fun fetchDetailExpanse(
        @Path("trno") trno: String,
    ): Response<DetailExpanseResponse>

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


    @GET(FETCH_MASTERING_EXPANSE)
    suspend fun fetchDetailExpanse(
    ): Response<DetailExpanseResponse>
}