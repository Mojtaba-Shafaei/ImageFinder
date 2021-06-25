package com.shafaei.imageFinder

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.chip.Chip
import com.shafaei.imageFinder.databinding.FragmentItemDetailBinding
import com.shafaei.imageFinder.placeholder.PlaceholderContent
import com.shafaei.imageFinder.utils.GlideApp
import com.shafaei.imageFinder.utils.GlideAppModule


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
class ItemDetailFragment : Fragment() {
  private lateinit var imageId: String
  private var _binding: FragmentItemDetailBinding? = null

  private val item by lazy { PlaceholderContent.ITEM }

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // The argument [ARG_ITEM_ID] is REQUIRED
    imageId = requireArguments().getString(ARG_ITEM_ID) ?: throw IllegalArgumentException("Please Pass the ImageId as an argument to this Fragment")
  }

  override fun onCreateView(
     inflater: LayoutInflater, container: ViewGroup?,
     savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    // show mock data
    binding.tvUserName?.text = item.userName
    binding.tvLikes?.text = item.likes.toString()
    binding.tvFavorites?.text = item.favorites.toString()
    binding.tvComments?.text = item.comments.toString()

    GlideApp.with(this)
       .load(item.imageUrl)
       .apply(GlideAppModule.sharpCornersRequestOptions)
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
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    const val ARG_ITEM_ID = "item_id"
  }
}