package com.shafaei.imageFinder.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.MediaStoreSignature
import com.shafaei.imageFinder.R
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.util.*

@GlideModule
class GlideAppModule : AppGlideModule() {

  companion object {
    private val last24HourStringSignature: MediaStoreSignature by lazy {
      MediaStoreSignature("JPG", Calendar.getInstance()[Calendar.DAY_OF_YEAR].toLong(), 0)
    }

    val sharpCornersRequestOptions: RequestOptions by lazy {
      RequestOptions()
         .skipMemoryCache(true)
         .encodeFormat(Bitmap.CompressFormat.JPEG)
         .encodeQuality(100)
         .error(R.drawable.baseline_error_outline_pink_200_24dp)
         .diskCacheStrategy(DiskCacheStrategy.ALL)
         .format(DecodeFormat.PREFER_RGB_565)
         .timeout(30000)
         .signature(last24HourStringSignature)
         .centerInside()
    }

    val rounded4dipRequestOptions: RequestOptions by lazy {
      RequestOptions()
         .centerInside()
         .skipMemoryCache(true)
         .error(R.drawable.baseline_error_outline_pink_200_24dp)
         .diskCacheStrategy(DiskCacheStrategy.ALL)
         .encodeFormat(Bitmap.CompressFormat.JPEG)
         .encodeQuality(100)
         .format(DecodeFormat.PREFER_RGB_565)
         .timeout(30000)
         .signature(last24HourStringSignature)
         .transform(RoundedCorners(AndroidUtil.dpToPx(4).toInt()))
    }

    /**
     * Clear Glide's cache on background thread
     */
    fun clearCache(context: Context) {
      Completable.create {
        Glide.get(context.applicationContext).clearDiskCache()
      }
         .subscribeOn(Schedulers.io())
         .subscribe()
    }
  }

  ////////////
  override fun applyOptions(context: Context, builder: GlideBuilder) {
    builder.setLogLevel(Log.DEBUG)
       .setDefaultRequestOptions(sharpCornersRequestOptions)
       .setDiskCache(InternalCacheDiskCacheFactory(context, null, 1024 * 1024 * 100 /* 100 MB*/))
  }
}