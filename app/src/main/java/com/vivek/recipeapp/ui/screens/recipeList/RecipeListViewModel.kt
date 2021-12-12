package com.vivek.recipeapp.ui.screens.recipeList

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.recipeapp.domain.model.Recipe
import com.vivek.recipeapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PAGE_SIZE = 30

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val token: String
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
        newSearch()
    }

    fun newSearch() = viewModelScope.launch {
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

    fun nextPageSearch() = viewModelScope.launch {
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

    /**
     * Append new recipes to the current list of recipes
     */
    private fun appendRecipes(recipes: List<Recipe>) {
        val current = this.recipes.value.toMutableList()
        current.addAll(recipes)
        this.recipes.value = current.toList()
    }

    private fun incrementPage() {
        page.value = page.value + 1
    }

    fun onChangeRecipeListScrollPosition(position: Int) {
        recipeListScrollPosition = position
    }

    fun onQueryChanged(query: String) {
        this.query.value = query
    }

    fun onSelectedCategoryChanged(category: String) {
        val newCategory = getFoodCategory(category)
        selectedCategory.value = newCategory
        onQueryChanged(category)
    }

    fun onChangeCategoryScrollPosition(position: Int) {
        categoryScrollPosition = position
    }

    private fun clearSelectedCategory() {
        selectedCategory.value = null
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
}


















