package com.example.devlife.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.devlife.data.Category
import com.example.devlife.data.Post
import com.example.devlife.data.Session


class FeedViewModel(private val category: Category) : ViewModel() {
    private val session = Session(category)
    fun getData(): LiveData<Post> = session.currentPost
    fun isHistoryEmpty() = session.isHistoryEmpty()

}