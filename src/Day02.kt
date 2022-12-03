import Choice.*

fun main() {
    val total = readInput("Day02")
        .map { line -> line.split("\\s".toRegex()) }
        .map { BattleV1(it) }
        .sumOf { it.calculate() }

    println("Total: $total")

    val total2 = readInput("Day02")
        .map { line -> line.split("\\s".toRegex()) }
        .map { BattleV2(it) }
        .sumOf { it.calculate() }

    println("Total2: $total2")
}

private data class BattleV1(val opponent: Choice, val you: Choice) {
    constructor(line: List<String>) : this(line[0].firstPlayer(), line[1].secondPlayer())

    fun calculate(): Int = you.value + battle(you, opponent)
}

private data class BattleV2(val opponent: Choice, val you: Choice) {
    constructor(line: List<String>) : this(line[0].firstPlayer(), line[1].desiredOutcome(line[0].firstPlayer()))

    fun calculate(): Int = you.value + battle(you, opponent)
}

private fun battle(choice1: Choice, choice2: Choice): Int {
    return when {
        choice1 == choice2 -> 3
        choice1 == ROCK && choice2 == SCISSORS -> 6
        choice1 == SCISSORS && choice2 == PAPER -> 6
        choice1 == PAPER && choice2 == ROCK -> 6
        else -> 0
    }
}

private enum class Choice(val value: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3)
}

private fun String.firstPlayer(): Choice = when (this) {
    "A" -> ROCK
    "B" -> PAPER
    "C" -> SCISSORS
    else -> error("Invalid choice $this")
}

private fun String.secondPlayer(): Choice = when (this) {
    "X" -> ROCK
    "Y" -> PAPER
    "Z" -> SCISSORS
    else -> error("Invalid choice $this")
}

private fun String.desiredOutcome(opponent: Choice): Choice = when (this) {
    "X" -> opponent.losingValue()
    "Y" -> opponent
    "Z" -> opponent.winningValue()
    else -> error("Invalid choice $this")
}

private fun Choice.losingValue() : Choice {
    return when(this) {
        ROCK -> SCISSORS
        PAPER -> ROCK
        SCISSORS -> PAPER
    }
}

private fun Choice.winningValue() : Choice {
    return when(this) {
        ROCK -> PAPER
        PAPER -> SCISSORS
        SCISSORS -> ROCK
    }
}