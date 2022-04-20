package com.jay.iconfinderassignment.data.model

// CATEGORY MODELS
data class CategoryResponse(
    val categories: List<IconCategory>
)

data class IconCategory(
    val name: String,
    val identifier: String
)