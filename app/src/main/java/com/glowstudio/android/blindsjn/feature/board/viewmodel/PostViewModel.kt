package com.glowstudio.android.blindsjn.feature.board.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glowstudio.android.blindsjn.feature.board.model.*
import com.glowstudio.android.blindsjn.feature.board.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val _selectedPost = MutableStateFlow<Post?>(null)
    val selectedPost: StateFlow<Post?> = _selectedPost

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage

    /**
     * UI 상태 메시지를 설정합니다.
     *
     * @param message 사용자에게 표시할 상태 또는 오류 메시지.
     */
    fun setStatusMessage(message: String) {
        _statusMessage.value = message
    }

    /**
     * 모든 게시글 목록을 비동기적으로 불러와 상태를 갱신합니다.
     *
     * 네트워크 요청이 성공하면 게시글 리스트 상태를 업데이트하며,
     * 실패하거나 예외가 발생할 경우 상태 메시지에 오류 내용을 설정합니다.
     */
    fun loadPosts() {
        viewModelScope.launch {
            try {
                val response = PostRepository.loadPosts()
                if (response.isSuccessful) {
                    response.body()?.let { postListResponse ->
                        _posts.value = postListResponse.data
                    }
                } else {
                    _statusMessage.value = "불러오기 실패: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "에러: ${e.message}"
            }
        }
    }

    /**
     * 지정한 ID의 게시글을 비동기적으로 조회하여 선택된 게시글 상태를 갱신합니다.
     *
     * 조회에 실패하거나 예외가 발생하면 상태 메시지에 오류 내용을 설정합니다.
     *
     * @param postId 조회할 게시글의 고유 ID
     */
    fun loadPostById(postId: Int) {
        viewModelScope.launch {
            try {
                val response = PostRepository.loadPostById(postId)
                if (response.isSuccessful) {
                    response.body()?.let { postDetailResponse ->
                        _selectedPost.value = postDetailResponse.data
                    }
                } else {
                    _statusMessage.value = "게시글 조회 실패: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "게시글 조회 에러: ${e.message}"
            }
        }
    }

    /**
     * 새로운 게시글을 저장하고 상태 메시지를 갱신합니다.
     *
     * 게시글 저장이 성공하면 상태 메시지를 업데이트하고 게시글 목록을 다시 불러옵니다.
     * 실패 시 상태 메시지에 오류 내용을 설정합니다.
     *
     * @param title 게시글 제목
     * @param content 게시글 내용
     * @param userId 작성자 ID
     * @param industry 업종 정보
     */
    fun savePost(title: String, content: String, userId: Int, industry: String) {
        viewModelScope.launch {
            try {
                val postRequest = PostRequest(title, content, userId, industry)
                val response = PostRepository.savePost(postRequest)
                if (response.isSuccessful) {
                    _statusMessage.value = response.body()?.message
                    loadPosts()
                } else {
                    _statusMessage.value = "저장 실패: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "에러 발생: ${e.message}"
            }
        }
    }

    /**
     * 지정한 게시글의 제목과 내용을 수정합니다.
     *
     * 수정이 성공하면 상태 메시지를 갱신하고 해당 게시글 정보를 다시 불러옵니다.
     * 실패하거나 예외가 발생하면 상태 메시지에 오류 내용을 설정합니다.
     *
     * @param postId 수정할 게시글의 ID
     * @param title 새 제목
     * @param content 새 내용
     */
    fun editPost(postId: Int, title: String, content: String) {
        viewModelScope.launch {
            try {
                val editRequest = EditPostRequest(postId, title, content)
                val response = PostRepository.editPost(editRequest)
                if (response.isSuccessful) {
                    _statusMessage.value = response.body()?.message
                    loadPostById(postId)
                } else {
                    _statusMessage.value = "수정 실패: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "에러 발생: ${e.message}"
            }
        }
    }

    /**
     * 지정한 게시글을 삭제하고 상태 메시지를 갱신합니다.
     *
     * @param postId 삭제할 게시글의 ID
     */
    fun deletePost(postId: Int) {
        viewModelScope.launch {
            try {
                val deleteRequest = DeleteRequest(postId)
                val response = PostRepository.deletePost(deleteRequest)
                if (response.isSuccessful) {
                    _statusMessage.value = response.body()?.message
                    loadPosts()
                } else {
                    _statusMessage.value = "삭제 실패: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "에러 발생: ${e.message}"
            }
        }
    }

    /**
     * 지정한 게시글의 좋아요 수를 1 증가시킵니다.
     *
     * @param postId 좋아요 수를 증가시킬 게시글의 ID
     */
    fun incrementLike(postId: Int) {
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) {
                post.copy(likeCount = post.likeCount + 1)
            } else {
                post
            }
        }
    }
} 