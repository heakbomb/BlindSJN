package com.glowstudio.android.blindsjn.data.network

/**
 * URL 서버 통신 객체
 *
 *
 **/

import com.glowstudio.android.blindsjn.data.network.ApiService
import com.glowstudio.android.blindsjn.feature.ocr.api.NaverOcrApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import retrofit2.http.GET
import retrofit2.http.Query

// 네이버 뉴스 응답 모델
data class NaverNewsItem(
    val title: String,
    val originallink: String,
    val link: String,
    val description: String,
    val pubDate: String
)

data class NaverNewsResponse(
    val items: List<NaverNewsItem>
)

interface NaverNewsApiService {
    @GET("v1/search/news.json")
    suspend fun searchNews(
        @Query("query") query: String,
        @Query("display") display: Int = 20,
        @Query("start") start: Int = 1,
        @Query("sort") sort: String = "date"
    ): Response<NaverNewsResponse>
}

// 공통 네트워크 설정
object NetworkConfig {
    const val INTERNAL_BASE_URL = "http://wonrdc.iptime.org/"
    const val NAVER_BASE_URL = "https://openapi.naver.com/"
    const val PUBLIC_API_BASE_URL = "https://api.odcloud.kr/api/"
    const val NAVER_OCR_BASE_URL = "https://d7cblqkw1o.apigw.ntruss.com/custom/v1/42447/02392618f64f8a5d3fd1004abc08eb2e9cd4a9eba2e80c21b5ccdf7fa7b73df4/document/receipt/"

    private val defaultClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    private val naverClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-Naver-Client-Id", "ztMJBFDCJqlNxnax0Hrj")
                .addHeader("X-Naver-Client-Secret", "GrIMlIGxdu")
                .build()
            chain.proceed(request)
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val naverOcrClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-OCR-SECRET", "bmxVU1pnQUhxVFBKWnJlb3RkaE9ybnRId0NkQ3lOQ3Q=")
                .build()
            chain.proceed(request)
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val defaultClientInstance: OkHttpClient
        get() = defaultClient

    val naverClientInstance: OkHttpClient
        get() = naverClient

    val naverOcrClientInstance: OkHttpClient
        get() = naverOcrClient
}

// 네이버 뉴스 서버용 Retrofit 인스턴스
object NaverNewsServer {
    private val retrofit = Retrofit.Builder()
            .baseUrl(NetworkConfig.NAVER_BASE_URL)
        .client(NetworkConfig.naverClientInstance)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val apiService: NaverNewsApiService = retrofit.create(NaverNewsApiService::class.java)
}

// 내부 서버
object InternalServer {
    private val retrofit = Retrofit.Builder()
            .baseUrl(NetworkConfig.INTERNAL_BASE_URL)
        .client(NetworkConfig.defaultClientInstance)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()

    val api: ApiService = retrofit.create(ApiService::class.java)
}

// 공공 API 서버용 Retrofit 인스턴스
object PublicApiRetrofitInstance {
    private val retrofit = Retrofit.Builder()
            .baseUrl(NetworkConfig.PUBLIC_API_BASE_URL)
        .client(NetworkConfig.defaultClientInstance)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val api: ApiService = retrofit.create(ApiService::class.java)
}

// Naver OCR 서버용 Retrofit 인스턴스
object NaverOcrServer {
    private val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConfig.NAVER_OCR_BASE_URL)
        .client(NetworkConfig.naverOcrClientInstance)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: NaverOcrApiService = retrofit.create(NaverOcrApiService::class.java)
}