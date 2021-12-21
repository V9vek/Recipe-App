package com.vivek.recipeapp.di

import com.vivek.recipeapp.cache.RecipeDao
import com.vivek.recipeapp.cache.model.RecipeEntityMapper
import com.vivek.recipeapp.interactors.recipe.GetRecipe
import com.vivek.recipeapp.interactors.recipe_list.RestoreRecipes
import com.vivek.recipeapp.interactors.recipe_list.SearchRecipes
import com.vivek.recipeapp.network.RecipeApiService
import com.vivek.recipeapp.network.model.RecipeDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideSearchRecipes(
        recipeApiService: RecipeApiService,
        recipeDao: RecipeDao,
        recipeDtoMapper: RecipeDtoMapper,
        recipeEntityMapper: RecipeEntityMapper
    ): SearchRecipes {
        return SearchRecipes(
            recipeApiService,
            recipeDao,
            recipeDtoMapper,
            recipeEntityMapper
        )
    }

    @ViewModelScoped
    @Provides
    fun provideRestoreRecipes(
        recipeDao: RecipeDao,
        recipeEntityMapper: RecipeEntityMapper
    ): RestoreRecipes {
        return RestoreRecipes(
            recipeDao,
            recipeEntityMapper
        )
    }

    @ViewModelScoped
    @Provides
    fun provideGetRecipes(
        recipeApiService: RecipeApiService,
        recipeDao: RecipeDao,
        recipeDtoMapper: RecipeDtoMapper,
        recipeEntityMapper: RecipeEntityMapper
    ): GetRecipe {
        return GetRecipe(
            recipeApiService,
            recipeDao,
            recipeDtoMapper,
            recipeEntityMapper
        )
    }
}



















