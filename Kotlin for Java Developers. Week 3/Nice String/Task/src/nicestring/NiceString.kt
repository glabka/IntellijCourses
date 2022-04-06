package nicestring

fun String.isNice(): Boolean {
    val notBString : Boolean = setOf("bu", "ba", "be").none { this.contains(it) }

    val containsVowels : Boolean = this.count { it in "aeiou" } >= 3

    val containsDoubleLetter : Boolean = this.zipWithNext().any { it.first == it.second }

    return listOf(notBString, containsVowels, containsDoubleLetter).count { it } >= 2
}