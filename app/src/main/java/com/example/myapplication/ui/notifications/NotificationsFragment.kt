package com.example.myapplication.ui.notifications

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentNotificationsBinding
import java.net.URL


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
        webView = binding.webView.apply { setOnLongClickListener { true } }
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout)
        // 启用JavaScript（可选，如果需要）
        webView.settings.javaScriptEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = true
        // 设置WebViewClient，用于处理页面跳转、加载等事件
        progressBar = root.findViewById(R.id.progressBar)
        // 添加需要允许的域名列表
        val allowedDomains = listOf("xvideos.com")
        // 设置WebViewClient，用于处理页面跳转、加载等事件
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?, request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                try {
                    allowedDomains.forEach {
                        if (URL(url).host.contains(it)) return false
                    }
                    Toast.makeText(context, "请勿轻信网页中的广告", Toast.LENGTH_SHORT).show()
                    return true
                } catch (e: java.net.MalformedURLException) {
                    // 处理 URL 解析异常
                    e.printStackTrace()
                }
                // 域名在允许列表中，继续加载
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar.visibility =
                    if (swipeRefreshLayout.isRefreshing) View.GONE else View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE
                // 页面加载完成后停止下拉刷新
                swipeRefreshLayout.isRefreshing = false
            }

            override fun onReceivedError(
                view: WebView?, request: WebResourceRequest?, error: WebResourceError?
            ) {
                progressBar.visibility = View.GONE
                val webViewState = Bundle()
                webView.saveState(webViewState)
                webViewState.getString("webViewState")
                if (error!!.errorCode == -2) view?.loadUrl("file:///android_asset/error_page.html");
            }
        }
        swipeRefreshLayout.setOnRefreshListener {
            // 刷新时重新加载WebView
            webView.reload()
        }
        // 加载URL
        webView.loadUrl("https://www.xvideos.com")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}