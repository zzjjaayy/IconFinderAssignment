package com.jay.iconfinderassignment.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.jay.iconfinderassignment.data.pagingSources.IconPagingSource
import com.jay.iconfinderassignment.data.api.RetrofitInstance
import com.jay.iconfinderassignment.data.model.*
import com.jay.iconfinderassignment.data.pagingSources.SearchIconsPagingSource
import com.jay.iconfinderassignment.utils.TAG
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class IconNetworkViewModel : ViewModel() {

    private val _iconList = MutableLiveData<PagingData<Icon>>()
    val iconList : LiveData<PagingData<Icon>> get() = _iconList

    private val _searchIconList = MutableLiveData<PagingData<Icon>>()
    val searchIconList : LiveData<PagingData<Icon>> get() = _searchIconList

    fun getIcons(iconSet: IconSet) {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    maxSize = 80,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = { IconPagingSource(RetrofitInstance.API, iconSet) }
            ).flow.collect {
                _iconList.postValue(it)
            }
        }
    }

    fun getCategories(resultCallback: (IconSet?) -> Unit) {
        viewModelScope.launch {
            try {
                val response : Response<CategoryResponse> = RetrofitInstance.API.getCategories(100)
                if(response.isSuccessful) {
                    Log.d(TAG, "getCategories: $response")
                    Log.d(TAG, "number of categories: ${response.body()?.categories?.size}")
                    val categories = response.body()?.categories ?: throw Exception("No categories found in response!")
                    getIconSetsForCategory(categories.random()) {
                        resultCallback(it)
                    }
                } else throw Exception(response.errorBody().toString())
            } catch (exception: Exception) {
                Log.d(TAG, "getCategories: $exception")
                resultCallback(null)
            }
        }
    }

    private fun getIconSetsForCategory(category: IconCategory, resultCallback: (IconSet?) -> Unit) {
        viewModelScope.launch {
            try {
                val response : Response<IconSetResponse> =
                    RetrofitInstance.API.getIconSetsForCategory(category.identifier, 10)
                if(response.isSuccessful) {
                    Log.d(TAG, "getIconSets: $response")
                    Log.d(TAG, "number of iconsets for ${category.identifier}: ${response.body()?.iconsets?.size}")
                    val iconSets = response.body()?.iconsets ?: throw Exception("No iconsets found in response!")
//                    getIconsForIconSet(iconSets.random())
                    resultCallback(iconSets.random())
                } else throw Exception(response.errorBody().toString())
            } catch (exception: Exception) {
                Log.d(TAG, "getIconSets: $exception")
                resultCallback(null)
            }
        }
    }

    fun searchIcons(query: String) {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    maxSize = 80,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = { SearchIconsPagingSource(RetrofitInstance.API, query) }
            ).flow.cachedIn(viewModelScope).collect {
                _searchIconList.postValue(it)
            }
        }
    }
}