package com.example.cst438_project1_team5.audio.cache

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import java.io.File
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CacheAudioTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private lateinit var server: MockWebServer
    private lateinit var cacheDir: File
    private lateinit var cacheAudio: CacheAudio

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        cacheDir = temporaryFolder.newFolder("audio-cache")
        cacheAudio = CacheAudio(cacheDir)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun getOrFetch_downloadsAndThenReusesTheCachedFile() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(200).setBody("audio-bytes"))
        val url = server.url("tracks/theme.mp3").toString()

        val downloaded = cacheAudio.getOrFetch(url)
        val cached = cacheAudio.getOrFetch(url)

        assertEquals("audio-bytes", downloaded.readText())
        assertEquals(downloaded, cached)
        assertEquals(1, server.requestCount)
    }

    @Test(expected = IOException::class)
    fun getOrFetch_throwsForAnUnsuccessfulResponse() {
        server.enqueue(MockResponse().setResponseCode(404))

        runBlocking {
            cacheAudio.getOrFetch(server.url("missing.mp3").toString())
        }
    }

    @Test
    fun evict_removesOnlyTheNamedCachedFile() = runBlocking {
        server.enqueue(MockResponse().setBody("first"))
        server.enqueue(MockResponse().setBody("second"))
        val firstUrl = server.url("first.mp3").toString()
        val secondUrl = server.url("second.mp3").toString()
        val firstFile = cacheAudio.getOrFetch(firstUrl)
        val secondFile = cacheAudio.getOrFetch(secondUrl)

        assertTrue(cacheAudio.evict(firstUrl))
        assertFalse(firstFile.exists())
        assertTrue(secondFile.exists())
        assertFalse(cacheAudio.evict(firstUrl))
    }

    @Test
    fun clear_removesEveryCachedFile() {
        File(cacheDir, "one.mp3").writeText("one")
        File(cacheDir, "two.mp3").writeText("two")

        assertTrue(cacheAudio.clear())
        assertTrue(cacheDir.listFiles().isNullOrEmpty())
    }

    @Test
    fun getUriFromFilename_returnsTheFilesUri() {
        val cachedFile = File(cacheDir, "theme.mp3")

        val uri = cacheAudio.getUriFromFilename(cachedFile)

        assertEquals("file", uri.scheme)
        assertEquals(cachedFile.absolutePath, uri.path)
    }
}
