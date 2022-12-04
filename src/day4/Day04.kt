package day4

import readInput

fun main() {
    val pairs = readInput("day4/Day04")
        .map { it.split(",") }
        .onEach { require(it.size == 2) { "expected 2 ranges" } }
        .map { parseRange(it[0]) to parseRange(it[1]) }
    val fullyOverlapped = pairs
        .count(::isFullyOverlapped)

    println("Fully included $fullyOverlapped")

    val partiallyOverlapped = pairs
        .count { it.first.intersect(it.second).isNotEmpty() }

    println("Partially included $partiallyOverlapped")
}

fun parseRange(range: String): IntRange {
    val parts = range.split("-")
    val from = parts[0].toInt()
    val to = parts[1].toInt()
    return from.rangeTo(to)
}

fun isFullyOverlapped(ranges: Pair<IntRange, IntRange>) : Boolean {
    val first = ranges.first.toSortedSet()
    val second = ranges.second.toSortedSet()
    val intersect = first.intersect(second)
    return intersect == first || intersect == second
}
