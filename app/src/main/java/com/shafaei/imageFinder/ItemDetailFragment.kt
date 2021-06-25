package com.shafaei.imageFinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shafaei.imageFinder.databinding.FragmentItemDetailBinding
import com.shafaei.imageFinder.placeholder.*

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

  /**
   * The placeholder content this fragment is presenting.
   */
  private var item: PixaBayItem? = null

  lateinit var itemDetailTextView: TextView

  private var _binding: FragmentItemDetailBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    arguments?.let {
      if (it.containsKey(ARG_ITEM_ID)) {
        // Load the placeholder content specified by the fragment
        // arguments. In a real-world scenario, use a Loader
        // to load content from a content provider.
        item = PlaceholderContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
      }
    }
  }

  override fun onCreateView(
     inflater: LayoutInflater, container: ViewGroup?,
     savedInstanceState: Bundle?,
  ): View {

    _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
    val rootView = binding.root

    binding.toolbarLayout?.title = item?.userName

    itemDetailTextView = binding.itemDetail
    // Show the placeholder content as text in a TextView.
    item?.let {
      itemDetailTextView.text = it.tagList.joinToString ()
    }

    return rootView
  }

  companion object {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    const val ARG_ITEM_ID = "item_id"
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}