package com.example.devlife.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.devlife.Network.DevLifeFeedService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class Session(private val category: Category) {

    private var posts: MutableList<Post> = mutableListOf()
    var currentPost: MutableLiveData<Post> = MutableLiveData()
    private var totalCount = 0
    private var currentPage = 0
    private var currentPostIndex = 0



    lateinit var service: DevLifeFeedService

    init {
        var client = OkHttpClient.Builder().build();


        var retrofit = Retrofit.Builder()
            .baseUrl("https://developerslife.ru/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        service = retrofit.create<DevLifeFeedService>(DevLifeFeedService::class.java)

        loadPage(currentPage)

    }

    @SuppressLint("CheckResult")
    fun loadPage(pageNum: Int) {
        service.getPage(category = category.category, page_num = pageNum)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { page ->
                    totalCount = page.totalCount
                    if (totalCount == 0) {
                        // Постов нет
                        return@subscribe
                    }

                    for (post in page.result) {
                        posts.add(post)
                    }

                    currentPost.value = posts[currentPostIndex]
                },
                { _ ->  Log.d("Session", "Не смогли загрузить категория = " + category.category)}
            )
    }

    fun isHistoryEmpty() : Boolean = (posts.size == 0)

}