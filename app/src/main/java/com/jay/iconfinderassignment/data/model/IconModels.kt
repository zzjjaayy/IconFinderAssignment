package com.jay.iconfinderassignment.data.model

// ICON MODELS
data class IconResponse (
    val icons: List<Icon>,
    val total_count : Int
)
data class Icon(
    val icon_id: Int,
    val is_premium : Boolean,
    val prices : List<IconPrice>,
    val raster_sizes : List<RasterSize>
)

data class IconPrice (val currency: String, val price : Int)
data class RasterFormat(val format: String, val preview_url: String, val download_url: String)
data class RasterSize(val size: Int, val formats: List<RasterFormat>)