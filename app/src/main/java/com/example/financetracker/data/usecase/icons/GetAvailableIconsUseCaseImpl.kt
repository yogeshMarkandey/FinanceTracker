package com.example.financetracker.data.usecase.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.financetracker.domain.model.usecase.icons.GetAvailableIconsUseCase
import javax.inject.Inject

class GetAvailableIconsUseCaseImpl @Inject constructor() : GetAvailableIconsUseCase {
    override fun execute(): List<ImageVector> {

        return arrayListOf(
            Icons.Default.Add,
            Icons.Default.Home,
            Icons.Default.Settings,
            Icons.Default.Menu,
            Icons.Default.Favorite,
            Icons.Default.ArrowBack,
            Icons.Default.Delete,
            Icons.Default.Edit,
            Icons.Default.Email,
            Icons.Default.Info,
            Icons.Default.Search,
            Icons.Default.ShoppingCart,
            Icons.Default.Check,
            Icons.Default.Close,
            Icons.Default.MoreVert,
            Icons.Default.ArrowForward,
            Icons.Default.FavoriteBorder,
            Icons.Default.Person,
            Icons.Default.Warning,
            Icons.Default.CheckCircle,
            Icons.Default.ArrowDropDown,
            Icons.Default.Lock,
            Icons.Default.Phone
        )
    }
}