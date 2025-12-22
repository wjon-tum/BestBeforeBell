package de.techwende.bestbeforebell.ui.navigation

sealed class Screen(
    val route: String
) {
    object ProductList : Screen("products")

    object AddProduct : Screen("product/add")

    object EditProduct : Screen("product/edit/{productId}") {
        fun createRoute(productId: Long) = "product/edit/$productId"
    }
}
