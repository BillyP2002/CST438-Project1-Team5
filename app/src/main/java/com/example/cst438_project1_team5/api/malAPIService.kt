interface ApiService{
    //Fetch MAL user's list by their username
    @GET("users/{user_name}/animelist")
    suspend fun getUser(
        @Path("user_name") username: String,
        @Query("limit") limit: Int = 700,
        @Query("offset") offset: Int = 0,
    ): MalAnimeListResponse
}