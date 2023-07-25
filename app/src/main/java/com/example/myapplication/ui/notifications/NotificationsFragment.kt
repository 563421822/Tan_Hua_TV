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
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentNotificationsBinding


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
        // 设置WebViewClient，用于处理页面跳转、加载等事件
        webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
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