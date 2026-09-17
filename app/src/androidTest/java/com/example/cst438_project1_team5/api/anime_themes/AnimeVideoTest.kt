package com.example.cst438_project1_team5.api.anime_themes

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class AnimeVideoTest {

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
    fun getVideo_usesDefaultQueryParameters_andMapsVideoWithItsAudio() = runTest {
        mockWebServer.enqueue(jsonResponse(videoCollectionJson))

        val response = api.getVideo()

        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/video", request.requestUrl?.encodedPath)
        assertEquals("audio", request.requestUrl?.queryParameter("include"))
        assertEquals("false", request.requestUrl?.queryParameter("filter[uncen]"))

        assertEquals(200, response.code())
        assertTrue(response.isSuccessful)
        val video = response.body()?.videos?.single()
        assertNotNull(video)
        assertEquals("Bakemonogatari-OP1.webm", video?.basename)
        assertEquals(720, video?.resolution)
        assertEquals(Source.WEB, video?.source)
        assertEquals(Overlap.None, video?.overlap)
        assertEquals(false, video?.uncen)
        assertEquals(
            "https://v.animethemes.moe/Bakemonogatari-OP1.webm",
            video?.link
        )
        assertEquals("Bakemonogatari-OP1.ogg", video?.audio?.basename)
        assertEquals(
            "https://a.animethemes.moe/Bakemonogatari-OP1.ogg",
            video?.audio?.link
        )
    }

    private fun jsonResponse(body: String): MockResponse = MockResponse()
        .setResponseCode(200)
        .addHeader("Content-Type", "application/json")
        .setBody(body)

    private companion object {
        private const val videoCollectionJson = """
            {
              "video": [
                {
                  "id": 7,
                  "basename": "Bakemonogatari-OP1.webm",
                  "filename": "Bakemonogatari-OP1",
                  "path": "video/Bakemonogatari-OP1.webm",
                  "size": 45678,
                  "mimetype": "video/webm",
                  "resolution": 720,
                  "nc": false,
                  "subbed": true,
                  "lyrics": false,
                  "uncen": false,
                  "source": "WEB",
                  "overlap": "None",
                  "tags": "OP",
                  "link": "https://v.animethemes.moe/Bakemonogatari-OP1.webm",
                  "views_count": 24,
                  "created_at": "2026-01-01T00:00:00.000Z",
                  "updated_at": "2026-01-02T00:00:00.000Z",
                  "deleted_at": null,
                  "audio": {
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
                }
              ]
            }
        """
    }
}
