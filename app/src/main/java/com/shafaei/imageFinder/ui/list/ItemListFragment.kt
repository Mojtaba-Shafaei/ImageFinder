package com.shafaei.imageFinder.ui.list

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import com.jakewharton.rxbinding3.recyclerview.scrollStateChanges
import com.mojtaba_shafaei.android.ErrorMessage.State
import com.shafaei.imageFinder.R
import com.shafaei.imageFinder.businessLogic.local.dto.ImageListItem
import com.shafaei.imageFinder.databinding.FragmentItemListBinding
import com.shafaei.imageFinder.exceptions.MyException
import com.shafaei.imageFinder.exceptions.NoInternetException
import com.shafaei.imageFinder.kotlinExt.*
import com.shafaei.imageFinder.ui.detail.ItemDetailFragment
import com.shafaei.imageFinder.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit.MILLISECONDS

/**
 * A Fragment representing a list of Pings. This fragment
 * has different presentations for handset and larger screen devices. On
 * handsets, the fragment presents a list of items, which when touched,
 * lead to a {@link ItemDetailFragment} representing
 * item details. On larger screens, the Navigation controller presents the list of items and
 * item details side-by-side using two vertical panes.
 */

class ItemListFragment : Fragment() {

  private var _binding: FragmentItemListBinding? = null
  private val mAdapter: SimpleItemRecyclerViewAdapter by lazy { SimpleItemRecyclerViewAdapter(layoutInflater) }

  private val mDisposables = CompositeDisposable()

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  // The detail fragment, if the layout is sw-600 it will be not-null
  private var itemDetailFragmentContainer: View? = null

  private val mViewModel: ListViewModel by viewModel()
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
    initUi()
  }

  private fun initUi() {
    binding.searchView.queryHint = getString(R.string.enter_n_chars_to_search).format(Constants.QUERY_MIN_LENGTH)
    binding.itemList.run {
      init()
      adapter = mAdapter
    }
  }

  override fun onResume() {
    super.onResume()
    bindClicks()
    bindStates()
  }

  override fun onPause() {
    super.onPause()
    mDisposables.clear()
  }

  private fun bindClicks() {
    /** Click Listener to trigger navigation based on if you have
     * a single pane layout or two pane layout
     */
    mDisposables +=
       mAdapter.clicks
          .subscribe { itemView ->
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

    mDisposables +=
       binding.searchView
          .queryTextChanges()
          .skipInitialValue()
          .skip(1)
          .map { it.toString().trim() }
          .filter { it.length >= Constants.QUERY_MIN_LENGTH }
          .throttleWithTimeout(Constants.THROTTLE_TIME_OUT_SEARCH, MILLISECONDS, Schedulers.io())
          .distinctUntilChanged()
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({
            mViewModel.search(it)
          }, { showErrors(it) })

    mDisposables +=
       binding.itemList
          .scrollStateChanges()
          .filter { it == RecyclerView.SCROLL_STATE_IDLE }
          .filter { binding.itemList.isReachedToTheLastItem(mAdapter.itemCount) }
          .filter { !mAdapter.loadingMore }
          .subscribe({
            mAdapter.loadingMore = true
            mViewModel.loadNextPage()
          },
             { showErrors(it) })
  }

  private fun bindStates() {
    mDisposables +=
       mViewModel.states
          .filter { it.data != null }
          .map { it.data!! }
          .take(1)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe {
            if (binding.searchView.query.isBlank()) {
              binding.searchView.setQuery(it.param.searchText, false)
            }
          }

    mDisposables +=
       mViewModel.states
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({
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
          }, { showErrors(it) })
  }

  private fun showErrors(error: MyException) {
    Log.e("TAG", "showErrors failed." + error.string(requireContext()))
    if (error is NoInternetException) {
      binding.em.state = State.internetError { mViewModel.retry() }.copy(actionTitle = getString(R.string.retry), message = getString(R.string.no_internet_connection))
    } else {
      Log.e("ItemListFragment", "showErrors failed." + error.extraMessage)
      binding.em.state = State.error(message = error.string(requireContext()), action = { mViewModel.retry() })
         .copy(actionTitle = getString(R.string.retry))
    }
  }

  private fun showErrors(error: Throwable) {
    showErrors(error.mapToMyException())
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
    mDisposables.clear()
  }
}