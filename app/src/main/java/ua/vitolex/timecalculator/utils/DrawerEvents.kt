package ua.vitolex.timecalculator.utils

sealed class DrawerEvents {
    data class OnItemClick(val title: String) : DrawerEvents()
}
