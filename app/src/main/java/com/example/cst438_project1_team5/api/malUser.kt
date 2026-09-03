data class malUser(
    val id: Int,
    val name: String,
    val showsWatched: List<show>
)

data class show(
    val id: Int,
    val name: String,
    val completedStatus: String,
    val userRating: String
)