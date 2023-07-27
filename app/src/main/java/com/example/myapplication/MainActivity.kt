package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.utils.MySharedPreferences
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var shrdPre: MySharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE or WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_SECURE or WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        shrdPre = MySharedPreferences(this)
        val data = shrdPre.getData("activated")
        if (data.isEmpty()) binding.maskView.visibility = View.GONE
        binding.maskView.setOnClickListener {
            Toast.makeText(this, "请先开通会员", Toast.LENGTH_SHORT).show()
        }
        val navHstFrgmnt =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHstFrgmnt.navController
        val set = setOf(
            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
        )
        set.forEach {
            findViewById<BottomNavigationItemView>(it).setOnLongClickListener { true }
        }
        val appBarConfiguration = AppBarConfiguration(set)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }
}