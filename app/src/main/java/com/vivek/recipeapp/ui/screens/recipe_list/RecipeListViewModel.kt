package com.vivek.recipeapp.ui.screens.recipe_list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.recipeapp.domain.model.Recipe
import com.vivek.recipeapp.interactors.recipe_list.RestoreRecipes
import com.vivek.recipeapp.interactors.recipe_list.SearchRecipes
import com.vivek.recipeapp.ui.screens.recipe_list.RecipeListEvent.NewSearchEvent
import com.vivek.recipeapp.ui.screens.recipe_list.RecipeListEvent.NextPageSearchEvent
import com.vivek.recipeapp.ui.screens.recipe_list.RecipeListEvent.RestoreStateEvent
import com.vivek.recipeapp.ui.util.CustomConnectivityManager
import com.vivek.recipeapp.ui.util.DialogQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PAGE_SIZE = 30

const val STATE_KEY_PAGE = "recipe.state.page.key"
const val STATE_KEY_QUERY = "recipe.state.query.key"
const val STATE_KEY_LIST_POSITION = "recipe.state.query.list_position"
const val STATE_KEY_SELECTED_CATEGORY = "recipe.state.query.selected_category"

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val searchRecipes: SearchRecipes,
    private val restoreRecipes: RestoreRecipes,
    private val token: String,
    private val savedStateHandle: SavedStateHandle,
    private val customConnectivityManager: CustomConnectivityManager
) : ViewModel() {

    val recipes = mutableStateOf<List<Recipe>>(listOf())

    val query = mutableStateOf("")

    val selectedCategory = mutableStateOf<FoodCategory?>(null)

    val isLoading = mutableStateOf(false)

    var categoryScrollPosition = 0

    // Paging setup
    val page = mutableStateOf(1)

    private var recipeListScrollPosition = 0

    // Dialog Queue
    val dialogQueue = DialogQueue()

    init {
        // getting the values from savedStateHandle
        savedStateHandle.get<Int>(STATE_KEY_PAGE)?.let { p ->
            setPage(page = p)
        }
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
            setQuery(query = q)
        }
        savedStateHandle.get<Int>(STATE_KEY_LIST_POSITION)?.let { p ->
            setRecipeListScrollPosition(position = p)
        }
        savedStateHandle.get<FoodCategory?>(STATE_KEY_SELECTED_CATEGORY)?.let { c ->
            setSelectedCategory(category = c)
        }

        // if scrolling is done, then we should restore previous states else no

        if (recipeListScrollPosition != 0) {
            onTriggerEvent(event = RestoreStateEvent)
        } else {
            onTriggerEvent(event = NewSearchEvent)
        }
    }

    fun onTriggerEvent(event: RecipeListEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is NewSearchEvent -> {
                        newSearch()
                    }
                    is NextPageSearchEvent -> {
                        nextPageSearch()
                    }
                    is RestoreStateEvent -> {
                        restoreState()
                    }
                }
            } catch (e: Exception) {
                println(e.cause)
            }
        }
    }

    // use case 1
    private fun newSearch() {
        resetSearchState()

        searchRecipes.execute(
            token = token,
            page = page.value,
            query = query.value,
            isNetworkAvailable = customConnectivityManager.isNetworkAvailable.value
        ).onEach { dataState ->
            isLoading.value = dataState.loading

            dataState.data?.let { list ->
                recipes.value = list
            }

            dataState.error?.let { error ->
                println("ERROR: newSearch: $error")
                dialogQueue.appendErrorMessage(title = "Error", description = error)
            }
        }.launchIn(viewModelScope)
    }

    // use case 2
    private fun nextPageSearch() {
        // prevent duplicate events of searching, due to recomposition happening too quickly
        if ((recipeListScrollPosition + 1) >= (page.value * PAGE_SIZE)) {
            incrementPage()

            if (page.value > 1) {
                searchRecipes.execute(
                    token = token,
                    page = page.value,
                    query = query.value,
                    isNetworkAvailable = customConnectivityManager.isNetworkAvailable.value
                ).onEach { dataState ->
                    isLoading.value = dataState.loading

                    dataState.data?.let { list ->
                        appendRecipes(recipes = list)
                    }

                    dataState.error?.let { error ->
                        println("ERROR: nextPageSearch: $error")
                        dialogQueue.appendErrorMessage(title = "Error", description = error)
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    // use case 3
    private fun restoreState() {
        restoreRecipes.execute(page = page.value, query = query.value)
            .onEach { dataState ->
                isLoading.value = dataState.loading

                dataState.data?.let { list ->
                    recipes.value = list
                }

                dataState.error?.let { error ->
                    println("ERROR: restoreState: $error")
                    dialogQueue.appendErrorMessage(title = "Error", description = error)
                }
            }.launchIn(viewModelScope)
    }

    /**
     * Append new recipes to the current list of recipes
     */
    private fun appendRecipes(recipes: List<Recipe>) {
        val current = this.recipes.value.toMutableList()
        current.addAll(recipes)
        this.recipes.value = current.toList()
    }

    private fun incrementPage() {
        setPage(page.value + 1)
    }

    fun onChangeRecipeListScrollPosition(position: Int) {
        setRecipeListScrollPosition(position)
    }

    fun onQueryChanged(query: String) {
        setQuery(query)
    }

    fun onSelectedCategoryChanged(category: String) {
        val newCategory = getFoodCategory(category)
        setSelectedCategory(newCategory)
        onQueryChanged(category)
    }

    fun onChangeCategoryScrollPosition(position: Int) {
        categoryScrollPosition = position
    }

    private fun clearSelectedCategory() {
        setSelectedCategory(null)
    }

    /**
     * resetting scroll to top always when new search is done
     * and clearing the selected chip if there is custom search through searchBar
     */
    private fun resetSearchState() {
        recipes.value = listOf()
        page.value = 1
        onChangeRecipeListScrollPosition(position = 0)

        if (selectedCategory.value?.value != query.value)
            clearSelectedCategory()
    }

    // savedStateHandle functions

    private fun setPage(page: Int) {
        this.page.value = page
        savedStateHandle.set(STATE_KEY_PAGE, page)
    }

    private fun setRecipeListScrollPosition(position: Int) {
        recipeListScrollPosition = position
        savedStateHandle.set(STATE_KEY_LIST_POSITION, position)
    }

    private fun setSelectedCategory(category: FoodCategory?) {
        selectedCategory.value = category
        savedStateHandle.set(STATE_KEY_SELECTED_CATEGORY, category)
    }

    private fun setQuery(query: String) {
        this.query.value = query
        savedStateHandle.set(STATE_KEY_QUERY, query)
    }
}


















