package com.vivek.recipeapp.interactors.recipe_list

import com.google.gson.GsonBuilder
import com.vivek.recipeapp.cache.AppDatabaseFake
import com.vivek.recipeapp.cache.RecipeDaoFake
import com.vivek.recipeapp.cache.model.RecipeEntityMapper
import com.vivek.recipeapp.domain.model.Recipe
import com.vivek.recipeapp.network.RecipeApiService
import com.vivek.recipeapp.network.data.MockWebServerResponses.recipeListResponse
import com.vivek.recipeapp.network.model.RecipeDtoMapper
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
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
    private val DUMMY_TOKEN = "does not matter"
    private val DUMMY_QUERY = "does not matter"

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

    /**
     * 1. Are the recipes retrieved from network
     * 2. Are the recipes inserted into cache
     * 3. Are the recipes then emitted as a FLOW from the cache to the UI
     * 4. Check for loading status before and after
     */

    @Test
    fun getRecipesFromNetwork_emitRecipesFromCache() = runBlocking {
        // mockWebServer setup AND condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(recipeListResponse)
        )

        // confirm the cache is empty
        assert(recipeDaoFake.getAllRecipes(page = 1).isEmpty())

        val flowItems = searchRecipes.execute(
            token = DUMMY_TOKEN,
            query = DUMMY_QUERY,
            page = 1,
            isNetworkAvailable = true
        ).toList()

        // confirm the cache is no longer empty
        assert(recipeDaoFake.getAllRecipes(page = 1).isNotEmpty())

        // first emission should be LOADING
        assert(flowItems[0].loading)

        // second emission should be LIST OF RECIPES
        val recipes = flowItems[1].data
        assert(recipes?.size ?: 0 > 0)

        // confirm they are actually RECIPE OBJECTS
        assert(recipes?.get(0) is Recipe)

        // ensure LOADING is FALSE now
        assert(!flowItems[1].loading)
    }

    @Test
    fun getRecipesFromNetwork_emitHttpError() = runBlocking {
        // mockWebServer setup AND condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody("{}")
        )

        val flowItems = searchRecipes.execute(
            token = DUMMY_TOKEN,
            page = 1,
            query = DUMMY_QUERY,
            isNetworkAvailable = true
        ).toList()

        assert(flowItems[0].loading)

        val error = flowItems[1].error
        assert(error != null)

        assert(!flowItems[1].loading)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }
}

























