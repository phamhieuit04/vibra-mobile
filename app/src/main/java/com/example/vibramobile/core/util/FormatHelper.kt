package com.example.vibramobile.core.util

import android.annotation.SuppressLint

object FormatHelper {

    @SuppressLint("DefaultLocale")
    fun formatMonthlyListeners(count: Int): String {
        val formatted = when {
            count >= 1_000_000 -> String.format("%.1f Tr", count / 1_000_000f)
            count >= 1_000 -> String.format("%.1f N", count / 1_000f)
            else -> count.toString()
        }

        return "$formatted lượt nghe hàng tháng"
    }
}