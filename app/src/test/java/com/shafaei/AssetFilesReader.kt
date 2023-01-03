package com.shafaei

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shafaei.imageFinder.businessLogic.network.dto.NetworkImageListItem
import java.io.*

object AssetFilesReader {
  private val gson = Gson()
  private fun loadJsonAsString(fileName: String): String {
    val inputStream = javaClass.getResourceAsStream("/$fileName")
    return getStringFromInputStream(inputStream)
  }

  @Throws(IOException::class)
  private fun getStringFromInputStream(stream: InputStream?): String {
    var n = 0
    val buffer = CharArray(1024 * 4)
    val reader = InputStreamReader(stream, "UTF8")
    val writer = StringWriter()
    while (-1 != reader.read(buffer).also { n = it }) writer.write(buffer, 0, n)
    return writer.toString()
  }

  private fun <T> convertJsonToModel(jsonString: String, classT: Class<T>): T {
    return gson.fromJson(jsonString, classT)
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  fun readFruitsPage1(): List<NetworkImageListItem> {
    val json = loadJsonAsString("fruits_page1.json")
    return gson.fromJson(json, object : TypeToken<List<NetworkImageListItem>>() {}.type)
  }

  fun readFruitsPage2(): List<NetworkImageListItem> {
    val json = loadJsonAsString("fruits_page2.json")
    return gson.fromJson(json, object : TypeToken<List<NetworkImageListItem>>() {}.type)
  }
}