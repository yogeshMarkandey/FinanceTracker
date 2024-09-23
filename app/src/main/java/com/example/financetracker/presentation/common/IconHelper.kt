package com.example.financetracker.presentation.common

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.financetracker.data.usecase.icons.GetAvailableIconsUseCaseImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object IconHelper {
    private val _mapAvailableIcon = HashMap<String, Int>()
    private var _availableIcons = emptyList<ImageVector>()

    fun init() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val icons = GetAvailableIconsUseCaseImpl().execute()
                _availableIcons = icons
                icons.forEachIndexed { index, icon ->
                    _mapAvailableIcon[icon.name] = index
                }
            } catch (e: Exception) {
                Log.e("IconHelper", "getAvailableIcons: ${e.message}", e)
            }
        }
    }

    fun getAvailableIcons(): List<ImageVector> {
        return _availableIcons
    }

    fun getIconByName(name: String): ImageVector {
        return _availableIcons.firstOrNull {
            it.name == name
        } ?: Icons.Default.Clear
    }
}