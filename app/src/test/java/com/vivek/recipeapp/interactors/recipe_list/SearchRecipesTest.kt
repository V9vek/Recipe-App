package com.vivek.recipeapp.interactors.recipe_list

import com.google.gson.GsonBuilder
import com.vivek.recipeapp.cache.AppDatabaseFake
import com.vivek.recipeapp.cache.RecipeDaoFake
import com.vivek.recipeapp.cache.model.RecipeEntityMapper
import com.vivek.recipeapp.network.RecipeApiService
import com.vivek.recipeapp.network.data.MockWebServerResponses.recipeListResponse
import com.vivek.recipeapp.network.model.RecipeDtoMapper
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class SearchRecipesTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val appDatabaseFake = AppDatabaseFake()

    // system in test
    private lateinit var searchRecipes: SearchRecipes

    // Dependencies
    private lateinit var recipeApiService: RecipeApiService
    private lateinit var recipeDaoFake: RecipeDaoFake
    private val recipeDtoMapper = RecipeDtoMapper()
    private val recipeEntityMapper = RecipeEntityMapper()


    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url(path = "/api/recipe/")
        recipeApiService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(RecipeApiService::class.java)

        recipeDaoFake = RecipeDaoFake(appDatabaseFake = appDatabaseFake)

        // instantiate the system in test
        searchRecipes = SearchRecipes(
            recipeApiService = recipeApiService,
            recipeDao = recipeDaoFake,
            recipeDtoMapper = recipeDtoMapper,
            recipeEntityMapper = recipeEntityMapper
        )
    }

    @Test
    fun mockWebServerSetup() {
        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(recipeListResponse)
        )
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }
}

























