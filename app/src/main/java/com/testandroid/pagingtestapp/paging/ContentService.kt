package com.testandroid.pagingtestapp.paging

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ContentService {
    @GET
    suspend fun searchRepos(
        @Url url: String?,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): RepoSearchResponse

    @FormUrlEncoded
    @POST("https://dashboard.aktier.ru/api/v1.3/user/test-content/updates")
    suspend fun checkUpdates(
        @Field("howManyNotificationsClientHave") notifications :Int,
        @Field("howManyNewsClientHave") news :Int,
        @Field("howManyOffersClientHave") offers :Int
    ):StatusUpdates

    companion object {
        private const val BASE_URL = "https://dashboard.aktier.ru/api/v1.3/"

        fun create(): ContentService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val client = OkHttpClient.Builder().addInterceptor(logger).build()

            return Retrofit.Builder().baseUrl(BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(ContentService::class.java)
        }
    }
}
