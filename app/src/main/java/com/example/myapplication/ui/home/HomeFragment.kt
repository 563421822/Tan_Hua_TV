package com.example.myapplication.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        webView = binding.webView
        progressBar = root.findViewById(R.id.progressBar)
        // 启用JavaScript（可选，如果需要）
        webView.settings.javaScriptEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
//        Toast.makeText(context, "开通会员后观看", Toast.LENGTH_SHORT).show()
        // 设置WebViewClient，用于处理页面跳转、加载等事件
        webView.webViewClient = WebViewClient()
        // 设置WebChromeClient，用于监听加载进度变化
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.visibility = if (newProgress == 100) View.GONE else View.VISIBLE
            }
        }
        // 加载URL
        webView.loadUrl("https://www.youtube.com/shorts")
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}