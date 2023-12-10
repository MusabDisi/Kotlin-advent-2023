fun main() {

    val solver = Day09Solver()
//    val input = readInput("Day09_test")
    val input = readInput("Day09")

    fun part1(input: List<String>) = solver.solvePart1(input) // idk if it is a must to use these functions
    fun part2(input: List<String>) = solver.solvePart2(input)

//    println(part1(input))
    println(part2(input))
}

class Day09Solver {

    fun solvePart1(input: List<String>): Long {
        var sum = 0L
        for (line in input) {
            sum += findNextNumber(line)
        }
        return sum
    }

    fun solvePart2(input: List<String>): Long {
        var sum = 0L
        for (line in input) {
            sum += findPrevNumber(line)
        }
        return sum
    }

    private fun findNextNumber(line: String): Long {
        val seq = line.toSequence()
        var nextSequence = findNextSequence(seq)
        var sum = 0L
        while (!nextSequence.all { it == 0L }) {
            sum += nextSequence.last()
            nextSequence = findNextSequence(nextSequence)
        }
        return sum + seq.last()
    }

    private fun findPrevNumber(line: String): Long {
        val seq = line.toSequence()
        var nextSequence = findNextSequence(seq)
        val firsts = mutableListOf(seq.first())
        while (!nextSequence.all { it == 0L }) {
            firsts.add(nextSequence.first())
            nextSequence = findNextSequence(nextSequence)
        }
        var res = firsts.last()
        for (idx in firsts.size - 2 downTo 0){
            res = firsts[idx] - res
        }
        return res
    }

    private fun findNextSequence(current: LongArray): LongArray {
        val newSequence = LongArray(current.size - 1)
        for (idx in current.size - 2 downTo 0) {
            newSequence[idx] = current[idx + 1] - current[idx]
        }
        return newSequence
    }

    private fun String.toSequence() = this.split(' ').map { it.toLong() }.toLongArray()

}