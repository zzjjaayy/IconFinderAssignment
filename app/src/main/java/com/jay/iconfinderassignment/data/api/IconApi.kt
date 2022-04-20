package com.jay.iconfinderassignment.data.api

import com.jay.iconfinderassignment.data.model.CategoryResponse
import com.jay.iconfinderassignment.data.model.IconResponse
import com.jay.iconfinderassignment.data.model.IconSetResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IconApi {
    @GET("iconsets/{set_id}/icons")
    suspend fun getIconsForIconset(@Path("set_id") iconsetId: String,
                                   @Query("count") count: Int,
                                   @Query("offset") offset: Int) : Response<IconResponse>

    @GET("categories")
    suspend fun getCategories(@Query("count") count: Int) : Response<CategoryResponse>

    @GET("categories/{icon_set}/iconsets")
    suspend fun getIconSetsForCategory(
        @Path("icon_set") iconSet: String,
        @Query("count") count: Int) : Response<IconSetResponse>

    @GET("icons/search")
    suspend fun searchIconWithQuery(@Query("query") query: String,
                                    @Query("count") count: Int,
                                    @Query("offset") offset: Int) : Response<IconResponse>
}