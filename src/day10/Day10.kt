package day10

import readInput


fun main() {
    val processor = Processor()
    val result = readInput("day10/Day10")
        .map(processor::executeCommand)
        .sum()

    println("Sum is $result")
}

private class Processor {
    private var registry: Int = 1
    private var cycle = 0

    fun executeCommand(line: String): Int {
        return when {
            line == "noop" -> cycle()
            line.startsWith("addx ") -> {
                val value1 = cycle()
                val value2 = cycle()
                registry += line.substring(5).toInt()
                value1 + value2
            }

            else -> error("Unable to process '$line'")
        }
    }

    fun cycle(): Int {
        cycle++
        val result = if ((cycle - 20) % 40 == 0) cycle * registry else 0
        return result
    }

}