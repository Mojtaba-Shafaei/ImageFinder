package com.shafaei.imageFinder.ui.list

import android.view.*
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.shafaei.imageFinder.businessLogic.local.dto.ImageListItem
import com.shafaei.imageFinder.databinding.ItemListContentBinding
import com.shafaei.imageFinder.databinding.LayoutLoadingMoreBinding
import com.shafaei.imageFinder.ui.list.SimpleItemRecyclerViewAdapter.ViewType.LOADING
import com.shafaei.imageFinder.utils.AndroidUtil
import com.shafaei.imageFinder.utils.GlideAppModule
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class SimpleItemRecyclerViewAdapter(private val inflater: LayoutInflater) : ListAdapter<ImageListItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<ImageListItem>() {
  override fun areItemsTheSame(oldItem: ImageListItem, newItem: ImageListItem): Boolean {
    return (oldItem.id == newItem.id)
  }

  override fun areContentsTheSame(oldItem: ImageListItem, newItem: ImageListItem): Boolean {
    return (oldItem == newItem)
  }
}) {

  init {
    setHasStableIds(true)
  }

  private val mClicks = Channel<View>()
  val clicks: Flow<View> = mClicks.receiveAsFlow()

  var loadingMore: Boolean = false
    set(value) {
      field = value
      if (value) {
        // notifyItemInserted(itemCount)
        notifyDataSetChanged()
      } else
        notifyDataSetChanged()
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return if (viewType == LOADING.ordinal) {
      ViewHolderLoading(LayoutLoadingMoreBinding.inflate(inflater, parent, false))
    } else {
      ViewHolder(ItemListContentBinding.inflate(inflater, parent, false))
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, _position: Int) {
    if (holder is ViewHolder) {
      val item = getItem(holder.bindingAdapterPosition)
      holder.initWith(item)
      with(holder.itemView) {
        tag = item
        setOnClickListener { mClicks.trySend(this) }
      }
    }
  }

  override fun getItemCount() = super.getItemCount() + (if (loadingMore) 1 else 0)

  override fun getItemId(position: Int): Long {
    return if (getItemViewType(position) == LOADING.ordinal) -1 else getItem(position).id.toLong()
  }

  override fun getItemViewType(position: Int): Int {
    return if (loadingMore && (position == itemCount - 1)) LOADING.ordinal else ViewType.DATA.ordinal
  }

  fun setItems(items: List<ImageListItem>) {
    submitList(items)
    loadingMore = false
  }

  inner class ViewHolder(private val binding: ItemListContentBinding) : RecyclerView.ViewHolder(binding.root) {
    fun initWith(item: ImageListItem) {
      Glide.with(binding.ivThumbnail)
         .load(item.imagePreviewUrl)
         .apply(GlideAppModule.sharpCornersRequestOptions)
         .placeholder(AndroidUtil.createProgressDrawable(binding.ivThumbnail.context))
         .into(binding.ivThumbnail)

      binding.tvUserName.text = item.userName
      binding.tvTagList.text = item.tagList.joinToString()
    }
  }

  inner class ViewHolderLoading(binding: LayoutLoadingMoreBinding) : RecyclerView.ViewHolder(binding.root)

  enum class ViewType {
    LOADING, DATA
  }
}