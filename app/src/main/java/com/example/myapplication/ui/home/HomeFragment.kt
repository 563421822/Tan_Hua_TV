package com.example.myapplication.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.utils.ToastUtils
import com.example.myapplication.utils.WbViwClnt


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        webView =
            root.findViewById<WebView?>(R.id.webView).apply { setOnLongClickListener { true } }
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout)
        progressBar = root.findViewById(R.id.progressBar)
        // 启用JavaScript（可选，如果需要）
        webView.settings.javaScriptEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = true
        // 添加需要允许的域名列表
        val allowedDomains = listOf("jable.tv")
        // 设置WebViewClient，用于处理页面跳转、加载等事件
        webView.webViewClient = WbViwClnt(allowedDomains, requireContext(), progressBar, swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            // 刷新时重新加载WebView
            webView.reload()
        }
        webView.loadUrl("https://jable.tv/")
        webView.isFocusableInTouchMode = true
        webView.requestFocus()
        var start: Long = 0;
        webView.setOnKeyListener { _, i, keyEvent ->
            val currMill = System.currentTimeMillis();
            if (keyEvent.action == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK) {
                if (webView.canGoBack()) {
                    webView.goBack()
                    true
                } else if (currMill - start < 2000) {
                    requireActivity().finishAffinity()
                    false
                } else {
                    start = currMill
                    ToastUtils.showToast("再按一次退出应用", requireContext())
                    true
                }
            } else false
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}