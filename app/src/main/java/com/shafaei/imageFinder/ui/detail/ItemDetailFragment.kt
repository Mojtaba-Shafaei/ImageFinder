package com.shafaei.imageFinder.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.mojtaba_shafaei.android.ErrorMessage
import com.shafaei.imageFinder.R
import com.shafaei.imageFinder.businessLogic.local.dto.ImageListItem
import com.shafaei.imageFinder.databinding.FragmentItemDetailBinding
import com.shafaei.imageFinder.utils.*
import dagger.hilt.android.AndroidEntryPoint


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [com.shafaei.imageFinder.ui.list.ItemListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
@AndroidEntryPoint
class ItemDetailFragment : Fragment() {
  private var _binding: FragmentItemDetailBinding? = null

  private var item: ImageListItem? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // The argument [ARG_ITEM_ID] is REQUIRED
    item = requireArguments().getParcelable(ARG_ITEM)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
    return binding.root
  }

  @SuppressLint("InflateParams")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    if (item != null) {
      val data = item!!
      binding.em2.state = ErrorMessage.State.hidden()
      binding.tvUserName.text = data.userName
      binding.tvLikes.text = data.likes.toString()
      binding.tvFavorites.text = data.favorites.toString()
      binding.tvComments.text = data.comments.toString()

      GlideApp.with(this)
         .load(data.imageUrl)
         .apply(GlideAppModule.sharpCornersRequestOptions)
         .placeholder(AndroidUtil.createProgressDrawable(this.requireContext(), large = true))
         .into(binding.ivLarge)

      binding.tagList.removeAllViews()
      data.tagList.forEach { tag ->
        val chip = layoutInflater.inflate(R.layout.chip_item, null) as Chip
        chip.text = tag
        binding.tagList.addView(chip)
      }
    } else {
      binding.em2.state = ErrorMessage.State.noData().copy(message = getString(R.string.no_data))
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