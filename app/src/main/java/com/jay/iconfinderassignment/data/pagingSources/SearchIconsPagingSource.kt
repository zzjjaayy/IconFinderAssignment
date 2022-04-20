package com.jay.iconfinderassignment.data.pagingSources

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jay.iconfinderassignment.data.api.IconApi
import com.jay.iconfinderassignment.data.model.Icon
import com.jay.iconfinderassignment.utils.TAG
import java.lang.Exception

class SearchIconsPagingSource(
    private val api: IconApi, private val query: String
) : PagingSource<Int, Icon>() {
    companion object {
        const val ICON_PAGING_STARTING_INDEX = 0;
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Icon> {
        val position = params.key ?: ICON_PAGING_STARTING_INDEX

        return try {
            Log.d(TAG, "load: calling for response")
            val response = api.searchIconWithQuery(query, params.loadSize, position)
            Log.d(TAG, "load: ${response.body()}")
            val icons = response.body()?.icons ?: throw Exception("Icons not found for this query!")

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