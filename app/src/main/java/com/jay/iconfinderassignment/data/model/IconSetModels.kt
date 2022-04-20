package com.jay.iconfinderassignment.data.model

// ICON SET MODELS
data class IconSetResponse(
    val iconsets: List<IconSet>,
    val total_count: Int
)

data class IconSet(
    val iconset_id : Int,
    val icons_count : Int,
    val name: String,
    val identifier: String,
    val is_premium: Boolean
)