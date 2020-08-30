package com.example.devlife.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.devlife.data.Category
import com.example.devlife.data.Post
import com.example.devlife.data.Session


class FeedViewModel(private val category: Category) : ViewModel() {
    private val session = Session(category)

    fun getData(): LiveData<Post> = session.currentPost
    fun isHistoryEmpty() = session.isHistoryEmpty()
    fun nextPost() = session.nextPost()
    fun prevPost() = session.prevPost()

    /**
     * Есть ли еще посты в репозитории
     */
    fun yetAnotherPost() = session.yetAnotherPost()

}