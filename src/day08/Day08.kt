package day08

import readInput


fun main() {
    val lines: List<List<Int>> = readInput("day08/Day08")
        .map { line -> line.toCharArray().map { it.toString().toInt() } }
        .toList()

    val grid = Grid(lines)
        .markEdgeVisible()
        .markFromLeft()
        .markFromRight()
        .markFromTop()
        .markFromBottom()

    println("Visible: ${grid.countVisible()}")
    grid.calculateHighestScenicScore()
}

private class Grid(values: List<List<Int>>) {
    private val size = values.size
    private val storage: List<List<Position>> = values
        .onEach { require(it.size == size) }
        .map { row -> row.map { Position(it) } }

    override fun toString(): String {
        return storage.joinToString("\n") { it.toString() }
    }

    fun markEdgeVisible(): Grid {
        storage.first().forEach { it.visible = true }
        storage.last().forEach { it.visible = true }
        (0 until size).forEach { storage[it][0].visible = true }
        (0 until size).forEach { storage[it][size - 1].visible = true }
        return this
    }

    fun markFromLeft(): Grid {
        for (row in (0 until size)) {
            var max = 0
            for (col in (0 until size)) {
                max = storage[row][col].markVisible(max)
            }
        }
        return this
    }

    fun markFromRight(): Grid {
        for (row in (0 until size)) {
            var max = 0
            for (col in (size - 1 downTo 0)) {
                max = storage[row][col].markVisible(max)
            }
        }
        return this
    }

    fun markFromTop(): Grid {
        for (col in (0 until size)) {
            var max = 0
            for (row in (0 until size)) {
                max = storage[row][col].markVisible(max)
            }
        }
        return this
    }

    fun markFromBottom(): Grid {
        for (col in (0 until size)) {
            var previous = 0
            for (row in (size - 1 downTo 0)) {
                previous = storage[row][col].markVisible(previous)
            }
        }
        return this
    }

    fun countVisible(): Int {
        return storage
            .flatten()
            .count { it.visible }
    }

    fun calculateHighestScenicScore() {
        var highest = 0
        for (row in (1..size - 2)) {
            for (col in (1..size - 2)) {
                val score = calculateScenicScore(row, col)
                if (score > highest) {
                    highest = score
                }
            }
        }
        println("Highest score is $highest")
    }

    private fun calculateScenicScore(row: Int, col: Int): Int {
        val tree = storage[row][col].value
        val score1 = calculateScore(tree, (row - 1 downTo 0)) { storage[it][col] }
        val score2 = calculateScore(tree, (row + 1 until size)) { storage[it][col] }
        val score3 = calculateScore(tree, (col - 1 downTo 0)) { storage[row][it] }
        val score4 = calculateScore(tree, (col + 1 until size)) { storage[row][it] }
        val score = score1 * score2 * score3 * score4
        println("Score is $score for position ($row, $col) with $score1 $score2 $score3 $score4")
        return score
    }

    private fun calculateScore(tree: Int, range: IntProgression, valueExtractor: (Int) -> Position): Int {
        val values = range
            .map(valueExtractor)
            .map { it.value }

        values.forEachIndexed {index, value ->
            if (value >= tree) {
                return index + 1
            }
        }

        return values.size
    }
}


private class Position(val value: Int, var visible: Boolean = false) {
    override fun toString(): String {
        return "$value${visible.toString().take(1)}"
    }

    fun markVisible(max: Int): Int =
        if (value > max) {
            visible = true
            value
        } else {
            max
        }
}