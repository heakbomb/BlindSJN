package com.glowstudio.android.blindsjn.feature.board.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glowstudio.android.blindsjn.feature.board.model.*
import com.glowstudio.android.blindsjn.feature.board.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentViewModel : ViewModel() {
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage

    /**
     * 지정한 게시글의 댓글 목록을 비동기적으로 불러와 상태를 갱신합니다.
     *
     * @param postId 댓글을 조회할 게시글의 ID
     */
    fun loadComments(postId: Int) {
        viewModelScope.launch {
            try {
                val response = PostRepository.loadComments(postId)
                if (response.isSuccessful) {
                    _comments.value = response.body()?.data ?: emptyList()
                } else {
                    _statusMessage.value = "댓글 불러오기 실패: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "댓글 에러: ${e.message}"
            }
        }
    }

    /**
     * 지정된 게시글에 새로운 댓글을 저장하고 상태 메시지를 갱신합니다.
     *
     * 댓글 저장이 성공하면 상태 메시지를 업데이트하고 해당 게시글의 댓글 목록을 다시 불러옵니다.
     * 실패 시 상태 메시지에 오류 내용을 반영합니다.
     *
     * @param postId 댓글을 저장할 게시글의 ID
     * @param userId 댓글 작성자의 사용자 ID
     * @param content 저장할 댓글 내용
     */
    fun saveComment(postId: Int, userId: Int, content: String) {
        viewModelScope.launch {
            try {
                val commentRequest = CommentRequest(postId, userId, content)
                val response = PostRepository.saveComment(commentRequest)
                if (response.isSuccessful) {
                    _statusMessage.value = response.body()?.message
                    loadComments(postId)
                } else {
                    _statusMessage.value = "댓글 저장 실패: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "댓글 저장 오류: ${e.message}"
            }
        }
    }

    /**
     * 지정한 댓글을 수정하고, 성공 시 해당 게시글의 댓글 목록을 새로 고칩니다.
     *
     * @param commentId 수정할 댓글의 ID
     * @param content 변경할 댓글 내용
     */
    fun editComment(commentId: Int, content: String) {
        viewModelScope.launch {
            try {
                val editRequest = EditCommentRequest(commentId, content)
                val response = PostRepository.editComment(editRequest)
                if (response.isSuccessful) {
                    _statusMessage.value = response.body()?.message
                    // 현재 보고 있는 게시글의 댓글 목록을 다시 로드
                    _comments.value.firstOrNull()?.postId?.let { postId ->
                        loadComments(postId)
                    }
                } else {
                    _statusMessage.value = "댓글 수정 실패: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "댓글 수정 오류: ${e.message}"
            }
        }
    }

    /**
     * 지정한 댓글을 삭제하고, 성공 시 해당 게시글의 댓글 목록을 새로 불러옵니다.
     *
     * @param commentId 삭제할 댓글의 ID
     */
    fun deleteComment(commentId: Int) {
        viewModelScope.launch {
            try {
                val deleteRequest = DeleteCommentRequest(commentId)
                val response = PostRepository.deleteComment(deleteRequest)
                if (response.isSuccessful) {
                    _statusMessage.value = response.body()?.message
                    // 현재 보고 있는 게시글의 댓글 목록을 다시 로드
                    _comments.value.firstOrNull()?.postId?.let { postId ->
                        loadComments(postId)
                    }
                } else {
                    _statusMessage.value = "댓글 삭제 실패: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "댓글 삭제 오류: ${e.message}"
            }
        }
    }
}
