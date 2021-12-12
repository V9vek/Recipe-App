package com.vivek.recipeapp.di

import com.vivek.recipeapp.network.RecipeApiService
import com.vivek.recipeapp.network.model.RecipeDtoMapper
import com.vivek.recipeapp.repository.RecipeRepository
import com.vivek.recipeapp.repository.RecipeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRecipeRepository(
        recipeApiService: RecipeApiService,
        recipeDtoMapper: RecipeDtoMapper
    ): RecipeRepository {
        return RecipeRepositoryImpl(recipeApiService, recipeDtoMapper)
    }
}











