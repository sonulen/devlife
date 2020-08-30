package com.example.devlife.data

/*
 * Страница постов от Api https://developerslife.ru/
 */
data class Page(
    val result: List<Post>,
    val totalCount: Int
)