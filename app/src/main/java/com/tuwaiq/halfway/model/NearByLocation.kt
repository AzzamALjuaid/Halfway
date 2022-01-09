package com.tuwaiq.halfway.model

data class NearByLocation (
    val next_page_token: String,
    val results: List<Result>,
    val status: String
)