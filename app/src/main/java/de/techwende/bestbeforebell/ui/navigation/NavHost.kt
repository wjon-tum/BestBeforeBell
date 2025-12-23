package de.techwende.bestbeforebell.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.techwende.bestbeforebell.ui.editproduct.ProductEditorScreen
import de.techwende.bestbeforebell.ui.productlist.ProductListScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.ProductList.route
    ) {
        // Product list
        composable(
            route = Screen.ProductList.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            ProductListScreen(
                onAddClicked = { navController.navigate(Screen.AddProduct.route) },
                onEditClicked = { product ->
                    navController.navigate(Screen.EditProduct.createRoute(product.id))
                }
            )
        }

        // Add product
        composable(
            route = Screen.AddProduct.route,
            enterTransition = {
                slideInHorizontally { it } + fadeIn()
            },
            exitTransition = {
                slideOutHorizontally { it } + fadeOut()
            },
            popEnterTransition = {
                slideInHorizontally { -it } + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally { it } + fadeOut()
            }
        ) {
            ProductEditorScreen(
                onFinished = {
                    navController.popBackStack()
                }
            )
        }

        // Edit product
        composable(
            route = Screen.EditProduct.route,
            enterTransition = {
                slideInHorizontally { it } + fadeIn()
            },
            exitTransition = {
                slideOutHorizontally { it } + fadeOut()
            },
            popEnterTransition = {
                slideInHorizontally { -it } + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally { it } + fadeOut()
            },
            arguments =
                listOf(
                    navArgument("productId") { type = NavType.LongType }
                )
        ) { backStackEntry ->
            backStackEntry.arguments!!.getLong("productId")
            ProductEditorScreen(
                onFinished = {
                    navController.popBackStack()
                }
            )
        }
    }
}
