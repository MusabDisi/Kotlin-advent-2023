fun main() {

    val solver = Day07Solver()
//    val input = readInput("Day07_test")
    val input = readInput("Day07")

    fun part1(input: List<String>) = solver.solvePart1(input) // idk if it is a must to use these functions
    fun part2(input: List<String>) = solver.solvePart2(input)

    println(part1(input))
    println(part2(input))
}

class Day07Solver {

    fun solvePart1(input: List<String>): Long {
        val lst = createPairsList(input)
        val sorted = lst.sortedWith(compareBy<Hand> { it.rating }.thenComparator { h1, h2 -> compareSameRatingHands2(h1, h2) })
        var sum = 0L
        for (idx in sorted.indices) {
            val hand = sorted[idx]
            sum += (hand.bid.toLong() * (idx + 1))
        }
        return sum
    }

    fun solvePart2(input: List<String>): Long {
        val lst = createPairsList(input, withJoker = true)
        val sorted = lst.sortedWith(compareBy<Hand> { it.rating }.thenComparator { h1, h2 -> compareSameRatingHands2(h1, h2, withJoker = true) })
        var sum = 0L
        for (idx in sorted.indices) {
            val hand = sorted[idx]
            sum += (hand.bid.toLong() * (idx + 1))
        }
        return sum
    }

    private fun compareSameRatingHands2(hand1: Hand, hand2: Hand, withJoker: Boolean = false): Int {
        val ratingChar = mapOf('A' to 14, 'K' to 13, 'Q' to 12, 'J' to if (withJoker) 1 else 11, 'T' to 10)
        for (idx in 0..<hand1.value.length) {
            val curr1 = ratingChar[hand1.value[idx]] ?: hand1.value[idx].digitToInt()
            val curr2 = ratingChar[hand2.value[idx]] ?: hand2.value[idx].digitToInt()
            if (curr1 > curr2) {
                return 1
            } else if (curr2 > curr1) {
                return -1
            }
        }
        return 0
    }

    private fun createPairsList(input: List<String>, withJoker: Boolean = false): List<Hand> {
        return input.map {
            val (hand, amount) = it.split(' ')
            Hand(value = hand, bid = amount, rating = calculateRating(hand, withJoker = withJoker))
        }
    }

    private fun calculateRating(hand: String, withJoker: Boolean): Long {
        val map = mutableMapOf<Char, Int>()
        for (c in hand) {
            map[c] = (map[c] ?: 0) + 1
        }
        if (withJoker && map.contains('J')) {
            val temp = map['J']
            map.remove('J')
            val currentMax = map.maxByOrNull { it.value }?.key
            if (currentMax == null) {
                map['J'] = 5
            } else {
                map[currentMax] = map[currentMax]!! + temp!!
            }
        }
        return when (map.keys.size) {
            1 -> 6
            2 -> {
                if (map.values.first() == 4 || map.values.last() == 4) {
                    5
                } else {
                    4
                }
            }

            3 -> {
                if (map.values.any { it == 3 }) {
                    3
                } else {
                    2
                }
            }

            4 -> 1
            else -> 0
        }
    }

    data class Hand(val value: String, val bid: String, val rating: Long)

}