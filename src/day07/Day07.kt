package day07

import readInput

private val root = Directory(null, "/")

fun main() {

    var currentDirectory = root
    val content = readInput("day07/Day07").joinToString(separator = "|")
    content.split("$").asSequence()
        .map { it.trim() }
        .filterNot { it.isBlank() }
        .map { if (it.endsWith("|")) it.dropLast(1) else it }
        .onEach { println("Line: $it") }
        .forEach { currentDirectory = executeCommand(currentDirectory, it) }

    root.calculateTotalSize()

    println()
    println("----------------------------")
    root.printTree("")
    println("----------------------------")
    println()

    val sizes = mutableListOf<Int>()
    root.collectTotalSizes(sizes)

    val sum = sizes
        .filter { it < 100000 }
        .sum()
    println("Sum of folder under 100000 is $sum")

    val totalSpace = 70000000
    val necessarySpace = 30000000
    val currentFreeSpace = totalSpace - root.totalSize()
    val spaceToFree = necessarySpace - currentFreeSpace
    println("current free space: $currentFreeSpace and space to free up $spaceToFree")

    val directoryToDelete = sizes
        .filter { it >= spaceToFree }
        .minOf { it }
    println("Directory to delete: $directoryToDelete")
}

private fun executeCommand(currentDirectory: Directory, commandText: String): Directory =
    when {
        commandText == "cd /" -> root
        commandText == "cd .." -> currentDirectory.toParent()
        commandText.startsWith("cd") -> {
            val directoryName = commandText.drop(3)
            currentDirectory.toChild(directoryName)
        }

        commandText.startsWith("ls") -> {
            commandText.drop(3)
                .split("|")
                .forEach { command ->
                    println("Command: $command")
                    if (command.startsWith("dir")) {
                        currentDirectory.addDirectory(command.drop(4))
                    } else {
                        val parts = command.split(" ")
                        currentDirectory.addFile(parts[1], parts[0].toInt())
                    }
                }
            currentDirectory
        }

        else -> throw IllegalArgumentException("Unknow command $commandText")
    }

private class Directory(val parent: Directory?, val name: String) {
    private val files = mutableListOf<File>()
    private val directories = mutableListOf<Directory>()
    private var totalSize: Int = 0

    fun addFile(filename: String, size: Int): Directory {
        files.add(File(filename, size))
        return this
    }

    fun addDirectory(directoryName: String): Directory {
        directories.add(Directory(this, directoryName))
        return this
    }

    fun toParent(): Directory {
        return requireNotNull(parent) { "Already in root directory" }
    }

    fun toChild(directoryName: String): Directory {
        return directories
            .first { it.name == directoryName }
    }

    fun printTree(prefix: String) {
        println("$prefix- $name (dir, size=$totalSize)")
        directories.forEach { it.printTree("  $prefix") }
        files.forEach { println("$prefix  - ${it.name} (file, size=${it.size})") }
    }

    fun calculateTotalSize() {
        val subSizes = directories
            .onEach { it.calculateTotalSize() }
            .sumOf { it.totalSize }
        val fileSizes = files.sumOf { it.size }
        totalSize = subSizes + fileSizes
    }

    fun collectTotalSizes(collector: MutableList<Int>) {
        directories.forEach { it.collectTotalSizes(collector) }
        collector.add(this.totalSize)
    }

    fun totalSize() = totalSize
}


private data class File(val name: String, val size: Int)