package com.glowstudio.android.blindsjn.model

// 네 가지 탭 모두 표시되도록 POPULAR 추가
enum class BoardCategory(val displayName: String, val route: String) {
    INDUSTRY("직종게시판", "jobBoard"),
    FREE("자유게시판",   "freeBoard"),
    QUESTION("질문게시판","qnaBoard"),
    POPULAR("인기게시판",  "popularBoard")
}
