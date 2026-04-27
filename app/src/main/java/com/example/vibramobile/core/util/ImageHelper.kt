package com.example.vibramobile.core.util

import android.content.Context
import androidx.compose.ui.graphics.Color
import coil3.ImageLoader
import coil3.request.ImageRequest
import androidx.palette.graphics.Palette
import coil3.request.allowHardware
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ImageHelper {
    suspend fun getDominantColor(
        context: Context,
        imageUrl: String?
    ): Color = withContext(Dispatchers.IO) {

        val loader = ImageLoader(context)

        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            .build()

        val result = loader.execute(request)

        val bitmap = result.image?.toBitmap()
            ?: return@withContext Color.Gray

        val palette = Palette.from(bitmap)
            .resizeBitmapArea(10_000)
            .generate()

        val colorInt = palette.darkVibrantSwatch?.rgb
            ?: palette.vibrantSwatch?.rgb
            ?: palette.dominantSwatch?.rgb
            ?: android.graphics.Color.GRAY

        Color(colorInt)
    }
}