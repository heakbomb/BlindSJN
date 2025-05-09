package com.glowstudio.android.blindsjn.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.glowstudio.android.blindsjn.feature.foodcoast.FoodCostScreen
import com.glowstudio.android.blindsjn.feature.foodcoast.RegisterIngredientScreen
import com.glowstudio.android.blindsjn.feature.foodcoast.RegisterRecipeScreen
import com.glowstudio.android.blindsjn.ui.viewModel.TopBarState
import com.glowstudio.android.blindsjn.ui.viewModel.TopBarViewModel

fun NavGraphBuilder.foodCostNavGraph(
    navController: NavHostController,
    topBarViewModel: TopBarViewModel
) {
    navigation(
        startDestination = "food_cost_screen",
        route = "food_cost_root"
    ) {
        composable("food_cost_screen") {
            topBarViewModel.updateState(TopBarState("푸드코스트 계산", true, false))
            FoodCostScreen(
                onRegisterRecipeClick = { navController.navigate("register_recipe_screen") },
                onRegisterIngredientClick = { navController.navigate("register_ingredient_screen") }
            )
        }

        composable("register_recipe_screen") {
            topBarViewModel.updateState(TopBarState("레시피 등록", true, false))
            RegisterRecipeScreen()
        }

        composable("register_ingredient_screen") {
            topBarViewModel.updateState(TopBarState("재료 등록", true, false))
            RegisterIngredientScreen()
        }
    }
} 