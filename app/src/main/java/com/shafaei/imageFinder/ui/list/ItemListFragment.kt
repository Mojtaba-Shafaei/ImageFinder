package com.shafaei.imageFinder.ui.list

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mojtaba_shafaei.android.ErrorMessage.State
import com.shafaei.imageFinder.R
import com.shafaei.imageFinder.businessLogic.local.dto.ImageListItem
import com.shafaei.imageFinder.databinding.FragmentItemListBinding
import com.shafaei.imageFinder.exceptions.*
import com.shafaei.imageFinder.kotlinExt.*
import com.shafaei.imageFinder.ui.detail.ItemDetailFragment
import com.shafaei.imageFinder.utils.Constants
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.java.KoinJavaComponent.inject
import reactivecircus.flowbinding.appcompat.queryTextChanges
import reactivecircus.flowbinding.recyclerview.scrollStateChanges

/**
 * A Fragment representing a list of Pings. This fragment
 * has different presentations for handset and larger screen devices. On
 * handsets, the fragment presents a list of items, which when touched,
 * lead to a {@link ItemDetailFragment} representing
 * item details. On larger screens, the Navigation controller presents the list of items and
 * item details side-by-side using two vertical panes.
 */

@OptIn(FlowPreview::class)
class ItemListFragment : Fragment() {

  private var _binding: FragmentItemListBinding? = null
  private val mAdapter: SimpleItemRecyclerViewAdapter by lazy { SimpleItemRecyclerViewAdapter(layoutInflater) }

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  // The detail fragment, if the layout is sw-600 it will be not-null
  private var itemDetailFragmentContainer: View? = null

  private val mViewModel: ListViewModel by inject()
  //////////////////////////////////////////////////////////////////////////////////////////////////

  override fun onCreateView(
     inflater: LayoutInflater, container: ViewGroup?,
     savedInstanceState: Bundle?,
  ): View {

    _binding = FragmentItemListBinding.inflate(inflater, container, false)
    return binding.root

  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    // Leaving this not using view binding as it relies on if the view is visible the current
    // layout configuration (layout, layout-sw600dp)
    itemDetailFragmentContainer = view.findViewById(R.id.item_detail_nav_container)
    //mViewModel = ViewModelProvider(this).get(ListViewModel::class.java)

    viewLifecycleOwner.lifecycleScope.launch {
      lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch {
          initUi(this)
        }

        launch {
          bindClicks(this)
        }

        launch {
          bindStates(this)
        }
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun initUi(scope: CoroutineScope) {
    binding.searchView.queryHint = getString(R.string.enter_n_chars_to_search).format(Constants.QUERY_MIN_LENGTH)
    binding.itemList.run {
      init()
      adapter = mAdapter
    }
  }

  private fun bindClicks(scope: CoroutineScope) {
    /** Click Listener to trigger navigation based on if you have
     * a single pane layout or two pane layout
     */
    scope.launch {
      mAdapter.clicks
         .collect { itemView ->
           // show confirmation dialog
           AlertDialog.Builder(requireContext())
              .setTitle(R.string.open_details)
              .setMessage(R.string.do_you_want_to_see_detail)
              .setPositiveButton(R.string.yes_display) { _, _ ->
                val item = itemView.tag as ImageListItem
                val bundle = bundleOf(ItemDetailFragment.ARG_ITEM to item)
                if (itemDetailFragmentContainer != null) {
                  itemDetailFragmentContainer!!.findNavController().navigate(R.id.fragment_item_detail, bundle)
                } else {
                  itemView.findNavController().navigate(R.id.show_item_detail, bundle)
                }
              }
              .setNegativeButton(android.R.string.cancel, null)
              .setCancelable(true)
              .create()
              .apply { window?.attributes?.gravity = Gravity.BOTTOM }
              .show()
         }
    }

    scope.launch {
      binding.searchView
         .queryTextChanges()
         .skipInitialValue()
         .drop(1)
         .map { it.trim().toString() }
         .filter { it.length >= Constants.QUERY_MIN_LENGTH }
         .debounce(Constants.THROTTLE_TIME_OUT_SEARCH)
         .distinctUntilChanged()
         .collect {
           mViewModel.search(it)
         }
    }

    scope.launch {
      binding.itemList
         .scrollStateChanges()
         .filter { it == RecyclerView.SCROLL_STATE_IDLE }
         .filter { binding.itemList.isReachedToTheLastItem(mAdapter.itemCount) }
         .filter { !mAdapter.loadingMore }
         .collect {
           mAdapter.loadingMore = true
           mViewModel.loadNextPage()
         }
    }
  }

  private fun bindStates(scope: CoroutineScope) {
    scope.launch {
      mViewModel.states
         .filter { it.data != null }
         .map { it.data!! }
         .take(1)
         .collect {
           if (binding.searchView.query.isNullOrBlank()) {
             binding.searchView.setQuery(it.param.searchText, false)
           }
         }
    }

    scope.launch {
      mViewModel.states
         .collect {
           binding.prgLoading.isVisible = it.isLoading

           if (it.hasError()) {
             showErrors(it.error!!)
           }

           it.data?.run {
             mAdapter.setItems(this.result)
             if (binding.searchView.query.isBlank() && this.param.page > 1) {
               binding.searchView.setQuery(this.param.searchText, false)
             }

             if (this.result.isEmpty()) {
               binding.tvMessage.isVisible = true
               binding.tvMessage.setText(R.string.not_found)
             } else {
               binding.tvMessage.isVisible = false
             }
           }

           if (!it.hasError()) {
             binding.em.state = State.hidden()
           }
         }
    }
  }

  private fun showErrors(error: Failable) {
    Log.e("TAG", "showErrors failed." + error.asString(requireContext()))
    if (error is NoInternetFailure) {
      binding.em.state = State.internetError { mViewModel.retry() }.copy(actionTitle = getString(R.string.retry), message = getString(R.string.no_internet_connection))
    } else {
      Log.e("ItemListFragment", "showErrors failed." + error.extraMessage)
      binding.em.state = State.error(message = error.asString(requireContext()), action = { mViewModel.retry() })
         .copy(actionTitle = getString(R.string.retry))
    }
  }
}