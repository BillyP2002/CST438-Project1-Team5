package com.example.cst438_project1_team5.database

import com.example.cst438_project1_team5.database.dao.ChallengeDao
import com.example.cst438_project1_team5.database.dao.SongDao
import com.example.cst438_project1_team5.database.entities.ChallengeSongEntity
import com.example.cst438_project1_team5.database.entities.SongEntity

class MusicRepository(private val songDao: SongDao, private val challengeDao: ChallengeDao) {

    suspend fun addSongToUserList(
        userId: Long,
        songId: String,
        title: String,
        artist: String,
        album: String? = null
    ): Long {
        val song = SongEntity(
            userId = userId,
            songId = songId,
            title = title,
            artist = artist,
            album = album
        )
        return songDao.insertSong(song)
    }

    suspend fun addSongToFavorites(userId: Long, songId: String): Int =
        songDao.markFavorite(userId, songId)

    suspend fun getUserSongList(userId: Long): List<SongEntity> =
        songDao.getUserSongs(userId)

    suspend fun recordDailyChallengeSong(
        userId: Long,
        challengeDate: String,
        songId: String,
        title: String,
        artist: String,
        album: String? = null
    ): Long {
        val challenge = ChallengeSongEntity(
            userId = userId,
            challengeDate = challengeDate,
            songId = songId,
            title = title,
            artist = artist,
            album = album
        )
        return challengeDao.recordChallenge(challenge)
    }

    suspend fun getPastPlayedSongsForChallenge(
        userId: Long,
        challengeDate: String
    ): List<ChallengeSongEntity> =
        challengeDao.getChallengeHistory(userId, challengeDate)
}
