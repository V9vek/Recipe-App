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

    // resetting scroll to top always when new search is done
    // and clearing the selected chip if there is custom search through searchBar

    private fun resetSearchState() {
        recipes.value = listOf()
        if (selectedCategory.value?.value != query.value)
            clearSelectedCategory()
    }
}


















