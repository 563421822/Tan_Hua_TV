package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.os.Handler
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
import com.example.myapplication.utils.ToastUtils
import com.google.android.material.bottomnavigation.BottomNavigationItemView

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
        val navView = binding.navView
        val findItem = navView.menu.findItem(R.id.navigation_dashboard)
//        非空，已激活的状态
        if (data.isNotEmpty()) {
            binding.maskView.visibility = View.GONE
            findItem.setOnMenuItemClickListener { false }
        } else {
            binding.maskView.visibility = View.VISIBLE
            findItem.setOnMenuItemClickListener {
                supportActionBar?.hide()
                setContentView(R.layout.fragment_blank)
                true
            }
            binding.maskView.setOnClickListener {
                ToastUtils.showToast("请先开通会员", this)
            }
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
        navView.setupWithNavController(navController)
    }
}