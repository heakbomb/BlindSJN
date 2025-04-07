//package com.glowstudio.android.blindsjn.network
//
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.GET
//import retrofit2.http.Query
//
//// 뉴스 기사 데이터 모델
//data class NewsArticle1(
//    val title: String,
//    val description: String?,
//    val url: String?,
//    val urlToImage: String?
//)
//
//// 뉴스 API 응답 모델
//data class NewsResponse1(
//    val status: String,
//    val totalResults: Int,
//    val articles: List<NewsArticle>
//)
//
//// 뉴스 API Retrofit 인터페이스
//interface NewsApiService1 {
//    @GET("v2/everything")
//    suspend fun getEverything(
//        @Query("q") query: String,
//        @Query("from") from: String,
//        @Query("sortBy") sortBy: String,
//        @Query("apiKey") apiKey: String
//    ): Response<NewsResponse>
//}
//
//// Retrofit 인스턴스를 생성하는 싱글톤 객체
//object NewsApiClient1 {
//    private const val BASE_URL = "https://newsapi.org/"
//
//    val apiService: NewsApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(NewsApiService::class.java)
//    }
//}
//
//// 뉴스 데이터를 가져오는 Repository 예시
//class NewsRepository1 {
//    suspend fun fetchEverything(query: String, from: String, sortBy: String, apiKey: String): Response<NewsResponse> {
//        return NewsApiClient.apiService.getEverything(query, from, sortBy, apiKey)
//    }
//}