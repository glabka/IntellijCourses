package taxipark

import kotlin.math.floor

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        allDrivers.filter { driver -> trips.none { trip -> trip.driver == driver } }.toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
    allPassengers.filter { passenger ->
        trips.count {trip -> trip.passengers.contains(passenger) } >= minTrips}.toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    allPassengers.filter { passenger -> trips.filter {
            trip -> trip.passengers.contains(passenger) && trip.driver == driver }.size > 1 }.toSet()
/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
    allPassengers.filter { passenger ->
        val (disTrips, nonDistTrips) = trips.filter { trip -> trip.passengers.contains(passenger) }
        .partition { trip -> trip.discount ?: 0.0 > 0.0 }
        disTrips.size > nonDistTrips.size
    }.toSet()
/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    if (trips == null || trips.isEmpty()) return null
    return allRanges().filterNotNull().maxBy { range ->
        trips.count {trip -> trip.duration in range} }
}

fun TaxiPark.allRanges(): MutableList<IntRange?> {
    fun Int.toDecimal() : Int {
        return this + (10 - (this % 10))
    }
    val maxRange = trips.maxBy { it.duration }?.duration?.toDecimal() ?: 0
    val ranges: MutableList<IntRange?> = mutableListOf()
    for (rangeStart in 0..maxRange step 10) {
        ranges.add(rangeStart until rangeStart + 10)
    }
    return ranges
}
/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips == null || trips.isEmpty()) return false
    val numOfTopDrivers = floor(allDrivers.size * 0.2)
    val sortedDriversAndCosts = allDrivers.associateWith { driver -> trips.filter {trip -> trip.driver == driver}
        .sumByDouble { trip -> trip.cost }}.toList().sortedBy { (_, costs) -> costs }
    val (restOfDrivers, topDrivers) = sortedDriversAndCosts.withIndex().partition { (index, _) -> index < allDrivers.size - numOfTopDrivers }
    println(restOfDrivers)
    println(topDrivers)
    val allCosts = sortedDriversAndCosts.sumByDouble { (_, costs) -> costs }
    return topDrivers.sumByDouble { indexedValue -> indexedValue.value.second} >= allCosts * 0.8

}