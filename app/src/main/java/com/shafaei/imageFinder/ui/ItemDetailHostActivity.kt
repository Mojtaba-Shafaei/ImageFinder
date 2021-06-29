package com.shafaei.imageFinder.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.shafaei.imageFinder.R
import com.shafaei.imageFinder.databinding.ActivityItemDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemDetailHostActivity : AppCompatActivity() {

  private lateinit var appBarConfiguration: AppBarConfiguration

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val binding = ActivityItemDetailBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment
    val navController = navHostFragment.navController
    appBarConfiguration = AppBarConfiguration(navController.graph)
    setupActionBarWithNavController(navController, appBarConfiguration)
  }

  override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_item_detail)
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }
}