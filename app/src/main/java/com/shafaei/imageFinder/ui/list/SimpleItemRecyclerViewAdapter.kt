package com.shafaei.imageFinder.ui.list

import android.view.*
import androidx.recyclerview.widget.*
import com.shafaei.imageFinder.ui.list.SimpleItemRecyclerViewAdapter.ViewHolder
import com.shafaei.imageFinder.bussinessLogic.local.ImageListItem
import com.shafaei.imageFinder.databinding.ItemListContentBinding
import com.shafaei.imageFinder.utils.GlideApp
import com.shafaei.imageFinder.utils.GlideAppModule
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SimpleItemRecyclerViewAdapter(private val inflater: LayoutInflater) : ListAdapter<ImageListItem, ViewHolder>(object : DiffUtil.ItemCallback<ImageListItem>() {
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

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemListContentBinding.inflate(inflater, parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = items[position]
    holder.initWith(item)
    with(holder.itemView) {
      tag = item
      setOnClickListener { mClicks.onNext(this) }
    }
  }

  override fun getItemCount() = items.size

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

}