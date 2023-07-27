package com.example.myapplication.utils

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.net.MalformedURLException
import java.net.URL

open class WbViwClnt(
    private val allowedDomains: List<String>,
    private val context: Context?,
    private val progressBar: ProgressBar,
    private val swipeRefreshLayout: SwipeRefreshLayout?
) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        return try {
            for (allowedDomain in allowedDomains) {
                if (URL(request.url.toString()).host.contains(allowedDomain)) return false
            }
            Toast.makeText(context, "请勿轻信网页中的广告", Toast.LENGTH_SHORT).show()
            true
        } catch (e: MalformedURLException) {
            throw RuntimeException(e)
        }
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        progressBar.visibility =
            if (swipeRefreshLayout?.isRefreshing == true) View.GONE else View.VISIBLE
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        progressBar.visibility = View.GONE
        // 页面加载完成后停止下拉刷新
        swipeRefreshLayout?.isRefreshing = false
    }

    override fun onReceivedError(
        view: WebView?, request: WebResourceRequest?, error: WebResourceError?
    ) {
        progressBar.visibility = View.GONE
        if (error!!.errorCode == -2) view?.loadUrl("file:///android_asset/error_page.html");
    }
}