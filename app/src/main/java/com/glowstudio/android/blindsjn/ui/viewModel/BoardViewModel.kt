// BoardViewModel.kt
package com.glowstudio.android.blindsjn.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glowstudio.android.blindsjn.model.Post
import com.glowstudio.android.blindsjn.model.User
import com.glowstudio.android.blindsjn.network.InternalServer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BoardViewModel : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val _user = MutableStateFlow(
        User(userId = 0, phoneNumber = "", certifiedIndustries = emptyList())
    )
    val user: StateFlow<User> = _user

    init {
        loadPosts()
        loadUser()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            try {
                val resp = InternalServer.api.getAllPosts()
                if (resp.isSuccessful) {
                    _posts.value = resp.body()?.data ?: emptyList()
                }
            } catch (_: Exception) { /* 에러 처리 */ }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            try {
                // TODO: API 연동 후 실제 인증된 업종 리스트로 교체할 것
                _user.value = User(
                    userId = 1,
                    phoneNumber = "010-1234-5678",
                    certifiedIndustries = emptyList()
                )
            } catch (_: Exception) { /* 에러 처리 */ }
        }
    }
}
