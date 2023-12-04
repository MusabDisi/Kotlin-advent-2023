fun main() {

    val solver = Day03Solver()
//    val input = readInput("Day03_test")
    val input = readInput("Day03")

    fun part1(input: List<String>) = solver.solvePart1(input) // idk if it is a must to use these functions
    fun part2(input: List<String>) = solver.solvePart2(input)

    println(part1(input))
    println(part2(input))
}

class Day03Solver { // this is messy will revisit if I have time, xd
    fun solvePart1(input: List<String>): Int {
        var sum = 0
        input.forEachIndexed { lineIndex, line ->
            var p = 0
            var numberStart = -1
            var numberEnd = -1
            while (p < line.length) {
                val current = line[p]
                if (current.isDigit()) {
                    if (numberStart == -1) {
                        numberStart = p
                        numberEnd = p
                    } else {
                        numberEnd = p
                    }
                }
                if ((p == line.length - 1 || !current.isDigit()) && numberEnd != -1) {
                    val touchesASymbol = touchesASymbol(input = input, lineIndex = lineIndex, startIndex = numberStart, endIndex = numberEnd)
                    if (touchesASymbol) {
                        sum += line.substring(numberStart, numberEnd + 1).toInt()
                    }
                    numberStart = -1
                    numberEnd = -1
                }
                p++
            }
        }
        return sum
    }

    fun solvePart2(input: List<String>): Int {
        val starsToNumber = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()
        var sum = 0
        input.forEachIndexed { lineIndex, line ->
            var p = 0
            var numberStart = -1
            var numberEnd = -1
            while (p < line.length) {
                val current = line[p]
                if (current.isDigit()) {
                    if (numberStart == -1) {
                        numberStart = p
                        numberEnd = p
                    } else {
                        numberEnd = p
                    }
                }
                if ((p == line.length - 1 || !current.isDigit()) && numberEnd != -1) {
                    val touchedStars = touchedStars(input = input, currentLineIndex = lineIndex, startIndex = numberStart, endIndex = numberEnd)
                    val num = line.substring(numberStart, numberEnd + 1).toInt()
                    for (star in touchedStars) {
                        starsToNumber[star]?.add(num) ?: starsToNumber.put(star, mutableListOf(num))
                    }
                    numberStart = -1
                    numberEnd = -1
                }
                p++
            }
        }
        for (starNumbers in starsToNumber.values) {
            if (starNumbers.size == 2) {
                sum += starNumbers[0] * starNumbers[1]
            }
        }
        return sum
    }

    private fun touchedStars(input: List<String>, currentLineIndex: Int, startIndex: Int, endIndex: Int): MutableList<Pair<Int, Int>> {
        val res = mutableListOf<Pair<Int, Int>>()

        //  check line above
        if (currentLineIndex > 0) {
            res.addAll(collectStarsInLine(input, currentLineIndex - 1, startIndex - 1, endIndex + 1))
        }

        // check same line
        res.addAll(collectStarsInLine(input, currentLineIndex, startIndex - 1, endIndex + 1))

        // check line below
        if (currentLineIndex < input.size - 1) {
            res.addAll(collectStarsInLine(input, currentLineIndex + 1, startIndex - 1, endIndex + 1))
        }

        return res
    }

    private fun collectStarsInLine(input: List<String>, lineIndex: Int, startIndex: Int, endIndex: Int): List<Pair<Int, Int>> {
        val res = mutableListOf<Pair<Int, Int>>()
        val line = input[lineIndex]
        for (idx in startIndex..endIndex) {
            if (idx >= 0 && idx < line.length - 1 && line[idx].isStar()) {
                res.add(lineIndex to idx)
            }
        }
        return res
    }

    private fun touchesASymbol(input: List<String>, lineIndex: Int, startIndex: Int, endIndex: Int): Boolean {

        val touchIndices = (startIndex - 1..endIndex + 1).toList()

        //  check line above
        if (lineIndex > 0 && indicesHasSymbols(input[lineIndex - 1], touchIndices)) return true

        // check same line
        if (indicesHasSymbols(input[lineIndex], touchIndices)) return true

        // check line below
        if (lineIndex < input.size - 1 && indicesHasSymbols(input[lineIndex + 1], touchIndices)) return true

        return false
    }

    private fun indicesHasSymbols(line: String, indices: List<Int>): Boolean {
        for (idx in indices) {
            if (idx >= 0 && idx < line.length - 1 && line[idx].isSymbol()) {
                return true
            }
        }
        return false
    }

    private fun Char.isSymbol() = !this.isDigit() && this != '.'

    private fun Char.isStar() = this == '*'
}