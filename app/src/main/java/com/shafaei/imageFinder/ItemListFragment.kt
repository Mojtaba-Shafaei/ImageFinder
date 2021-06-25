package com.shafaei.imageFinder

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.shafaei.imageFinder.databinding.FragmentItemListBinding
import com.shafaei.imageFinder.placeholder.PixaBayItem
import com.shafaei.imageFinder.placeholder.PlaceholderContent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

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
    val recyclerView: RecyclerView = binding.itemList

    // Leaving this not using view binding as it relies on if the view is visible the current
    // layout configuration (layout, layout-sw600dp)
    itemDetailFragmentContainer = view.findViewById(R.id.item_detail_nav_container)

    setupRecyclerView(recyclerView)
    bindClicks()

    mAdapter.setItems(PlaceholderContent.ITEMS)
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
               .setTitle(R.string.confirmation)
               .setMessage(R.string.do_you_want_to_see_detail)
               .setPositiveButton(R.string.yes_display) { _, _ ->
                 val item = itemView.tag as PixaBayItem
                 val bundle = bundleOf(ItemDetailFragment.ARG_ITEM_ID to item.id)
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

  private fun setupRecyclerView(recyclerView: RecyclerView) {
    recyclerView.adapter = mAdapter
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
    mDisposables.clear()
  }
}