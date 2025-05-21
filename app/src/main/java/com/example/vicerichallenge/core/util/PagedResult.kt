package com.example.vicerichallenge.core.util

data class PagedResult<T>(
    val items: List<T>,
    val totalItems: Int
)
