package com.vivek.recipeapp.ui.screens.recipe_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vivek.recipeapp.ui.components.CircularIndeterminateProgressBar
import com.vivek.recipeapp.ui.components.ConnectivityMonitor
import com.vivek.recipeapp.ui.components.DefaultSnackBar
import com.vivek.recipeapp.ui.components.GenericDialog
import com.vivek.recipeapp.ui.components.GenericDialogInfo
import com.vivek.recipeapp.ui.components.RecipeCard
import com.vivek.recipeapp.ui.components.RecipeListShimmerAnimation
import com.vivek.recipeapp.ui.components.SearchAppBar
import com.vivek.recipeapp.ui.components.utils.SnackBarController
import com.vivek.recipeapp.ui.screens.recipe_list.RecipeListEvent.NewSearchEvent
import com.vivek.recipeapp.ui.screens.recipe_list.RecipeListEvent.NextPageSearchEvent
import kotlinx.coroutines.launch
import java.util.*


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel = hiltViewModel(),
    onRecipeClicked: (id: Int) -> Unit,
    onToggleTheme: () -> Unit,
    isNetworkAvailable: Boolean,
    isDarkTheme: Boolean
) {
    val recipes = viewModel.recipes.value
    val query = viewModel.query.value
    val selectedCategory = viewModel.selectedCategory.value
    val isLoading = viewModel.isLoading.value
    val page = viewModel.page.value
    val dialogQueue = viewModel.dialogQueue

    // controlling soft keyboard and the cursor focus
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val snackBarController = SnackBarController(scope = scope)

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState },
        topBar = {
            SearchAppBar(
                query = query,
                onQueryChanged = { viewModel.onQueryChanged(query = it) },
                onExecuteSearch = {
                    if (viewModel.selectedCategory.value?.value == "Milk") {
                        // TODO: show snackbar
//                        scope.launch {
//                            scaffoldState.snackbarHostState.showSnackbar(
//                                message = "Invalid Category: Milk",
//                                actionLabel = "Hide"
//                            )
//                        }
                        snackBarController.getScope().launch {
                            snackBarController.showSnackBar(
                                scaffoldState = scaffoldState,
                                message = "Invalid Category: Milk",
                                actionLabel = "Hide"
                            )
                        }
                    } else {
                        viewModel.onTriggerEvent(NewSearchEvent)
                    }
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
                },
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading && recipes.isEmpty()) {
                // TODO: Show Shimmer Animation
                RecipeListShimmerAnimation(
                    cardHeight = 250.dp,
                    colors = listOf(
                        Color.LightGray.copy(alpha = 0.9f),
                        Color.LightGray.copy(alpha = 0.2f),
                        Color.LightGray.copy(alpha = 0.9f),
                    ),
                    gradientWidth = 350f
                )
            } else {
                LazyColumn {
                    itemsIndexed(items = recipes) { index, recipe ->
                        RecipeCard(
                            recipe = recipe,
                            onClick = { onRecipeClicked(recipe.id) }
                        )
                        viewModel.onChangeRecipeListScrollPosition(position = index)

                        if ((index + 1) >= (page * PAGE_SIZE) && !isLoading) {
                            viewModel.onTriggerEvent(NextPageSearchEvent)
                        }
                    }
                }
            }

            CircularIndeterminateProgressBar(isDisplayed = isLoading)

            DefaultSnackBar(
                snackbarHostState = scaffoldState.snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter),
                onDismiss = { scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() }
            )

            ProcessDialogQueue(queue = dialogQueue.queue.value)

            ConnectivityMonitor(isNetworkAvailable = isNetworkAvailable)
        }
    }
}

@Composable
fun ProcessDialogQueue(queue: Queue<GenericDialogInfo>) {
    queue.peek()?.let { dialogInfo ->
        GenericDialog(
            title = dialogInfo.title,
            onDismiss = dialogInfo.onDismiss,
            description = dialogInfo.description,
            positiveAction = dialogInfo.positiveAction,
            negativeAction = dialogInfo.negativeAction
        )
    }
}




















