package data

class Question(val show: String) {
    companion object {
        private const val MAX_GUESSES = 5
        private const val SECOND_GUESS = 1
        private const val THIRD_GUESS = 2
        private const val FOURTH_GUESS = 3
        private const val FIFTH_GUESS = 4
        private const val SECOND_GUESS_DURATION = 3
        private const val THIRD_GUESS_DURATION = 7
        private const val FOURTH_GUESS_DURATION = 14
        private const val FIFTH_GUESS_DURATION = 16
    }

    var guesses = 0
    var duration = 1

    /**
     * Returns whether the user's guess was successful, and a getter can be used to
     * receive the current guess count.
     * The UI will likely have to specifically account for receiving a failure value.
     * A skip input should be available
     */
    fun guess(answer: String): Boolean {
        if (guesses < MAX_GUESSES) {
            when (guesses) {
                SECOND_GUESS -> {
                    duration = SECOND_GUESS_DURATION
                }

                THIRD_GUESS -> {
                    duration = THIRD_GUESS_DURATION
                }

                FOURTH_GUESS -> {
                    duration = FOURTH_GUESS_DURATION
                }

                FIFTH_GUESS -> {
                    duration = FIFTH_GUESS_DURATION
                }
            }
            guesses++
            if (answer.uppercase().equals(show.uppercase())) {
                return true
            }
        }
        return false
    }
}
