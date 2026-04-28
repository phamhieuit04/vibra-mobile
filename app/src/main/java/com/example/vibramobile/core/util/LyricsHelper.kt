package com.example.vibramobile.core.util

private val LRC_TIME_REGEX = Regex("\\[(\\d{2}):(\\d{2})(?:\\.(\\d{1,3}))?]")

data class LyricLine(
    val timeMs: Long?,
    val text: String
)

object LyricsHelper {
    fun parseLyrics(lines: List<String>): List<LyricLine> {
        if (lines.isEmpty()) return emptyList()

        val parsed = ArrayList<LyricLine>(lines.size)
        for (line in lines) {
            val matches = LRC_TIME_REGEX.findAll(line).toList()
            if (matches.isEmpty()) {
                parsed.add(LyricLine(timeMs = null, text = line))
                continue
            }

            val text = line.replace(LRC_TIME_REGEX, "").trim()
            for (match in matches) {
                parsed.add(LyricLine(timeMs = parseTimestamp(match), text = text))
            }
        }
        return parsed
    }

    fun findActiveIndex(lines: List<LyricLine>, positionMs: Long): Int {
        if (lines.isEmpty()) return -1

        var activeIndex = -1
        for (index in lines.indices) {
            val timeMs = lines[index].timeMs ?: continue
            if (positionMs >= timeMs) {
                activeIndex = index
            } else {
                break
            }
        }
        return activeIndex
    }

    private fun parseTimestamp(match: MatchResult): Long {
        val minutes = match.groupValues[1].toIntOrNull() ?: 0
        val seconds = match.groupValues[2].toIntOrNull() ?: 0
        val fraction = match.groupValues[3]
        val fractionMs = when (fraction.length) {
            0 -> 0
            1 -> fraction.toIntOrNull()?.times(100) ?: 0
            2 -> fraction.toIntOrNull()?.times(10) ?: 0
            else -> fraction.take(3).toIntOrNull() ?: 0
        }
        return ((minutes * 60) + seconds) * 1000L + fractionMs
    }
}

