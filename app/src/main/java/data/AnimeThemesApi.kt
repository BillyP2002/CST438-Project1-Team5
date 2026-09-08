package data

import com.example.cst438_project1_team5.ui.soundTest.SoundTest
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimeThemesApi {
    @GET("anime")
    suspend fun getRandomAnime(
        @Query("sort") sort: String = "random",
        @Query("page[size]") pageSize: Int = 1,
        @Query("include") include: String =
            "animethemes.song," +
                    "animethemes.animethemeentries.videos"
    ): SoundTest.AnimeIndexResponse
}