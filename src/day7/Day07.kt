package day7

import readInput

private val root = Directory(null, "/")

fun main() {

    var currentDirectory = root
    val content = readInput("day7/Day07_test").joinToString(separator = "|")
    content.split("$").asSequence()
        .map { it.trim() }
        .filterNot { it.isBlank() }
        .map { if (it.endsWith("|")) it.dropLast(1) else it }
        .onEach { println("Line: $it") }
        .forEach { currentDirectory = executeCommand(currentDirectory, it) }

    println()
    println("----------------------------")
    root.printTree("")
    println("----------------------------")
    println()

    val sizes = root.totalSize().sorted().reversed()
    val sum = sizes
        .filter { it < 100000 }
        .sum()
    println("Sum of folder under 100000 is $sum")

    sizes.forEach { println(it) }
    val totalSpace = 70000000
    val neededSpace = 30000000 - (totalSpace - sizes.first())
    println("Needed: $neededSpace")
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
        println("$prefix- $name (dir)")
        directories.forEach { it.printTree("  $prefix") }
        files.forEach { println("$prefix  - ${it.name} (file, size=${it.size})") }
    }

    fun totalSize(): List<Int> {
        val subSizes = directories.flatMap { it.totalSize() }
        val fileSizes = files.sumOf { it.size }
        val localSize = subSizes.sum() + fileSizes
        return subSizes + localSize
    }
}


private data class File(val name: String, val size: Int)