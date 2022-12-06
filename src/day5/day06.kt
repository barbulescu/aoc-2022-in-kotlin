package day5

import readInput
import java.lang.IllegalArgumentException

fun main() {
    val lines = readInput("day5/Day05")
    val (stackLines, operationLines) = splitLines(lines)

    val stacks = buildStacks(stackLines)

    buildOperations(operationLines, stacks.size())
        .forEach(stacks::operate)

    println(stacks.top())
}

private data class Operation(val quantity: Int, val from: Int, val to: Int)

private fun buildOperations(operationLines: List<String>, stackSize: Int): List<Operation> = operationLines
    .map { buildOperation(it) }
    .onEach { require(it.from in 0 until stackSize) { "invalid operation 'from' value on $it" } }
    .onEach { require(it.to in 0 until stackSize) { "invalid operation 'to' value on $it" } }


private fun buildOperation(line: String): Operation {
    val matches = "move (\\d+) from (\\d+) to (\\d+)".toRegex().matchEntire(line)?.groupValues
        ?: throw IllegalArgumentException("Unable to parse '$line'")
    return Operation(matches[1].toInt(), matches[2].toInt() - 1, matches[3].toInt() - 1)
}


private data class Stacks(private val stacks: List<ArrayDeque<String>>) {
    fun size(): Int = stacks.size

    fun operate(operation: Operation): Stacks {
        val buffer = mutableListOf<String>()
        repeat(operation.quantity) {
            val value = stacks[operation.from].removeFirst()
            buffer.add(value)
        }
        stacks[operation.to].addAll(0, buffer)
        return this
    }

    fun top(): String = stacks.joinToString(separator = "") { it.first() }
}

private fun buildStacks(stackLines: List<String>): Stacks {
    val size = stackLines.last().split("  ").size
    val parsedStackLines = stackLines
        .map { line -> parseStackLine(line, size) }
        .dropLast(1)
    val stacks = (0 until size)
        .map { column ->
            parsedStackLines
                .map { it[column] }
                .map(Char::toString)
                .filter(String::isNotBlank)
        }
        .map(::ArrayDeque)

    return Stacks(stacks)
}

private fun parseStackLine(line: String, size: Int): List<Char> {
    val chars = line.toCharArray()
    var index = 1
    return buildList {
        repeat(size) {
            add(chars[index])
            index += 4
        }
    }
}

private fun splitLines(lines: List<String>): Pair<List<String>, List<String>> {
    val separatorIndex = lines.indexOf("")
    val stackLines = lines.take(separatorIndex)
    val operationLines = lines.drop(separatorIndex + 1)
    return stackLines to operationLines
}
