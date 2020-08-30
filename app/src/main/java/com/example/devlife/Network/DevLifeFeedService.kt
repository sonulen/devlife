package com.example.devlife.Network

import com.example.devlife.data.Page
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface DevLifeFeedService {
    @GET("/{category}/{page_num}?json=true")
    fun getPage(
        @Path("category") category: String,
        @Path("page_num") page_num: Int
    ): Single<Page>
}