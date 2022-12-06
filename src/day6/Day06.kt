package day6

import readInput
import kotlin.streams.asSequence

fun main() {
    val first = readInput("day6/Day06")[0].chars()
        .asSequence()
        .windowed(4)
        .mapIndexed { index, values ->  if (values.toSet().size == 4) index else null}
        .filterNotNull()
        .first() + 4
    println(first)
}
