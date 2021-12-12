package com.vivek.recipeapp.network

import com.vivek.recipeapp.network.model.RecipeDto
import com.vivek.recipeapp.network.responses.RecipeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RecipeApiService {
    /*
        https://food2fork.ca/api/recipe/search/?page=1&query=egg
        Base URL: https://food2fork.ca/api/recipe/
        GET Request: search/
        Header
        Query Param: ?page=1&query=egg
    */
    @GET("search")
    suspend fun search(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ): RecipeSearchResponse


    /*
        https://food2fork.ca/api/recipe/get/?id=9
        GET Request: get/
        Header
        Query Param: ?id=9
    */
    @GET("get")
    suspend fun get(
        @Header("Authorization") token: String,
        @Query("id") id: Int,
    ): RecipeDto
}























