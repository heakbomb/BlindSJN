package com.glowstudio.android.blindsjn.network

/**
 * URL 서버 통신 객체
 *
 *
 **/

import com.glowstudio.android.blindsjn.network.ApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import retrofit2.http.GET
import retrofit2.http.Query

// 뉴스 기사 데이터 모델
data class NewsArticle(
    val title: String,
    val description: String?,
    val author: String?, // 추가
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String? // 추가
)

// 뉴스 API 응답 모델
data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsArticle>
)

// 뉴스 API Retrofit 인터페이스
interface NewsApiService {
    @GET("v2/everything")
    suspend fun getEverything(
        @Query("q") query: String,
        @Query("from") from: String,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>
}

// 뉴스 데이터를 가져오는 Repository 예시
class NewsRepository {
    suspend fun fetchEverything(query: String, from: String, sortBy: String, apiKey: String): Response<NewsResponse> {
        return NewsServer.apiService.getEverything(query, from, sortBy, apiKey)
    }
}

/**
 *
 *
 *
 *
 **/
// Retrofit 인스턴스를 생성하는 싱글톤 객체
object NewsServer {
    private const val BASE_URL = "https://newsapi.org/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val apiService: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }
}

// 내부 서버
object InternalServer {

    private const val BASE_URL = "http://wonrdc.duckdns.org/"

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