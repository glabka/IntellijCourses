package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    val secretChars = secret.map { it }
    val guessChars = guess.map { it }
    val numOfSecretChars: Map<Char, Int> =
        secretChars.associateWith { character -> secretChars.filter { it == character }.size }
    val numOfGuessChars: Map<Char, Int> =
        guessChars.associateWith { character -> guessChars.filter { it == character }.size }
    val sameChars = secretChars.zip(guessChars).filter { (sCh, gCh) -> sCh == gCh }
    val numOfCorrectChars: Map<Char, Int> =
        secretChars.associateWith { ch -> sameChars.filter { (character, _) -> character == ch }.size }
    val numOfWrongPositions: Map<Char, Int> = numOfSecretChars.toList().associateBy({ it.first },
        {
            Integer.min(
                (numOfGuessChars[it.first] ?: 0) - (numOfCorrectChars[it.first] ?: 0),
                (it.second - (numOfCorrectChars[it.first] ?: 0))
            )
        })

    val rightPosition = numOfCorrectChars.values.sum()
    val wrongPosition = numOfWrongPositions.values.sum()

    return Evaluation(rightPosition, wrongPosition)
}