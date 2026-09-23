package com.example.cst438_project1_team5

import androidx.test.platform.app.InstrumentationRegistry
import com.example.cst438_project1_team5.api.anime_themes.RetrofitClient
import com.example.cst438_project1_team5.api.malapi.MalApiRepository
import com.example.cst438_project1_team5.api.malapi.MalOAuthManager
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test


//Checks explicitly that the MyAnimeList show ID of a show can be used to obtain
//the same show from the AnimeThemes API.
class ApiLinkingTest {
    @Test
    fun apiIdMatch() = runBlocking {

        val malOAuthManager =
            MalOAuthManager(InstrumentationRegistry.getInstrumentation().targetContext)
        val repository = MalApiRepository(requireNotNull(malOAuthManager.getAccessToken()))
        val user = repository.getList().getOrThrow()

        val show = user.showsWatched[0];
        System.out.println("Show title and ID according to MAL: " + show.title + ", " + show.id);

        val response = RetrofitClient.animeSongApi.getAnimeByMalId(show.id)
        val anime = response.resources[0].anime[0]

        System.out.println("Show title according to AnimeThemes: " + anime.name)

        assertEquals(show.title, anime.name);
    }
}