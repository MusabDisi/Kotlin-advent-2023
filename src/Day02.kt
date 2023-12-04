import kotlin.math.max

fun main() {

    val solver = Day02Solver()
    val input = readInput("Day02")

    fun part1(input: List<String>) = solver.solvePart1(input) // idk if it is a must to use these functions
    fun part2(input: List<String>) = solver.solvePart2(input)

    println(part1(input))
    println(part2(input))
}

class Day02Solver {

    fun solvePart1(input: List<String>): Int {
        var sum = 0
        for (idx in input.indices) {
            if (isValidGame(input[idx])) {
                sum += (idx + 1)
            }
        }
        return sum
    }

    fun solvePart2(input: List<String>): Int {
        var sum = 0
        for (idx in input.indices) {
            val colors = getMaxRGB(input[idx])
            sum += (colors.red * colors.blue * colors.green)
        }
        return sum
    }

    private fun isValidGame(line: String): Boolean {
        val colors = getMaxRGB(line)
        return colors.red <= 12 && colors.green <= 13 && colors.blue <= 14
    }
    private fun getMaxRGB(line: String): ColorsGroup {
        val setsString = line.substringAfterLast(':')
        var maxRed = 0
        var maxGreen = 0
        var maxBlue = 0
        var p = 0
        var tempNum = ""
        var skipChar = true
        while (p < setsString.length) {
            val current = setsString[p]
            if (current.isDigit()) {
                tempNum += current
                skipChar = false
            } else if (!skipChar) {
                if (current in setOf('r', 'g', 'b')) {
                    when (current) {
                        'r' -> {
                            maxRed = max(maxRed, tempNum.toInt())
                        }

                        'g' -> {
                            maxGreen = max(maxGreen, tempNum.toInt())
                        }

                        'b' -> {
                            maxBlue = max(maxBlue, tempNum.toInt())
                        }
                    }
                    tempNum = ""
                    skipChar = true
                }
            }
            p++
        }
        return ColorsGroup(red = maxRed, green = maxGreen, blue = maxBlue)
    }

    data class ColorsGroup(
        val red: Int,
        val green: Int,
        val blue: Int
    )
}