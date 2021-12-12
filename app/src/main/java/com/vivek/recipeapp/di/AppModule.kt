package com.vivek.recipeapp.di

import android.content.Context
import com.vivek.recipeapp.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // this binding fun is to provide Application Context, we can use it anywhere by field injection
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext context: Context): BaseApplication {
        return context as BaseApplication
    }

}













