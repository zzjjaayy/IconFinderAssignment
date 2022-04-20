package com.jay.iconfinderassignment.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.jay.iconfinderassignment.R
import com.jay.iconfinderassignment.data.model.Icon
import com.jay.iconfinderassignment.databinding.FragmentHomeBinding
import com.jay.iconfinderassignment.ui.viewmodels.DownloaderViewModel
import com.jay.iconfinderassignment.ui.viewmodels.IconNetworkViewModel
import com.jay.iconfinderassignment.utils.IconLoadStateAdapter
import com.jay.iconfinderassignment.utils.TAG

class HomeFragment : Fragment(), SearchView.OnQueryTextListener {

    private val viewModel : IconNetworkViewModel by activityViewModels()
    private val downloaderViewModel : DownloaderViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var iconsListAdapter: IconsAdapter
    private lateinit var searchIconAdapter: IconsAdapter
    private lateinit var listIconObserver: Observer<PagingData<Icon>>
    private lateinit var searchView: SearchView
    private lateinit var backPressedCallback: OnBackPressedCallback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        initializeObservers()
        handleBackButtonLogic()
        initializeAdapters()

        binding.recyclerView.setHasFixedSize(true)
        setAdapter(iconsListAdapter)
        getCategories()
        binding.randomiseBtn.setOnClickListener {
            getCategories()
        }
    }

    private fun handleBackButtonLogic() {
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d(TAG, "handleOnBackPressed: $isEnabled")
                if(!searchView.isIconified) {
                    Log.d(TAG, "not iconified!")
                    searchView.isIconified = true
                    return
                }
                requireActivity().onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedCallback)
    }

    private fun initializeObservers() {
        listIconObserver = Observer<PagingData<Icon>> { pagedIcons ->
            binding.apply {
                recyclerView.isVisible = true
                errorIcon.isVisible = false
                errorText.isVisible = false
            }
            iconsListAdapter.submitData(this.lifecycle, pagedIcons)
        }
    }

    private fun initializeAdapters() {
        iconsListAdapter = IconsAdapter{ icon ->
            Log.d(TAG, "initializeAdapters: icon is -> $icon")
            icon?.let {
                downloaderViewModel.downloadMedia(requireActivity().applicationContext, icon)
            }
        }
        searchIconAdapter = IconsAdapter{ icon ->
            Log.d(TAG, "initializeAdapters: icon is -> $icon")
            icon?.let {
                downloaderViewModel.downloadMedia(requireActivity().applicationContext, icon)
            }
        }
    }

    private fun setAdapter(adapter: IconsAdapter) {
        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = IconLoadStateAdapter { adapter.retry() },
            footer = IconLoadStateAdapter { adapter.retry() }
        )
    }

    private fun getCategories() {
        viewModel.getCategories { iconset ->
            iconset?.let {
                viewModel.getIcons(iconset)
                viewModel.iconList.observe(viewLifecycleOwner, listIconObserver)
            } ?: run {
                binding.apply {
                    recyclerView.isVisible = false
                    errorIcon.isVisible = true
                    errorText.isVisible = true
                    loadingBar.isVisible = false
                }
            }
            binding.loadingBar.isVisible = false
        }
        binding.loadingBar.isVisible = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        searchView = (search.actionView as? SearchView)!!
        searchView.setOnQueryTextListener(this)
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.queryHint = "Search icons here..."

        searchView.setOnSearchClickListener {
            Log.d(TAG, "onCreateOptionsMenu: search clicked")
            binding.randomiseBtn.isVisible = false
            setAdapter(searchIconAdapter)
        }

        searchView.setOnCloseListener {
            Log.d(TAG, "onCreateOptionsMenu: search closed")
            binding.randomiseBtn.isVisible = true
            setAdapter(iconsListAdapter)
            false
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            viewModel.searchIcons(query)
            binding.loadingBar.isVisible = true
            viewModel.searchIconList.observe(viewLifecycleOwner) { pagedIcons ->
                Log.d(TAG, "initializeObservers: $pagedIcons")
                binding.loadingBar.isVisible = false
                searchIconAdapter.submitData(this.lifecycle, pagedIcons)
            }
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        onQueryTextSubmit(newText)
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}