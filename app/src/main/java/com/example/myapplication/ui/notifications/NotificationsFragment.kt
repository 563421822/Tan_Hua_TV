package com.example.myapplication.ui.notifications

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentNotificationsBinding
import com.example.myapplication.utils.WbViwClnt


class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        webView = root.findViewById<WebView>(R.id.webView).apply { setOnLongClickListener { true } }
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout)
        // 启用JavaScript（可选，如果需要）
        webView.settings.javaScriptEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = true
        // 设置WebViewClient，用于处理页面跳转、加载等事件
        progressBar = root.findViewById(R.id.progressBar)
        // 添加需要允许的域名列表
        // 设置WebViewClient，用于处理页面跳转、加载等事件
        webView.webViewClient =
            WbViwClnt(listOf("xvideos.com"), requireContext(), progressBar, swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            // 刷新时重新加载WebView
            webView.reload()
        }
        savedInstanceState?.getString("url")?.let { webView.loadUrl(it) }
        if (savedInstanceState == null) webView.loadUrl("https://www.xvideos.com")
        // 加载URL
        webView.isFocusableInTouchMode = true
        webView.requestFocus()
        webView.setOnKeyListener { _, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK) {
                if (webView.canGoBack()) {
                    webView.goBack()
                    true
                } else false
            } else false
        }
        return root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("url", webView.url.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}