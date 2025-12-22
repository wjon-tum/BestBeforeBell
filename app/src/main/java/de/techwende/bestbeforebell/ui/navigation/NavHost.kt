package de.techwende.bestbeforebell.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.techwende.bestbeforebell.ui.modifyproduct.ProductEditorScreen
import de.techwende.bestbeforebell.ui.productlist.ProductListScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.ProductList.route
    ) {
        // Product list
        composable(Screen.ProductList.route) {
            ProductListScreen(
                onAddClicked = {
                    navController.navigate(Screen.AddProduct.route)
                },
                onEditClicked = { product ->
                    navController.navigate(Screen.EditProduct.createRoute(product.id))
                }
            )
        }

        // Add product
        composable(Screen.AddProduct.route) {
            ProductEditorScreen(
                productId = null,
                onFinished = {
                    navController.popBackStack()
                }
            )
        }

        // Edit product
        composable(
            route = Screen.EditProduct.route,
            arguments =
                listOf(
                    navArgument("productId") { type = NavType.LongType }
                )
        ) { backStackEntry ->
            val productId =
                backStackEntry.arguments!!.getLong("productId")

            ProductEditorScreen(
                productId = productId,
                onFinished = {
                    navController.popBackStack()
                }
            )
        }
    }
}
