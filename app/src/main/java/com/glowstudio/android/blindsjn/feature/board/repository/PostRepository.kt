package com.glowstudio.android.blindsjn.feature.board.repository

import com.glowstudio.android.blindsjn.data.model.BasicResponse
import com.glowstudio.android.blindsjn.feature.board.model.*
import retrofit2.Response
import com.glowstudio.android.blindsjn.feature.board.model.PostRequest
import com.glowstudio.android.blindsjn.feature.board.model.EditPostRequest
import com.glowstudio.android.blindsjn.feature.board.model.DeleteRequest
import com.glowstudio.android.blindsjn.feature.board.model.CommentRequest
import com.glowstudio.android.blindsjn.feature.board.model.EditCommentRequest
import com.glowstudio.android.blindsjn.feature.board.model.DeleteCommentRequest
import com.glowstudio.android.blindsjn.data.network.InternalServer

object PostRepository {

    suspend fun savePost(request: PostRequest): Response<BasicResponse> {
        return InternalServer.api.savePost(request)
    }

    /**
     * 모든 게시글 목록을 서버에서 비동기적으로 가져옵니다.
     *
     * @return 게시글 목록이 포함된 서버 응답 객체
     */
    suspend fun loadPosts(): Response<PostListResponse> {
        return InternalServer.api.getAllPosts()
    }

    /**
     * 지정된 ID의 게시글 상세 정보를 가져옵니다.
     *
     * @param postId 조회할 게시글의 고유 ID
     * @return 게시글 상세 정보를 담은 Retrofit Response 객체
     */
    suspend fun loadPostById(postId: Int): Response<PostDetailResponse> {
        return InternalServer.api.getPostById(postId)
    }

    /**
     * 게시글을 수정하는 요청을 서버에 전송합니다.
     *
     * @param request 수정할 게시글의 정보가 담긴 요청 객체
     * @return 수정 결과를 포함하는 서버 응답
     */
    suspend fun editPost(request: EditPostRequest): Response<BasicResponse> {
        return InternalServer.api.editPost(request)
    }

    /**
     * 게시글 삭제 요청을 서버에 전송합니다.
     *
     * @param request 삭제할 게시글의 정보를 담은 요청 객체
     * @return 삭제 결과를 포함하는 응답 객체
     */
    suspend fun deletePost(request: DeleteRequest): Response<BasicResponse> {
        return InternalServer.api.deletePost(request)
    }

    /**
     * 지정된 게시글의 댓글 목록을 조회합니다.
     *
     * @param postId 댓글을 조회할 게시글의 ID
     * @return 댓글 목록이 포함된 응답 객체
     */
    suspend fun loadComments(postId: Int): Response<CommentListResponse> {
        return InternalServer.api.getComments(postId)
    }

    /**
     * 새로운 댓글을 저장하는 요청을 서버에 전송합니다.
     *
     * @param request 저장할 댓글의 정보를 담은 요청 객체
     * @return 저장 결과를 포함하는 서버 응답
     */
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
