package com.shafaei.imageFinder.ui.list

import android.view.*
import androidx.recyclerview.widget.*
import com.shafaei.imageFinder.bussinessLogic.local.dto.ImageListItem
import com.shafaei.imageFinder.databinding.ItemListContentBinding
import com.shafaei.imageFinder.databinding.LayoutLoadingMoreBinding
import com.shafaei.imageFinder.ui.list.SimpleItemRecyclerViewAdapter.ViewType.LOADING
import com.shafaei.imageFinder.utils.GlideApp
import com.shafaei.imageFinder.utils.GlideAppModule
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SimpleItemRecyclerViewAdapter(private val inflater: LayoutInflater) : ListAdapter<ImageListItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<ImageListItem>() {
  override fun areItemsTheSame(oldItem: ImageListItem, newItem: ImageListItem): Boolean {
    return (oldItem.id == newItem.id)
  }

  override fun areContentsTheSame(oldItem: ImageListItem, newItem: ImageListItem): Boolean {
    return (oldItem == newItem)
  }
}) {
  private val items: MutableList<ImageListItem> = ArrayList()

  private val mClicks: PublishSubject<View> = PublishSubject.create()
  val clicks: Observable<View> = mClicks

  var loadingMore: Boolean = false
    set(value) {
      field = value
      if (value) {
        notifyItemInserted(itemCount)
      } else
        notifyItemRemoved(itemCount)
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return if (viewType == LOADING.ordinal) {
      ViewHolderLoading(LayoutLoadingMoreBinding.inflate(inflater, parent, false))
    } else {
      ViewHolder(ItemListContentBinding.inflate(inflater, parent, false))
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    if (holder is ViewHolder) {
      val item = items[position]
      holder.initWith(item)
      with(holder.itemView) {
        tag = item
        setOnClickListener { mClicks.onNext(this) }
      }
    }
  }

  override fun getItemCount() = items.size + (if (loadingMore) 1 else 0)

  override fun getItemId(position: Int): Long {
    return getItem(position).id.toLong()
  }

  override fun getItemViewType(position: Int): Int {
    return if (loadingMore && (position == itemCount - 1)) LOADING.ordinal else ViewType.DATA.ordinal
  }

  fun setItems(items: List<ImageListItem>) {
    this.items.clear()
    this.items.addAll(items)
    submitList(this.items) { notifyDataSetChanged() }
  }

  inner class ViewHolder(private val binding: ItemListContentBinding) : RecyclerView.ViewHolder(binding.root) {
    fun initWith(item: ImageListItem) {
      GlideApp.with(binding.ivThumbnail)
         .load(item.imagePreviewUrl)
         .apply(GlideAppModule.sharpCornersRequestOptions)
         .into(binding.ivThumbnail)

      binding.tvUserName.text = item.userName
      binding.tvTagList.text = item.tagList
    }

  }

  inner class ViewHolderLoading(private val binding: LayoutLoadingMoreBinding) : RecyclerView.ViewHolder(binding.root)

  enum class ViewType {
    LOADING, DATA
  }
}