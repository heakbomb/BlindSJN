package com.glowstudio.android.blindsjn.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glowstudio.android.blindsjn.model.*
import com.glowstudio.android.blindsjn.network.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val _popularPosts = MutableStateFlow<List<Post>>(emptyList())
    val popularPosts: StateFlow<List<Post>> = _popularPosts

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    private val _selectedPost = MutableStateFlow<Post?>(null)
    val selectedPost: StateFlow<Post?> = _selectedPost

    fun setStatusMessage(message: String) {
        _statusMessage.value = message
    }

    fun loadPosts() {
        viewModelScope.launch {
            try {
                val response = InternalServer.api.getAllPosts()
                if (response.isSuccessful) {
                    _posts.value = response.body()?.data ?: emptyList()
                } else {
                    _statusMessage.value = "불러오기 실패: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "에러: ${e.message}"
            }
        }
    }

    fun loadPopularPosts() {
        viewModelScope.launch {
            try {
                val response = InternalServer.api.getPopularPosts()
                if (response.isSuccessful) {
                    _popularPosts.value = response.body()?.data ?: emptyList()
                } else {
                    _statusMessage.value = "인기글 불러오기 실패: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "에러: ${e.message}"
            }
        }
    }

    fun savePost(title: String, content: String, userId: Int, industry: String) {
        viewModelScope.launch {
            try {
                val response = InternalServer.api.savePost(PostRequest(title, content, userId, industry))
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

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            try {
                val response = InternalServer.api.deletePost(DeleteRequest(postId))
                _statusMessage.value = if (response.isSuccessful) {
                    loadPosts()
                    response.body()?.message
                } else {
                    "삭제 실패: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "삭제 오류: ${e.message}"
            }
        }
    }

    fun editPost(id: Int, title: String, content: String) {
        viewModelScope.launch {
            try {
                val response = InternalServer.api.editPost(EditPostRequest(id, title, content))
                _statusMessage.value = if (response.isSuccessful) {
                    loadPosts()
                    response.body()?.message
                } else {
                    "수정 실패: ${response.message()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "수정 오류: ${e.message}"
            }
        }
    }

    fun loadPostById(postId: Int) {
        viewModelScope.launch {
            try {
                val response = InternalServer.api.getPostById(postId)
                val postData = response.body()?.data
                if (postData != null) {
                    _selectedPost.value = postData
                } else {
                    _statusMessage.value = "게시글 데이터가 없습니다."
                }
            } catch (e: Exception) {
                _statusMessage.value = "게시글 조회 에러: ${e.message}"
            }
        }
    }



    fun loadComments(postId: Int) {
        viewModelScope.launch {
            try {
                val response = InternalServer.api.getComments(postId)
                if (response.isSuccessful) {
                    _comments.value = response.body()?.data ?: emptyList()
                } else {
                    _statusMessage.value = "댓글 불러오기 실패"
                }
            } catch (e: Exception) {
                _statusMessage.value = "댓글 에러: ${e.message}"
            }
        }
    }

    fun saveComment(postId: Int, userId: Int, content: String) {
        viewModelScope.launch {
            try {
                val response = InternalServer.api.saveComment(
                    com.glowstudio.android.blindsjn.network.CommentRequest(postId, userId, content)
                )
                _statusMessage.value = response.body()?.message
                loadComments(postId)
            } catch (e: Exception) {
                _statusMessage.value = "댓글 저장 오류: ${e.message}"
            }
        }
    }

    fun deleteComment(commentId: Int, postId: Int) {
        viewModelScope.launch {
            try {
                val response = InternalServer.api.deleteComment(
                    com.glowstudio.android.blindsjn.network.DeleteCommentRequest(commentId)
                )
                _statusMessage.value = response.body()?.message
                loadComments(postId)
            } catch (e: Exception) {
                _statusMessage.value = "댓글 삭제 실패: ${e.message}"
            }
        }
    }

    fun editComment(commentId: Int, content: String, postId: Int) {
        viewModelScope.launch {
            try {
                val response = InternalServer.api.editComment(
                    com.glowstudio.android.blindsjn.network.EditCommentRequest(commentId, content)
                )
                _statusMessage.value = response.body()?.message
                loadComments(postId)
            } catch (e: Exception) {
                _statusMessage.value = "댓글 수정 오류: ${e.message}"
            }
        }
    }
}
