package com.vivek.recipeapp.ui.screens.recipeList

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.recipeapp.domain.model.Recipe
import com.vivek.recipeapp.repository.RecipeRepository
import com.vivek.recipeapp.ui.screens.recipeList.RecipeListEvent.NewSearchEvent
import com.vivek.recipeapp.ui.screens.recipeList.RecipeListEvent.NextPageSearchEvent
import com.vivek.recipeapp.ui.screens.recipeList.RecipeListEvent.RestoreStateEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PAGE_SIZE = 30

const val STATE_KEY_PAGE = "recipe.state.page.key"
const val STATE_KEY_QUERY = "recipe.state.query.key"
const val STATE_KEY_LIST_POSITION = "recipe.state.query.list_position"
const val STATE_KEY_SELECTED_CATEGORY = "recipe.state.query.selected_category"

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val token: String,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val recipes = mutableStateOf<List<Recipe>>(listOf())

    val query = mutableStateOf("")

    val selectedCategory = mutableStateOf<FoodCategory?>(null)

    val isLoading = mutableStateOf(false)

    var categoryScrollPosition = 0

    // Paging setup

    val page = mutableStateOf(1)

    private var recipeListScrollPosition = 0

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
    private suspend fun newSearch() {
        isLoading.value = true

        resetSearchState()

        delay(1000)

        val result = repository.search(
            token = token,
            page = 1,
            query = query.value
        )
        recipes.value = result

        isLoading.value = false
    }

    // use case 2
    private suspend fun nextPageSearch() {
        // prevent duplicate events of searching, due to recomposition happening too quickly
        if ((recipeListScrollPosition + 1) >= (page.value * PAGE_SIZE)) {
            isLoading.value = true
            incrementPage()

            delay(1000)     // because api is fast

            if (page.value > 1) {
                val result = repository.search(
                    token = token,
                    page = page.value,
                    query = query.value
                )
                appendRecipes(recipes = result)
            }

            isLoading.value = false
        }
    }

    // use case 3
    private suspend fun restoreState() {
        isLoading.value = true

        val results = mutableListOf<Recipe>()

        for (p in 1..page.value) {
            val result = repository.search(
                token = token,
                page = p,
                query = query.value
            )
            results.addAll(result)
        }

        recipes.value = results
        isLoading.value = false
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


















