package com.shafaei.imageFinder.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.shafaei.imageFinder.R
import com.shafaei.imageFinder.bussinessLogic.local.dto.ImageListItem
import com.shafaei.imageFinder.databinding.FragmentItemDetailBinding
import com.shafaei.imageFinder.utils.*


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [com.shafaei.imageFinder.ui.list.ItemListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
class ItemDetailFragment : Fragment() {
  private var _binding: FragmentItemDetailBinding? = null

  private lateinit var item: ImageListItem

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // The argument [ARG_ITEM_ID] is REQUIRED
    item = requireArguments().getParcelable(ARG_ITEM) ?: throw IllegalArgumentException("Please Pass the Item as an argument to this Fragment")
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
    return binding.root
  }

  @SuppressLint("InflateParams")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    // show mock data
    binding.tvUserName?.text = item.userName
    binding.tvLikes?.text = item.likes.toString()
    binding.tvFavorites?.text = item.favorites.toString()
    binding.tvComments?.text = item.comments.toString()

    GlideApp.with(this)
       .load(item.imageUrl)
       .apply(GlideAppModule.sharpCornersRequestOptions)
       .placeholder(AndroidUtil.createProgressDrawable(large = true))
       .into(binding.ivLarge)

    binding.tagList?.removeAllViews()
    item.tagList.forEach { tag ->
      val chip = layoutInflater.inflate(R.layout.chip_item, null) as Chip
      chip.text = tag
      binding.tagList?.addView(chip)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {
    /**
     * The fragment argument representing the item[ImageListItem] that this fragment
     * represents.
     */
    const val ARG_ITEM = "item"
  }
}