package com.example.boycottini

data class Suggest(
    val id:Long,
    val type: String,         // "PRODUCT_TO_BOYCOTT" or "ALTERNATIVE"
    val brandName: String,
    val proofUrl: String,
    val reasonWhy: String?,
    val alternativeOf: String?,
    val userName: String,
    var liked: Boolean
)

