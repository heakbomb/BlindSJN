package com.glowstudio.android.blindsjn.data.network

/**
 * URL 서버 통신 객체
 *
 *
 **/

import com.glowstudio.android.blindsjn.data.network.ApiService
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

// 네이버 뉴스 서버용 Retrofit 인스턴스
object NaverNewsServer {
    private const val BASE_URL = "https://openapi.naver.com/"

    private val client = OkHttpClient.Builder()
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

    val apiService: NaverNewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NaverNewsApiService::class.java)
    }
}

// 내부 서버
object InternalServer {

    private const val BASE_URL = "http://wonrdc.iptime.org/"

    private val client by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create())) // ← 중요!
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

// 공공 API 서버용 Retrofit 인스턴스
object PublicApiRetrofitInstance {
    private const val BASE_URL = "https://api.odcloud.kr/api/" // 공공 API URL

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: BusinessApiService by lazy {
        retrofit.create(BusinessApiService::class.java)
    }
}

// OCR API 서버용 Retrofit 인스턴스
object OcrApiServer {
    private const val BASE_URL = "https://d7cblqkw1o.apigw.ntruss.com/custom/v1/42447/02392618f64f8a5d3fd1004abc08eb2e9cd4a9eba2e80c21b5ccdf7fa7b73df4/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val apiService: com.glowstudio.android.blindsjn.feature.ocr.network.OcrApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(com.glowstudio.android.blindsjn.feature.ocr.network.OcrApiService::class.java)
    }
}