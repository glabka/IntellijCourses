package rationals

import org.w3c.dom.ranges.Range
import java.math.BigInteger

class Rational(numerator: BigInteger, denominator: BigInteger) {
    var numerator: BigInteger = BigInteger.ONE
        private set

    var denominator: BigInteger = BigInteger.ONE
        private set

    init {
        if (denominator == BigInteger.ZERO) throw IllegalArgumentException()

        // moving possible negative sign from denominator to numerator or dropping it completely if both are negative
        val numNegative = numerator.compareTo(BigInteger.ZERO) < 0
        val denomNegative = denominator.compareTo(BigInteger.ZERO) < 0

        this.numerator = numerator
        this.denominator = denominator

//        println("numNegative: $numNegative")
//        println("denomNegative: $denomNegative")

        if (numNegative && denomNegative) {
            this.numerator = this.numerator.abs()
            this.denominator = this.denominator.abs()
        } else if (denomNegative) {
            this.numerator = this.numerator.negate()
            this.denominator = this.denominator.negate()
        }

        // dividing by greatest common divisor
        val gcd = numerator.gcd(denominator)
//        println("gcd: $gcd")
//        println("this.numerator before gcd ${this.numerator}")
//        println("this.denominator before gcd ${this.denominator}")
        this.numerator = this.numerator / gcd
        this.denominator = this.denominator / gcd
//        println("this.numerator ${this.numerator}")
//        println("this.denominator ${this.denominator}")
    }

    constructor(numerator: Int, denominator: Int): this(numerator.toBigInteger(), denominator.toBigInteger())
    constructor(numerator: Long, denominator: Long): this(numerator.toBigInteger(), denominator.toBigInteger())

    operator fun times(rational: Rational): Rational {
        val numerator = this.numerator * rational.numerator
        val denominator = this.denominator * rational.denominator
        return Rational(numerator, denominator)
    }
   
    operator fun unaryMinus(): Rational = Rational(-this.numerator, this.denominator)
   
    // short implementation 
    operator fun minus(that: Rational): Rational = this + (-that)
        
    // naive implementation
    operator fun plus(that: Rational): Rational {
        val thisNumerator = this.numerator * that.denominator
        val thatNumerator = that.numerator * this.denominator
        return Rational(thisNumerator + thatNumerator, this.denominator * that.denominator)
    }

    operator fun div(that: Rational): Rational {
        val numerator = this.numerator * that.denominator
        val denominator = this.denominator * that.numerator
        return Rational(numerator, denominator)
    }

    operator fun compareTo(other: Rational): Int {
        if (this == other) return 0
        val thisNum = this.numerator * other.denominator
        val otherNum = other.numerator * this.denominator

        return if (thisNum > otherNum) 1 else -1
    }

    operator fun rangeTo(toNum: Rational) : RationalNumberRange = RationalNumberRange(this, toNum)

    override fun toString(): String {
        if (denominator == BigInteger.ONE)
            return numerator.toString()
        else
            return numerator.toString() + "/" + denominator.toString()

    }
    
    override fun equals(other: Any?): Boolean {
        if (other !is Rational) return false
        if (this === other) return false
        if (this.numerator != other.numerator) return false
        if (this.denominator != other.denominator) return false
        return true
    }

    override fun hashCode(): Int {
        return numerator.hashCode() + denominator.hashCode()
    }
}

data class RationalNumberRange(val fromNum: Rational, val toNum: Rational) {
//    init {
//        if (fromNum.denominator != toNum.denominator) throw IllegalArgumentException("Denominator of rational numbers" +
//                fromNum.toString() + " and " + toNum.toString() + " are different.")
//    }

    operator fun contains(rational: Rational): Boolean
    = rational >= fromNum && rational <= toNum
}

fun String.toRational(): Rational {
    val splittedStr = this.split("""/""")
    if (splittedStr.size != 2 && splittedStr.size != 1) throw IllegalArgumentException("Not in the rational number format: " + this)

    val numerator = splittedStr[0].toBigInteger()
    val denominator = if (splittedStr.size == 1) BigInteger.ONE else splittedStr[1].toBigInteger()

    return Rational(numerator, denominator)
}

infix fun BigInteger.divBy(denominator: BigInteger): Rational = Rational(this, denominator)
infix fun BigInteger.divBy(denominator: Long): Rational = Rational(this, denominator.toBigInteger())
infix fun BigInteger.divBy(denominator: Int): Rational = Rational(this, denominator.toBigInteger())

infix fun Long.divBy(denominator: Long): Rational = Rational(this, denominator)
infix fun Long.divBy(denominator: BigInteger): Rational = Rational(this.toBigInteger(), denominator)
infix fun Long.divBy(denominator: Int): Rational = Rational(this.toBigInteger(), denominator.toBigInteger())

infix fun Int.divBy(denominator: Int): Rational = Rational(this, denominator)
infix fun Int.divBy(denominator: BigInteger): Rational = Rational(this.toBigInteger(), denominator)
infix fun Int.divBy(denominator: Long): Rational = Rational(this.toBigInteger(), denominator.toBigInteger())

