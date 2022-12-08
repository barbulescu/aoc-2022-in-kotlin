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

    println(grid)
    println("Visible: ${grid.countVisible()}")
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