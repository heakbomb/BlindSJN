package com.glowstudio.android.blindsjn.data.network

/**
 * URL 서버 통신 객체
 *
 *
 **/

import com.glowstudio.android.blindsjn.data.model.ApiResponse
import com.glowstudio.android.blindsjn.data.model.BasicResponse
import com.glowstudio.android.blindsjn.data.model.LoginRequest
import com.glowstudio.android.blindsjn.data.model.SignupRequest
import com.glowstudio.android.blindsjn.data.network.ApiService
import com.glowstudio.android.blindsjn.feature.board.model.CommentListResponse
import com.glowstudio.android.blindsjn.feature.board.model.CommentRequest
import com.glowstudio.android.blindsjn.feature.board.model.DeleteCommentRequest
import com.glowstudio.android.blindsjn.feature.board.model.DeleteRequest
import com.glowstudio.android.blindsjn.feature.board.model.EditCommentRequest
import com.glowstudio.android.blindsjn.feature.board.model.EditPostRequest
import com.glowstudio.android.blindsjn.feature.board.model.LikePostRequest
import com.glowstudio.android.blindsjn.feature.board.model.LikeResponse
import com.glowstudio.android.blindsjn.feature.board.model.PostDetailResponse
import com.glowstudio.android.blindsjn.feature.board.model.PostListResponse
import com.glowstudio.android.blindsjn.feature.board.model.PostRequest
import com.glowstudio.android.blindsjn.feature.board.model.Report
import com.glowstudio.android.blindsjn.feature.board.model.ReportRequest
import com.glowstudio.android.blindsjn.feature.board.model.ReportResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.*

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

// InternalServer용 ApiService 정의
interface ApiService {
    @POST("signup.php")
    suspend fun signup(@Body request: SignupRequest): Response<BasicResponse>

    @POST("login.php")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse>

    @GET("Get_post_by_id.php")
    suspend fun getPostById(@Query("post_id") postId: Int): Response<PostDetailResponse>

    @POST("Save_post.php")
    suspend fun savePost(@Body request: PostRequest): Response<BasicResponse>

    @PUT("Edit_post.php")
    suspend fun editPost(@Body request: EditPostRequest): Response<BasicResponse>

    @HTTP(method = "DELETE", path = "Delete_post.php", hasBody = true)
    suspend fun deletePost(@Body request: DeleteRequest): Response<BasicResponse>

    @GET("Load_post.php")
    suspend fun getAllPosts(): Response<PostListResponse>

    @GET("Popular_posts.php")
    suspend fun getPopularPosts(): Response<PostListResponse>

    @GET("Load_comment.php")
    suspend fun getComments(@Query("post_id") postId: Int): Response<CommentListResponse>

    @POST("Save_comment.php")
    suspend fun saveComment(@Body request: CommentRequest): Response<BasicResponse>

    @HTTP(method = "DELETE", path = "Delete_comment.php", hasBody = true)
    suspend fun deleteComment(@Body request: DeleteCommentRequest): Response<BasicResponse>

    @PUT("Edit_comment.php")
    suspend fun editComment(@Body request: EditCommentRequest): Response<BasicResponse>

    @POST("cors.php")
    suspend fun reportPost(@Body reportRequest: ReportRequest): Response<ReportResponse>

    @GET("cors.php")
    suspend fun getReports(): Response<List<Report>>

    @PUT("cors.php")
    suspend fun updateReportStatus(
        @Path("reportId") reportId: Int,
        @Body status: String
    ): Response<ReportResponse>

    @POST("Like_post.php")
    suspend fun likePost(@Body request: LikePostRequest): Response<LikeResponse>
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