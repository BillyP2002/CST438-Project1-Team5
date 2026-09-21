package com.example.cst438_project1_team5.audio.cache

import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Builds a cache of audio files for the game.
 * Includes functions to check the cache for audio files,
 * delete single files, and delete the entire cache.
 * @param cacheDir File
 */
class CacheAudio (private val cacheDir: File) {
    // builds Okhttp client with extra timout padding
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * A function to get the filename for a passed url
     * @param url String
     * @return filename
     */
    private fun filenameFor(url: String): String =
        url.toHttpUrl().pathSegments.last()

    /**
     * A function that gets the Uri from the cached file in order
     * to build a MediaItem around the .setUri() function.
     * @param cachedFile File
     * @return Uri
     */
    fun getUriFromFilename(cachedFile: File): Uri {
        return Uri.fromFile(cachedFile)
    }

    /**
     * Async function that gets audio file from AnimeThemesAPI.
     * Downloads if not already cached, otherwise returns the
     * existing file.
     * It's a singleton for cached files.
     * @param url String
     * @return File
     */
    suspend fun getOrFetch(url: String): File = withContext(Dispatchers.IO){
        // uses client to get audio file from url
        val request = Request.Builder().url(url).build()

        // check if destFile is already cached
        val destFile = File(cacheDir, filenameFor(url))
        if (destFile.exists()) return@withContext destFile

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw java.io.IOException("Fetch failed: ${response.code}")

            // download & return new file if not cached yet
            destFile.outputStream().use { out ->
                response.body!!.byteStream().copyTo(out)
            }
            return@withContext destFile
        }
    }



    /**
     * Deletes a specific cached source (when round ends)
     * @return Boolean
     */
    fun evict(url: String): Boolean{
        val destFile: File = File(cacheDir, filenameFor(url))
        var deleted: Boolean = false
        if (destFile.exists()) {
            deleted = destFile.delete()
            if (!deleted) {
                Log.d("CacheAudio", "Error! Cached file not properly removed!")
            }
        }
        return deleted
    }

    /**
     * Deletes everything that was present in the cache (when game is over)
     */
    fun clear(): Boolean {
        cacheDir.listFiles()?.forEach { file ->
            file.delete()
        }
        val remaining = cacheDir.listFiles()
        val deleted: Boolean = remaining != null && remaining.isEmpty()
        return deleted
    }
}