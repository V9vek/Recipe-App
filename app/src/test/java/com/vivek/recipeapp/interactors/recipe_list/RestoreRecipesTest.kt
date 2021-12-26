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

class RestoreRecipesTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val appDatabaseFake = AppDatabaseFake()
    private val DUMMY_TOKEN = "does not matter"     // can be anything
    private val DUMMY_QUERY = "does not matter"     // can be anything

    // system in test
    private lateinit var restoreRecipes: RestoreRecipes

    // Dependencies
    private lateinit var searchRecipes: SearchRecipes
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

        searchRecipes = SearchRecipes(
            recipeApiService = recipeApiService,
            recipeDao = recipeDaoFake,
            recipeDtoMapper = recipeDtoMapper,
            recipeEntityMapper = recipeEntityMapper
        )

        // instantiate the system in test
        restoreRecipes = RestoreRecipes(
            recipeDao = recipeDaoFake,
            recipeEntityMapper = recipeEntityMapper
        )
    }

    /**
     * 1. Get some recipes from the network and insert into cache,
     *    because RestoreRecipes doesn't restore till we have some recipes in cache
     * 2. Restore and show recipes that are restored from cache
     */
    @Test
    fun getRecipesFromNetwork_restoreFromCache() = runBlocking {
        // mockWebServer setup AND condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(recipeListResponse)
        )

        // confirm the cache is empty
        assert(recipeDaoFake.getAllRecipes(page = 1).isEmpty())

        // insert recipes in cache
        searchRecipes.execute(
            token = DUMMY_TOKEN, page = 1, query = DUMMY_QUERY, isNetworkAvailable = true
        ).toList()

        // ensure cache is NOT EMPTY
        assert(recipeDaoFake.getAllRecipes(page = 1).isNotEmpty())

        // run our use case
        val flowItems = restoreRecipes.execute(
            page = 1,
            query = DUMMY_QUERY
        ).toList()

        // first emission should be LOADING
        assert(flowItems[0].loading)

        // second emission should be LIST OF RESTORED RECIPES from CACHE
        val restoredRecipesFromCache = flowItems[1].data
        assert(restoredRecipesFromCache?.size ?: 0 > 0)

        assert(restoredRecipesFromCache?.get(0) is Recipe)

        // ensure LOADING is FALSE now
        assert(!flowItems[1].loading)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
























