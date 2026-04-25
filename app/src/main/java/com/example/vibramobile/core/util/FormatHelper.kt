package com.example.vibramobile.core.util

object FormatHelper {
    fun formatFollowers(count: Int): String {
        return when {
            count >= 1_000_000 -> String.format("%.1f Tr", count / 1_000_000f)
            count >= 1_000 -> String.format("%.1f N", count / 1_000f)
            else -> count.toString()
        }
    }

    fun formatPlayed(count: Int): String {
        return when {
            count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000f)
            count >= 1_000 -> String.format("%.1fK", count / 1_000f)
            else -> count.toString()
        }
    }

    fun formatPlayCount(count: Int): String {
        return when {
            count >= 1_000_000 -> String.format("%.1fM plays", count / 1_000_000f)
            count >= 1_000 -> String.format("%.1fK plays", count / 1_000f)
            else -> "$count plays"
        }
    }
}