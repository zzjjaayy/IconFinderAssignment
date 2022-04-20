package com.jay.iconfinderassignment.data.pagingSources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jay.iconfinderassignment.data.model.Icon
import com.jay.iconfinderassignment.data.model.IconSet
import com.jay.iconfinderassignment.data.api.IconApi
import java.lang.Exception

class IconPagingSource(
    private val api: IconApi,
    private val iconSet: IconSet
) : PagingSource<Int, Icon>() {
    companion object {
        const val ICON_PAGING_STARTING_INDEX = 0;
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Icon> {
        val position = params.key ?: ICON_PAGING_STARTING_INDEX

        return try {
            val response = api.getIconsForIconset(iconSet.identifier, params.loadSize, position)
            val icons = response.body()?.icons ?: throw Exception("Icons not found for this iconset!")

            LoadResult.Page(
                data = icons,
                prevKey = if (position == ICON_PAGING_STARTING_INDEX) null else position - params.loadSize,
                nextKey = if (icons.isEmpty()) null else position + params.loadSize
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Icon>): Int? {
        return null
    }
}