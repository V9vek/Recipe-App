package com.vivek.recipeapp.ui.screens.recipeList

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import com.vivek.recipeapp.ui.components.AnimatedHeartButton
import com.vivek.recipeapp.ui.components.HeartButtonState.ACTIVE
import com.vivek.recipeapp.ui.components.HeartButtonState.IDLE
import com.vivek.recipeapp.ui.components.SearchAppBar
import com.vivek.recipeapp.ui.components.utils.SnackBarController
import com.vivek.recipeapp.ui.screens.recipeList.RecipeListEvent.NewSearchEvent
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel = hiltViewModel(),
    onRecipeClicked: (id: Int) -> Unit,
    onToggleTheme: () -> Unit
) {
    val recipes = viewModel.recipes.value
    val query = viewModel.query.value
    val selectedCategory = viewModel.selectedCategory.value
    val isLoading = viewModel.isLoading.value
    val page = viewModel.page.value

    // controlling soft keyboard and the cursor focus
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val snackBarController = SnackBarController(scope = scope)

    // animation
    var buttonState by remember { mutableStateOf(IDLE) }

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
                onToggleTheme = onToggleTheme,
            )
        }
    ) {
        Column {
//            PulsingAnimation()
            AnimatedHeartButton(
                buttonState = buttonState,
                onToggle = { buttonState = if (buttonState == IDLE) ACTIVE else IDLE }
            )
        }

        /*
        Box(modifier = Modifier.fillMaxSize()) {
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

            DefaultSnackBar(
                snackbarHostState = scaffoldState.snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter),
                onDismiss = { scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() }
            )

        }
        */
    }
}




















