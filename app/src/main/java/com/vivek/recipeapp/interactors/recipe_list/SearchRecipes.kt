package com.vivek.recipeapp.interactors.recipe_list

import com.vivek.recipeapp.cache.RecipeDao
import com.vivek.recipeapp.cache.model.RecipeEntityMapper
import com.vivek.recipeapp.domain.data.DataState
import com.vivek.recipeapp.domain.model.Recipe
import com.vivek.recipeapp.network.RecipeApiService
import com.vivek.recipeapp.network.model.RecipeDtoMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * USE CASE: Fetch from API -> Store in DB -> Query from DB -> send into UI
 */

class SearchRecipes(
    private val recipeApiService: RecipeApiService,
    private val recipeDao: RecipeDao,
    private val recipeDtoMapper: RecipeDtoMapper,
    private val recipeEntityMapper: RecipeEntityMapper
) {

    fun execute(
        token: String,
        page: Int,
        query: String
    ): Flow<DataState<List<Recipe>>> = flow {
        try {
            emit(DataState.loading())
            delay(1000)          // fake delay

            if (query == "error") {
                throw Exception("Query Error!")
            }

            // fetch from network
            val recipes = getRecipesFromNetwork(token, page, query)

            // insert into cache
            recipeDao.insertRecipes(recipes = recipeEntityMapper.fromDomainModelList(recipes))

            // query cache
            val cacheResult = if (query.isBlank()) {
                recipeDao.getAllRecipes(page = page)
            } else {
                recipeDao.searchRecipes(query = query, page = page)
            }

            // emit the cached data
            val list = recipeEntityMapper.toDomainModelList(cacheResult)
            emit(DataState.success(data = list))

        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Unknown Error!"))
        }
    }

    // TODO: This can throw exception if there is no network
    private suspend fun getRecipesFromNetwork(
        token: String,
        page: Int,
        query: String
    ): List<Recipe> {
        return recipeDtoMapper.toDomainModelList(
            recipeApiService.search(
                token = token,
                page = page,
                query = query
            ).recipes
        )
    }
}



























