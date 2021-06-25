package com.shafaei.imageFinder

import android.view.*
import androidx.recyclerview.widget.*
import com.shafaei.imageFinder.SimpleItemRecyclerViewAdapter.ViewHolder
import com.shafaei.imageFinder.databinding.ItemListContentBinding
import com.shafaei.imageFinder.placeholder.PixaBayItem
import com.shafaei.imageFinder.utils.GlideApp
import com.shafaei.imageFinder.utils.GlideAppModule
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SimpleItemRecyclerViewAdapter(private val inflater: LayoutInflater) : ListAdapter<PixaBayItem, ViewHolder>(object : DiffUtil.ItemCallback<PixaBayItem>() {
  override fun areItemsTheSame(oldItem: PixaBayItem, newItem: PixaBayItem): Boolean {
    return (oldItem.id == newItem.id)
  }

  override fun areContentsTheSame(oldItem: PixaBayItem, newItem: PixaBayItem): Boolean {
    return (oldItem == newItem)
  }
}) {
  private val items: MutableList<PixaBayItem> = ArrayList()

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

  fun setItems(items: List<PixaBayItem>) {
    this.items.clear()
    this.items.addAll(items)
    submitList(this.items) { notifyDataSetChanged() }
  }

  inner class ViewHolder(private val binding: ItemListContentBinding) : RecyclerView.ViewHolder(binding.root) {
    fun initWith(item: PixaBayItem) {
      GlideApp.with(binding.ivThumbnail)
         .load("https://cdn.pixabay.com/photo/2018/01/28/11/24/sunflower-3113318_150.jpg")
         .apply(GlideAppModule.sharpCornersRequestOptions)
         .into(binding.ivThumbnail)

      binding.tvUserName.text = item.userName
      binding.tvTagList.text = item.tagList.joinToString()
    }

  }

}