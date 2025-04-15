package com.glowstudio.android.blindsjn.repository

import com.glowstudio.android.blindsjn.model.BasicResponse
import com.glowstudio.android.blindsjn.model.PostListResponse
import com.glowstudio.android.blindsjn.network.*
import retrofit2.Response

object PostRepository {

    suspend fun savePost(request: PostRequest): Response<BasicResponse> {
        return InternalServer.api.savePost(request)
    }

    suspend fun loadPosts(): Response<PostListResponse> {
        return InternalServer.api.getAllPosts()
    }

    suspend fun editPost(request: EditPostRequest): Response<BasicResponse> {
        return InternalServer.api.editPost(request)
    }

    suspend fun deletePost(request: DeleteRequest): Response<BasicResponse> {
        return InternalServer.api.deletePost(request)
    }

    suspend fun saveComment(request: CommentRequest): Response<BasicResponse> {
        return InternalServer.api.saveComment(request)
    }

    suspend fun editComment(request: EditCommentRequest): Response<BasicResponse> {
        return InternalServer.api.editComment(request)
    }

    suspend fun deleteComment(request: DeleteCommentRequest): Response<BasicResponse> {
        return InternalServer.api.deleteComment(request)
    }
}
