package com.tab.storiesclone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.tab.storiesclone.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.nav_host_container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onSupportNavigateUp(): Boolean = navController.navigateUp()

}