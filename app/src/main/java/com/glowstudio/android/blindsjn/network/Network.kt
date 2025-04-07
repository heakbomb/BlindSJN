package com.glowstudio.android.blindsjn.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.glowstudio.android.blindsjn.model.*
import retrofit2.Response
import retrofit2.http.*

// ✅ 요청 데이터 클래스
data class SignupRequest(val phoneNumber: String, val password: String)
data class PostRequest(val title: String, val content: String, val user_id: Int, val industry: String)
data class EditPostRequest(val id: Int, val title: String, val content: String)
data class DeleteRequest(val id: Int)
data class CommentRequest(val post_id: Int, val user_id: Int, val content: String)
data class EditCommentRequest(val id: Int, val content: String)
data class DeleteCommentRequest(val id: Int)
// ✅ 네트워크 상태 확인 함수
fun isNetworkAvailable(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = cm.activeNetwork ?: return false
    val capabilities = cm.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

// ✅ Retrofit API 인터페이스
interface ApiService {

    // 🔹 회원가입 / 로그인
    @POST("signup.php")
    suspend fun signup(@Body request: SignupRequest): Response<BasicResponse>

    @POST("login.php")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse>

    // 🔹 게시글
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

    // 🔹 댓글
    @GET("Load_comment.php")
    suspend fun getComments(@Query("post_id") postId: Int): Response<CommentListResponse>

    @POST("Save_comment.php")
    suspend fun saveComment(@Body request: CommentRequest): Response<BasicResponse>

    @HTTP(method = "DELETE", path = "Delete_comment.php", hasBody = true)
    suspend fun deleteComment(@Body request: DeleteCommentRequest): Response<BasicResponse>

    @PUT("Edit_comment.php")
    suspend fun editComment(@Body request: EditCommentRequest): Response<BasicResponse>
}
