package com.example.myapplication.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        webView = binding.webView
        // 启用JavaScript（可选，如果需要）
        webView.settings.javaScriptEnabled = true
        Toast.makeText(context,"开通会员后观看", Toast.LENGTH_SHORT).show()
        // 设置WebViewClient以便在WebView内部打开链接，而不是使用默认的浏览器
        webView.webViewClient = WebViewClient()
        // 加载URL
        webView.loadUrl("https://www.pornhub.com")
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}