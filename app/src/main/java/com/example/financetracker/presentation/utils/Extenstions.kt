package com.example.financetracker.presentation.utils

import androidx.compose.ui.graphics.Color

/**
 *  Converts Color hex code to Android Color
 *  - input Example: #0000FF
 * */
fun Color.Companion.fromHex(colorString: String) =
    Color(android.graphics.Color.parseColor(colorString))
