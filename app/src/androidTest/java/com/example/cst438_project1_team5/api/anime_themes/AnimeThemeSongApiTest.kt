package com.example.cst438_project1_team5.api.anime_themes

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class AnimeThemeSongApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: AnimeThemeSongApi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnimeThemeSongApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getRandomAudio_usesDefaultQueryParameters_andMapsAudioResponse() = runTest {
        mockWebServer.enqueue(jsonResponse(audioCollectionJson))

        val response = api.getRandomAudio()

        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/audio/", request.requestUrl?.encodedPath)
        assertEquals("random", request.requestUrl?.queryParameter("sort"))
        assertEquals("1", request.requestUrl?.queryParameter("page[size]"))

        assertEquals(200, response.code())
        val song = response.body()?.songs?.single()
        assertNotNull(song)
        assertEquals("Bakemonogatari-OP1.ogg", song?.basename)
        assertEquals("2026-01-01T00:00:00.000000Z", song?.createdAt)
        assertNull(song?.deletedAt)
        assertEquals(42, song?.viewsCount)
    }

    @Test
    fun getRandomAudio_usesProvidedQueryParameters() = runTest {
        mockWebServer.enqueue(jsonResponse(audioCollectionJson))

        api.getRandomAudio(sort = "basename", pageSize = 5)

        val request = mockWebServer.takeRequest()
        assertEquals("basename", request.requestUrl?.queryParameter("sort"))
        assertEquals("5", request.requestUrl?.queryParameter("page[size]"))
    }

    @Test
    fun getSong_requestsBasename_andMapsSingleAudioResponse() = runTest {
        mockWebServer.enqueue(jsonResponse(singleAudioJson))

        val response = api.getSong("Bakemonogatari-OP1.ogg")

        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/audio/Bakemonogatari-OP1.ogg", request.requestUrl?.encodedPath)
        assertEquals("https://a.animethemes.moe/Bakemonogatari-OP1.ogg", response.body()?.song?.link)
    }

    @Test
    fun getSong_returnsUnsuccessfulResponse_forHttpError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(404)
                .setBody("""{"message":"Audio not found"}""")
        )

        val response = api.getSong("missing.ogg")

        assertFalse(response.isSuccessful)
        assertEquals(404, response.code())
        assertNull(response.body())
        assertEquals("""{"message":"Audio not found"}""", response.errorBody()?.string())
    }

    @Test
    fun retrofitClient_createsAnimeThemeSongApi_withoutMakingANetworkRequest() {
        assertNotNull(RetrofitClient.animeSongApi)
    }

    private fun jsonResponse(body: String): MockResponse =
        MockResponse()
            .setResponseCode(200)
            .addHeader("Content-Type", "application/json")
            .setBody(body)

    private companion object {
        private const val audioJson = """
            {
              "id": 1,
              "basename": "Bakemonogatari-OP1.ogg",
              "filename": "Bakemonogatari-OP1",
              "path": "audio/Bakemonogatari-OP1.ogg",
              "size": 12345,
              "mimetype": "audio/ogg",
              "created_at": "2026-01-01T00:00:00.000000Z",
              "updated_at": "2026-01-02T00:00:00.000000Z",
              "deleted_at": null,
              "link": "https://a.animethemes.moe/Bakemonogatari-OP1.ogg",
              "views_count": 42
            }
        """

        private const val audioCollectionJson = """
            { "audio": [$audioJson] }
        """

        private const val singleAudioJson = """
            { "audio": $audioJson }
        """
    }
}
