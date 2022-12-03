import kotlin.streams.toList

fun main() {
    val total = readInput("Day03")
        .asSequence()
        .map(::toPriorities)
        .map { it.chunked(it.size / 2) }
        .onEach { require(it[0].size == it[1].size) }
        .map { it[0].intersect(it[1].toSet()) }
        .onEach { require(it.size == 1) }
        .sumOf { it.first() }

    println("Total: $total")

    val badgeTotal = readInput("Day03")
        .asSequence()
        .map(::toPriorities)
        .chunked(3)
        .onEach { require(it.size == 3) { "each group must have 3 elves" } }
        .map { findBadge(it) }
        .onEach { require(it.size == 1) { "only 1 badge must be common" } }
        .map { it.first() }
        .sum()

    println("Badge total: $badgeTotal")
}

fun findBadge(group: List<List<Int>>): Set<Int> =
    group[0].intersect(group[1].toSet()).intersect(group[2].toSet())

fun split(line: String): Pair<String, String> {
    val middle = line.length / 2
    return line.substring(0, middle) to line.substring(middle)
}

fun toPriorities(line: String): List<Int> {
    return line.chars()
        .map { toPriority(it) }
        .toList()
}

fun toPriority(value: Int): Int {
    return when (value) {
        in 97..122 -> value - 96
        in 65..90 -> value - 38
        else -> error("Value $value not supported")
    }
}
