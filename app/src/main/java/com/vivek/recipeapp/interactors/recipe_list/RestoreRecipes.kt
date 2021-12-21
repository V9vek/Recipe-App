package com.vivek.recipeapp.interactors.recipe_list

import com.vivek.recipeapp.cache.RecipeDao
import com.vivek.recipeapp.cache.model.RecipeEntityMapper
import com.vivek.recipeapp.domain.data.DataState
import com.vivek.recipeapp.domain.model.Recipe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * USE CASE: Restore list after process death
 */

class RestoreRecipes(
    private val recipeDao: RecipeDao,
    private val recipeEntityMapper: RecipeEntityMapper
) {
    fun execute(
        page: Int,
        query: String
    ): Flow<DataState<List<Recipe>>> = flow {
        try {
            emit(DataState.loading())
            delay(1000)

            // query cache
            val cacheResult = if (query.isBlank()) {
                recipeDao.restoreAllRecipes(page = page)
            } else {
                recipeDao.restoreRecipes(query = query, page = page)
            }

            // emit the restored data from cache
            val list = recipeEntityMapper.toDomainModelList(cacheResult)
            emit(DataState.success(data = list))

        } catch (e: Exception) {
            emit(DataState.error(message = e.message ?: "Unknown Error"))
        }
    }
}



























