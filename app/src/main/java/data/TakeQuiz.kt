package data

class TakeQuiz {
    var guesses = 0
    var duration = 1
    var answer : String = ""

    fun getDuration() : Int{
        return duration
    }

    /**
     * Returns the number of guesses the user took to get the show, or -1 if they failed.
     * The UI will likely have to specifically account for receiving a failure value.
     * A skip input should be available
     */
    fun Guess(show: String) : Int{
        while(guesses < 5){
            when (guesses) {
                1 -> {
                    duration = 3;
                }
                2 -> {
                    duration = 7;
                }
                3 -> {
                    duration = 14;
                }
                4 -> {
                    duration = 16;
                }
            }
            answer = readln()
            guesses++
            if (answer.uppercase().equals(show.uppercase())){
                return guesses
            }
        }
        return -1;
    }
}