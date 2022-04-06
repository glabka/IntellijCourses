package nicestring

fun String.isNice(): Boolean {
        return listOf(this.notBString().toInt(), this.containsVowels().toInt(),
            this.containsDoubleLetter().toInt()).sum() >= 2
}

fun Boolean.toInt(): Int = if (this) 1 else 0

fun String.notBString() : Boolean = !this.contains(""".*(bu|ba|be).*""".toRegex())

fun String.containsVowels() : Boolean = this.contains("""(.*[aeiou].*){3,}""".toRegex())

fun String.containsDoubleLetter() : Boolean = this.contains(""".*([a-z])\1.*""".toRegex())