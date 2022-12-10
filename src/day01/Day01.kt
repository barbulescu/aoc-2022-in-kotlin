package day01

import readInput

fun main() {
    var groupTotal = 0
    val groups = buildList<Int> {
        readInput("day01/Day01")
            .forEach { line -> groupTotal = processLine(line, groupTotal) }
        add(groupTotal)
    }
        .sorted()
        .reversed()

    println("Max: ${groups.first()}")
    val sumOfTopThree = groups.take(3)
        .sum()
    println("Sum of top 3: $sumOfTopThree")
}

private fun MutableList<Int>.processLine(line: String, groupTotal: Int): Int {
    return if (line.isBlank()) {
        this.add(groupTotal)
        0
    } else {
        groupTotal + line.toInt()
    }
}
