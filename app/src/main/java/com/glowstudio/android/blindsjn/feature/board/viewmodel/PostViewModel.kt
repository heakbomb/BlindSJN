package com.glowstudio.android.blindsjn.feature.board.viewmodel

import android.util.Log
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

    private val _reportResult = MutableStateFlow<String?>(null)
    val reportResult: StateFlow<String?> = _reportResult

    fun setStatusMessage(message: String) {
        _statusMessage.value = message
    }

    fun loadPosts() {
        viewModelScope.launch {
            try {
                val response = PostRepository.loadPosts()
                if (response.isSuccessful) {
                    response.body()?.let { postListResponse ->
                        // 현재 선택된 게시글의 좋아요 상태 유지
                        val currentSelectedPost = _selectedPost.value
                        _posts.value = postListResponse.data.map { post ->
                            if (currentSelectedPost?.id == post.id) {
                                post.copy(
                                    isLiked = currentSelectedPost.isLiked,
                                    likeCount = currentSelectedPost.likeCount
                                )
                            } else {
                                post
                            }
                        }
                    }
                } else {
                    _statusMessage.value = "불러오기 실패: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "에러: ${e.message}"
            }
        }
    }

    fun loadPostById(postId: Int) {
        viewModelScope.launch {
            try {
                val response = PostRepository.loadPostById(postId)
                if (response.isSuccessful) {
                    response.body()?.let { postDetailResponse ->
                        // 게시글 목록에서 해당 게시글 찾기
                        val existingPost = _posts.value.find { it.id == postId }
                        // 기존 게시글의 좋아요 상태 유지
                        postDetailResponse.data?.let { newPost ->
                            val updatedPost = newPost.copy(
                                isLiked = existingPost?.isLiked ?: newPost.isLiked,
                                likeCount = existingPost?.likeCount ?: newPost.likeCount
                            )
                            _selectedPost.value = updatedPost
                            
                            // 게시글 목록도 업데이트
                            _posts.value = _posts.value.map { post ->
                                if (post.id == postId) updatedPost else post
                            }
                        }
                    }
                } else {
                    _statusMessage.value = "게시글 조회 실패: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "게시글 조회 에러: ${e.message}"
            }
        }
    }

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

    fun incrementLike(postId: Int) {
        viewModelScope.launch {
            try {
                // 먼저 UI 업데이트
                _posts.value = _posts.value.map { post ->
                    if (post.id == postId) {
                        post.copy(
                            isLiked = true,
                            likeCount = (post.likeCountInt + 1).toString()
                        )
                    } else {
                        post
                    }
                }
                
                _selectedPost.value?.let { currentPost ->
                    if (currentPost.id == postId) {
                        _selectedPost.value = currentPost.copy(
                            isLiked = true,
                            likeCount = (currentPost.likeCountInt + 1).toString()
                        )
                    }
                }

                // 서버 요청
                val request = LikePostRequest(post_id = postId, user_id = 1)
                val response = PostRepository.likePost(request)
                
                if (!response.isSuccessful || response.body()?.status != "success") {
                    // 실패 시 원래 상태로 복구
                    _posts.value = _posts.value.map { post ->
                        if (post.id == postId) {
                            post.copy(
                                isLiked = false,
                                likeCount = (post.likeCountInt - 1).toString()
                            )
                        } else {
                            post
                        }
                    }
                    
                    _selectedPost.value?.let { currentPost ->
                        if (currentPost.id == postId) {
                            _selectedPost.value = currentPost.copy(
                                isLiked = false,
                                likeCount = (currentPost.likeCountInt - 1).toString()
                            )
                        }
                    }
                    _statusMessage.value = "좋아요 추가 실패: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "좋아요 추가 중 오류: ${e.message}"
                Log.e("PostViewModel", "좋아요 추가 오류", e)
            }
        }
    }

    fun decrementLike(postId: Int) {
        viewModelScope.launch {
            try {
                // 먼저 UI 업데이트
                _posts.value = _posts.value.map { post ->
                    if (post.id == postId) {
                        post.copy(
                            isLiked = false,
                            likeCount = (post.likeCountInt - 1).toString()
                        )
                    } else {
                        post
                    }
                }
                
                _selectedPost.value?.let { currentPost ->
                    if (currentPost.id == postId) {
                        _selectedPost.value = currentPost.copy(
                            isLiked = false,
                            likeCount = (currentPost.likeCountInt - 1).toString()
                        )
                    }
                }

                // 서버 요청
                val request = LikePostRequest(post_id = postId, user_id = 1)
                val response = PostRepository.likePost(request)
                
                if (!response.isSuccessful || response.body()?.status != "success") {
                    // 실패 시 원래 상태로 복구
                    _posts.value = _posts.value.map { post ->
                        if (post.id == postId) {
                            post.copy(
                                isLiked = true,
                                likeCount = (post.likeCountInt + 1).toString()
                            )
                        } else {
                            post
                        }
                    }
                    
                    _selectedPost.value?.let { currentPost ->
                        if (currentPost.id == postId) {
                            _selectedPost.value = currentPost.copy(
                                isLiked = true,
                                likeCount = (currentPost.likeCountInt + 1).toString()
                            )
                        }
                    }
                    _statusMessage.value = "좋아요 취소 실패: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "좋아요 취소 중 오류: ${e.message}"
                Log.e("PostViewModel", "좋아요 취소 오류", e)
            }
        }
    }

    fun toggleLike(postId: Int, userId: Int, onResult: (Boolean, Boolean, Int) -> Unit) {
        viewModelScope.launch {
            try {
                val currentPost = _selectedPost.value
                if (currentPost?.isLiked == true) {
                    decrementLike(postId)
                    onResult(true, false, currentPost.likeCountInt - 1)
                } else {
                    incrementLike(postId)
                    onResult(true, true, currentPost?.likeCountInt?.plus(1) ?: 1)
                }
            } catch (e: Exception) {
                _statusMessage.value = "좋아요 상태 변경 중 오류: ${e.message}"
                Log.e("PostViewModel", "좋아요 토글 오류", e)
                onResult(false, _selectedPost.value?.isLiked ?: false, _selectedPost.value?.likeCountInt ?: 0)
            }
        }
    }

    fun reportPost(postId: Int, userId: Int, reason: String) {
        viewModelScope.launch {
            try {
                val request = ReportRequest(postId, userId, reason)
                val response = PostRepository.reportPost(request)
                if (response.isSuccessful) {
                    response.body()?.let { reportResponse ->
                        if (reportResponse.success) {
                            _reportResult.value = reportResponse.message ?: "신고가 접수되었습니다."
                        } else {
                            _reportResult.value = reportResponse.error ?: "신고 접수 실패"
                        }
                    } ?: run {
                        _reportResult.value = "서버 응답이 비어있습니다."
                    }
                } else {
                    _reportResult.value = "서버 오류: ${response.code()}"
                }
            } catch (e: Exception) {
                _reportResult.value = "신고 중 오류 발생: ${e.message}"
            }
        }
    }

    fun clearReportResult() {
        _reportResult.value = null
    }
} 