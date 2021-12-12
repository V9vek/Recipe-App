package com.vivek.recipeapp.ui.screens.recipeList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import com.vivek.recipeapp.ui.components.CircularIndeterminateProgressBar
import com.vivek.recipeapp.ui.components.RecipeCard
import com.vivek.recipeapp.ui.components.SearchAppBar
import com.vivek.recipeapp.ui.screens.recipeList.RecipeListEvent.NewSearchEvent
import com.vivek.recipeapp.ui.screens.recipeList.RecipeListEvent.NextPageSearchEvent


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel = hiltViewModel(),
    onRecipeClicked: (id: Int) -> Unit
) {
    val recipes = viewModel.recipes.value
    val query = viewModel.query.value
    val selectedCategory = viewModel.selectedCategory.value
    val isLoading = viewModel.isLoading.value
    val page = viewModel.page.value

    // controlling soft keyboard and the cursor focus
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column {
        SearchAppBar(
            query = query,
            onQueryChanged = { viewModel.onQueryChanged(query = it) },
            onExecuteSearch = {
                viewModel.onTriggerEvent(NewSearchEvent)
                keyboardController?.hide()
                focusManager.clearFocus()
            },
            categoryScrollPosition = viewModel.categoryScrollPosition,
            selectedCategory = selectedCategory,
            onSelectedCategoryChanged = { selectedChip ->
                viewModel.onSelectedCategoryChanged(category = selectedChip)
            },
            onChangeCategoryScrollPosition = { scrollStateValue ->
                viewModel.onChangeCategoryScrollPosition(position = scrollStateValue)
            }
        )

        Box(modifier = Modifier) {
            if (isLoading && recipes.isEmpty()) {
                // TODO: Show Shimmer Animation
            } else {
                LazyColumn {
                    itemsIndexed(items = recipes) { index, recipe ->
                        RecipeCard(
                            recipe = recipe,
                            onClick = {
                                if (recipe.id != null) {
                                    onRecipeClicked(recipe.id)
                                } else {
                                    // TODO: Show SnackBar
                                }
                            }
                        )
                        viewModel.onChangeRecipeListScrollPosition(position = index)

                        if ((index + 1) >= (page * PAGE_SIZE) && !isLoading) {
                            viewModel.onTriggerEvent(NextPageSearchEvent)
                        }
                    }
                }
            }

            CircularIndeterminateProgressBar(isDisplayed = isLoading)
        }
    }
}
























