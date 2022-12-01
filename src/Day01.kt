fun main() {
    var maxTotal = 0
    var currentTotal = 0
    readInput("Day01").forEach {line ->
        if (line.isBlank()) {
            if (currentTotal > maxTotal) {
                maxTotal = currentTotal
            }
            currentTotal = 0
        } else {
            currentTotal += line.toInt()
        }
    }
    println(maxTotal)
}
