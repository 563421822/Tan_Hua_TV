package com.example.myapplication

import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.common.Constant
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.utils.MySharedPreferences
import com.example.myapplication.utils.ToastUtils
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import java.security.MessageDigest
import java.time.Duration
import java.time.LocalDateTime
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var shrdPre: MySharedPreferences
    private lateinit var dashboard: MenuItem
    private var activated by Delegates.notNull<Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE or WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_SECURE or WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        shrdPre = MySharedPreferences(this)
        activated = shrdPre.getData(Constant.ACTIVATED)?.toBoolean() ?: false
//        检查激活状态
        if (activated) checkMembershipStatus() else dealNonActivated()
    }

    private fun dealNonActivated() {
        binding.maskView.visibility = View.VISIBLE
        dashboard.setOnMenuItemClickListener {
            supportActionBar?.hide()
            setContentView(R.layout.fragment_blank)
            if (activated) {
                findViewById<LinearLayout>(R.id.expired_layout).visibility = View.VISIBLE
                findViewById<TextView>(R.id.last_expired).text =
                    shrdPre.getData(Constant.EXPIRE_TIME)
                findViewById<Button>(R.id.activate).text = "立即续费"
            }
            true
        }
        binding.maskView.setOnClickListener { ToastUtils.showToast("请先激活会员", this) }
    }

    private fun checkMembershipStatus() {
        val now = LocalDateTime.now()
        val expireTime =
            shrdPre.getData(Constant.EXPIRE_TIME)?.let { LocalDateTime.parse(it) } ?: now

        if (now > expireTime) {
            ToastUtils.showToast("您的会员已到期", this)
            dealNonActivated()
        } else {
            binding.maskView.visibility = View.GONE
            dashboard.setOnMenuItemClickListener { false }
        }
    }

    private fun setupUI() {
        val navView = binding.navView
        dashboard = navView.menu.findItem(R.id.navigation_dashboard)
        val fragmentManager = supportFragmentManager
        val navHstFrgmnt =
            fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHstFrgmnt.navController
        val set =
            setOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        set.forEach {
            findViewById<BottomNavigationItemView>(it).setOnLongClickListener { true }
        }
        val appBarConfiguration = AppBarConfiguration(set)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun onCloseClick(ignoredView: View) {
        supportActionBar?.show()
        setContentView(binding.root)
    }

    fun onActivate(view: View) {
        val activateItem = findViewById<RadioGroup>(R.id.activate_item)
        val chkdItem = activateItem.checkedRadioButtonId
        val acvtPeriod = findViewById<RadioButton>(chkdItem).tag.toString()
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val md5Hex = androidId.md5Hash()
        val now = LocalDateTime.now()
        val expireTime =
            shrdPre.getData(Constant.EXPIRE_TIME)?.let { LocalDateTime.parse(it) } ?: now
        val daysDifference =
            if (now > expireTime) acvtPeriod else (Duration.between(now, expireTime)
                .toDays() + acvtPeriod.toByte()).toString()
        val dataMap = mapOf(
            Constant.ANDROID_ID to androidId,
            Constant.ACVT_TIME to now.toString(),
            Constant.RES_MATURITY to "$daysDifference 天",
            Constant.EXPIRE_TIME to if (now > expireTime) now.plusDays(acvtPeriod.toLong())
                .toString()
            else expireTime.plusDays(acvtPeriod.toLong()).toString(),
            Constant.ACTIVATED to true.toString(),
            Constant.TOKEN to md5Hex
        )

        shrdPre.saveMultipleData(dataMap)
        ToastUtils.showToast("激活成功", this)
        onCloseClick(view)
        binding.maskView.visibility = View.GONE
        dashboard.setOnMenuItemClickListener { false }
    }

    private fun String.md5Hash(): String {
        val instance = MessageDigest.getInstance("MD5")
        instance.update(this.toByteArray())
        val md5Bytes = instance.digest()
        return md5Bytes.joinToString("") { "%02x".format(it) }
    }
}