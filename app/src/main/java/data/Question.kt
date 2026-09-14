package data

class Question(val show: String){
    var guesses = 0
    var duration = 1

    /**
     * Returns whether the user's guess was successful, and a getter can be used to
     * receive the current guess count.
     * The UI will likely have to specifically account for receiving a failure value.
     * A skip input should be available
     */
    fun guess(answer: String) : Boolean{
        if(guesses < 5){
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
            guesses++
            if (answer.uppercase().equals(show.uppercase())){
                return true
            }
        }
        return false;
    }
}