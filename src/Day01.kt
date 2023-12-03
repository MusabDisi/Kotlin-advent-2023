fun main() {

    val solver = Day01Solver()
    val input = readInput("Day01")

    fun part1(input: List<String>) = solver.solvePart1(input) // idk if it is a must to use these functions
    fun part2(input: List<String>) = solver.solvePart2(input)

    println(part1(input))
    println(part2(input))
}

class Day01Solver {

    private val maxWordSize = 5
    private val possibleInitials = setOf('o', 't', 'f', 's', 'e', 'n')
    private val numbersInWords = mapOf(
            "one" to '1',
            "two" to '2',
            "three" to '3',
            "four" to '4',
            "five" to '5',
            "six" to '6',
            "seven" to '7',
            "eight" to '8',
            "nine" to '9'
    )

    fun solvePart1(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            val extractedNumber = extractDigits1(line)
            sum += extractedNumber
        }
        return sum
    }

    fun solvePart2(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            val extractedNumber = extractDigits2(line)
            sum += extractedNumber
        }
        return sum
    }

    private fun extractDigits1(line: String): Int {
        var firstDigit: Char? = null
        var secondDigit: Char? = null
        var p = 0
        while (p < line.length) {
            val current = line[p++]
            if (current.isDigit()) {
                if (firstDigit == null) {
                    firstDigit = current
                    secondDigit = current
                } else {
                    secondDigit = current
                }
            }
        }
        return if (firstDigit == null) 0 else "$firstDigit$secondDigit".toInt()
    }

    private fun extractDigits2(line: String): Int {
        var firstDigit: Char? = null
        var secondDigit: Char? = null

        var p1 = -1
        var p2: Int
        var tempWord = ""
        while (p1 < line.length - 1) {
            val currentChar = line[++p1]

            // if already a number
            if (currentChar.isDigit()) {
                if (firstDigit == null) {
                    firstDigit = currentChar
                    secondDigit = currentChar
                } else {
                    secondDigit = currentChar
                }
                continue
            }

            // if char but cannot be an initial of a number
            if (!possibleInitials.contains(currentChar)) continue

            // if a char that is a possible initial of a number
            tempWord += line[p1]
            p2 = p1 + 1
            while (p2 < line.length && p2 - p1 < maxWordSize) {
                tempWord += line[p2++]
                val number = numbersInWords[tempWord]
                if (number != null) {
                    if (firstDigit == null) {
                        firstDigit = number
                        secondDigit = number
                    } else {
                        secondDigit = number
                    }
                    break
                }
            }
            tempWord = ""
        }

        return if (firstDigit == null) 0 else "$firstDigit$secondDigit".toInt()
    }
}