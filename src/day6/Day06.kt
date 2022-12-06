package day6

import readInput
import kotlin.streams.asSequence

fun main() {
    val size = 14
    val first = readInput("day6/Day06")[0].chars()
        .asSequence()
        .windowed(size)
        .mapIndexed { index, values ->  if (values.toSet().size == size) index else null}
        .filterNotNull()
        .first() + size
    println(first)
}
