package rationals

import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO

data class Rational private constructor(val numerator: BigInteger, val denominator: BigInteger) : Comparable<Rational> {

    companion object {
        fun createRational(numerator: BigInteger, denominator: BigInteger): Rational {
            require(denominator != ZERO) { "Denominator can't be zero." }

            // normalization
            val sign = denominator.signum().toBigInteger()

            // dividing by greatest common divisor
            val gcd = numerator.gcd(denominator)
            return Rational(numerator / gcd * sign, denominator / gcd * sign)
        }

        fun createRational(numerator: Int, denominator: Int): Rational {
            return createRational(numerator.toBigInteger(), denominator.toBigInteger())
        }

        fun createRational(numerator: Long, denominator: Long): Rational {
            return createRational(numerator.toBigInteger(), denominator.toBigInteger())
        }
    }

    operator fun times(rational: Rational): Rational
    = createRational(numerator * rational.numerator,  denominator * rational.denominator)

    operator fun unaryMinus(): Rational = createRational(-numerator, denominator)
   
    operator fun minus(that: Rational): Rational = createRational(
        numerator * that.denominator - that.numerator * denominator,
        denominator * that.denominator
    )
        
    operator fun plus(that: Rational): Rational = createRational(
       numerator * that.denominator + that.numerator * denominator,
       denominator * that.denominator)

    operator fun div(that: Rational): Rational = createRational(
       numerator * that.denominator,
        denominator * that.numerator)

// Range works automatically in kotlin when comparable is implemented (overrides Comparable interface method)
    override fun compareTo(other: Rational): Int {
        return (numerator * other.denominator - other.numerator * denominator).signum()
    }
// Range works automatically in kotlin when comparable is implemented
//    operator fun rangeTo(toNum: Rational) : RationalNumberRange = RationalNumberRange(this, toNum)

    override fun toString(): String {
        if (denominator == BigInteger.ONE)
            return numerator.toString()
        else
            return numerator.toString() + "/" + denominator.toString()

    }
}

// Range works automatically in kotlin when comparable is implemented - no special class is required
//data class RationalNumberRange(val fromNum: Rational, val toNum: Rational) {
//    operator fun contains(rational: Rational): Boolean
//    = rational >= fromNum && rational <= toNum
//}

fun String.toRational(): Rational {
    fun String.toBigIntegerOrFail() =
        toBigIntegerOrNull() ?: throw IllegalArgumentException(
        "Expecting rational in the form of 'numerator/denominator' or 'numerator', was: '${this@toRational}'"
    )
    if (!contains("/")) {
        return Rational.Companion.createRational(toBigIntegerOrFail(), ONE)
    }
    val (numeratorText, denominatorText) = this.split("""/""")
    return Rational.createRational(numeratorText.toBigIntegerOrFail(), denominatorText.toBigIntegerOrFail())
}

infix fun BigInteger.divBy(denominator: BigInteger): Rational = Rational.createRational(this, denominator)
infix fun BigInteger.divBy(denominator: Long): Rational = Rational.createRational(this, denominator.toBigInteger())
infix fun BigInteger.divBy(denominator: Int): Rational = Rational.createRational(this, denominator.toBigInteger())

infix fun Long.divBy(denominator: Long): Rational = Rational.createRational(this, denominator)
infix fun Long.divBy(denominator: BigInteger): Rational = Rational.createRational(this.toBigInteger(), denominator)
infix fun Long.divBy(denominator: Int): Rational = Rational.createRational(this.toBigInteger(), denominator.toBigInteger())

infix fun Int.divBy(denominator: Int): Rational = Rational.createRational(this, denominator)
infix fun Int.divBy(denominator: BigInteger): Rational = Rational.createRational(this.toBigInteger(), denominator)
infix fun Int.divBy(denominator: Long): Rational = Rational.createRational(this.toBigInteger(), denominator.toBigInteger())

