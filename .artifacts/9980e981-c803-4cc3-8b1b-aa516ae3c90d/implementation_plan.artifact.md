# Migration from SQLiteOpenHelper to Room Database

This plan outlines the steps to migrate the existing manual SQLite implementation in `MusicDatabaseHelper` to the Room Persistence Library. Room provides a cleaner API, compile-time SQL verification, and better integration with modern Android components.

## User Review Required

> [!IMPORTANT]
> The migration will replace the existing `MusicDatabaseHelper`. Any direct usages of this class throughout the app will need to be updated to use the new Repository/DAO structure. I will identify these usages during the execution phase.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/Users/jtrbg/AndroidStudioProjects/CST438-Project1-Team5/gradle/libs.versions.toml)
* Add Room and KSP versions.
* Add Room library definitions.
* Add KSP plugin definition.

#### [MODIFY] [build.gradle.kts (app)](file:///C:/Users/jtrbg/AndroidStudioProjects/CST438-Project1-Team5/app/build.gradle.kts)
* Apply the KSP plugin.
* Add Room dependencies.

---

### Database Layer

#### [NEW] [UserEntity.kt](file:///C:/Users/jtrbg/AndroidStudioProjects/CST438-Project1-Team5/app/src/main/java/com/example/cst438_project1_team5/database/entities/UserEntity.kt)
* Define the `users` table with Room annotations.
* Mirror the existing schema (username, email, password hash/salt, etc.).

#### [NEW] [SongEntity.kt](file:///C:/Users/jtrbg/AndroidStudioProjects/CST438-Project1-Team5/app/src/main/java/com/example/cst438_project1_team5/database/entities/SongEntity.kt)
* Define the `user_song_list` table.
* Include foreign key relationship to `UserEntity`.

#### [NEW] [ChallengeSongEntity.kt](file:///C:/Users/jtrbg/AndroidStudioProjects/CST438-Project1-Team5/app/src/main/java/com/example/cst438_project1_team5/database/entities/ChallengeSongEntity.kt)
* Define the `daily_challenge_history` table.

#### [NEW] [UserDao.kt](file:///C:/Users/jtrbg/AndroidStudioProjects/CST438-Project1-Team5/app/src/main/java/com/example/cst438_project1_team5/database/dao/UserDao.kt)
* Queries for finding users by username/email and updating login state.

#### [NEW] [SongDao.kt](file:///C:/Users/jtrbg/AndroidStudioProjects/CST438-Project1-Team5/app/src/main/java/com/example/cst438_project1_team5/database/dao/SongDao.kt)
* Methods for adding/removing songs and marking favorites.

#### [NEW] [ChallengeDao.kt](file:///C:/Users/jtrbg/AndroidStudioProjects/CST438-Project1-Team5/app/src/main/java/com/example/cst438_project1_team5/database/dao/ChallengeDao.kt)
* Methods for recording and retrieving challenge history.

#### [NEW] [AppDatabase.kt](file:///C:/Users/jtrbg/AndroidStudioProjects/CST438-Project1-Team5/app/src/main/java/com/example/cst438_project1_team5/database/AppDatabase.kt)
* The main Room database class providing access to DAOs.

---

### Repository Layer

#### [NEW] [UserRepository.kt](file:///C:/Users/jtrbg/AndroidStudioProjects/CST438-Project1-Team5/app/src/main/java/com/example/cst438_project1_team5/database/UserRepository.kt)
* Encapsulate complex logic currently in `MusicDatabaseHelper` (password hashing, registration validation).
* Provide a clean API for the UI layer.

#### [NEW] [MusicRepository.kt](file:///C:/Users/jtrbg/AndroidStudioProjects/CST438-Project1-Team5/app/src/main/java/com/example/cst438_project1_team5/database/MusicRepository.kt)
* Handle song and challenge data management using the new DAOs.

---

### Cleanup

#### [DELETE] [MusicDatabaseHelper.kt](file:///C:/Users/jtrbg/AndroidStudioProjects/CST438-Project1-Team5/app/src/main/java/com/example/cst438_project1_team5/database/MusicDatabaseHelper.kt)
* Remove the legacy SQLite implementation once the migration is complete and verified.

## Verification Plan

### Automated Tests
* Run existing unit tests (if any) to ensure data logic remains sound.
* Create new Room-specific instrumented tests to verify DAO operations.

### Manual Verification
* Deploy the app and verify that user registration and login still work correctly.
* Verify that songs can be added to the list and marked as favorites.
* Check that daily challenge history is correctly recorded and displayed.
