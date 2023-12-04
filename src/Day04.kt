fun main() {

    val solver = Day04Solver()
//    val input = readInput("Day04_test")
    val input = readInput("Day04")

    fun part1(input: List<String>) = solver.solvePart1(input) // idk if it is a must to use these functions
    fun part2(input: List<String>) = solver.solvePart2(input)

    println(part1(input))
    println(part2(input))
}

class Day04Solver {
    fun solvePart1(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            sum += countPointsInLine(line).first
        }
        return sum
    }

    fun solvePart2(input: List<String>): Int {
        val cardsExtra = IntArray(input.size) { 0 }
        for (idx in input.indices) {
            cardsExtra[idx] = countPointsInLine(input[idx]).second
        }
        var sum = 0
        for (index in input.indices) {
            sum += countCards(index, cardsExtra)
        }
        return sum
    }

    private fun countCards(currentIndex: Int, cardsExtra: IntArray): Int {
        if (cardsExtra[currentIndex] == 0) {
            return 1
        }
        var sum = 1
        for (idx in currentIndex + 1 .. currentIndex + cardsExtra[currentIndex]) {
            sum += countCards(idx, cardsExtra)
        }
        return sum
    }

    private fun countPointsInLine(line: String): Pair<Int, Int> { // Points to extra cards count
        var res = 0
        var count = 0
        val sets = line.substringAfterLast(':').split('|')
        val winningNums = sets[0].split(" ").filter { it.isNotEmpty() }.toSet()
        val myNums = sets[1].split(" ").filter { it.isNotEmpty() }
        for (num in myNums) {
            if (winningNums.contains(num)) {
                res = if (res == 0) 1 else res * 2
                count++
            }
        }
        return res to count
    }
}