package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    val sOccurrencesAndCorrectness = getOccurrencesAndCorrectness(secret, guess)
    val guessLetterOccurrences = getLetterOccurrences(guess)
    var rightPosition = 0
    var wrongPosition = 0
    for ((ch, pair) in sOccurrencesAndCorrectness) {
        rightPosition += pair.second
        val guessedOc = numOfGuessedOc(pair.first, guessLetterOccurrences[ch] ?: 0)
        wrongPosition += guessedOc - pair.second
    }
    return Evaluation(rightPosition, wrongPosition)
}

fun numOfGuessedOc(secretOc: Int, guessOc: Int) : Int {
    return if (secretOc > guessOc) guessOc else secretOc
}
fun Boolean.toInt() = if (this) 1 else 0

fun getOccurrencesAndCorrectness(secret: String, guess: String) : Map<Char, Pair<Int, Int>> {
    val occurrences: MutableMap<Char, Pair<Int, Int>> = mutableMapOf()
    val charsSecret = secret.toCharArray();
    val charsGuess= guess.toCharArray();
    for ((index, ch) in charsSecret.withIndex()) {
        val correct = ch == charsGuess[index]
        // Pair? var has to be assigned to variable in order for compiler to smart-cast it
        // into Pair - probably because of possible parallelization access to data
        val pair = occurrences[ch]
        if (pair == null) {
            occurrences[ch] = Pair(1, correct.toInt())
        } else {
            occurrences[ch] = Pair(pair.first.plus(1), pair.second.plus(correct.toInt()))

        }
    }
    return occurrences
}

fun getLetterOccurrences(str: String) : Map<Char,Int> {
    val occurrences: MutableMap<Char, Int> = mutableMapOf()
    val chars = str.toCharArray();
    for (ch in chars) {
        if (occurrences[ch] == null) {
            occurrences[ch] = 1
        } else {
            occurrences[ch] = occurrences[ch]!! + 1
        }
    }
    return occurrences
}
