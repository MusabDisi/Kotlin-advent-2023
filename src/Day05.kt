import kotlin.math.abs
import kotlin.math.min

fun main() {

    val solver = Day05Solver()
//    val input = readInput("Day05_test")
    val input = readInput("Day05")

    fun part1(input: List<String>) = solver.solvePart1(input) // idk if it is a must to use these functions
    fun part2(input: List<String>) = solver.solvePart2(input)

    println(part1(input))
    println(part2(input))
}

class Day05Solver {
    fun solvePart1(input: List<String>): Long {
        val seeds = input[0].substringAfterLast(':').split(' ').filter { it.isNotEmpty() }.map { it.toLong() }
        val (namesMap, sourceDestMap) = createMaps(input)
        var lowestLocation = Long.MAX_VALUE
        for (seed in seeds) {
            val location = findLocation(seed, namesMap, sourceDestMap)
            lowestLocation = min(location, lowestLocation)
        }
        return lowestLocation
    }

    fun solvePart2(input: List<String>): Long {
        val seeds = input[0].substringAfterLast(':').split(' ').filter { it.isNotEmpty() }.map { it.toLong() }
        val (namesMap, sourceDestMap) = createMaps(input)

        val seedsRanges = mutableListOf<LongRange>()
        for (idx in seeds.indices step 2) {
            seedsRanges.add(seeds[idx]..<seeds[idx] + seeds[idx + 1])
        }

        var lowestLocation = Long.MAX_VALUE
        for (range in seedsRanges) {
            val location = findLocation2(range, namesMap, sourceDestMap)
            lowestLocation = min(location, lowestLocation)
        }
        return lowestLocation
    }

    // Brute force takes so much time
//    private fun findLocation2(seedsRange: LongRange, namesMap: MutableMap<String, String>, map: MutableMap<Pair<String, String>, MutableList<Pair<LongRange, LongRange>>>): Long {
//        var lowest = Long.MAX_VALUE
//        for (num in seedsRange) {
//            lowest = min(lowest, findLocation(num, namesMap, map))
//        }
//        return lowest
//    }

    // don't ask me what I did here lol
    private fun findLocation2(seedsRange: LongRange, namesMap: MutableMap<String, String>, map: MutableMap<Pair<String, String>, MutableList<Pair<LongRange, LongRange>>>): Long {

        fun findNewRanges(sourceName: String, destName: String, currentRanges: List<LongRange>): List<LongRange> {
            val newRanges = mutableListOf<LongRange>()
            val rangesMap = map[sourceName to destName] ?: return emptyList()
            for (currRange in currentRanges) {
                val temp = mutableListOf<LongRange>()
                for (rangeSet in rangesMap) {
                    val (ranges, found) = getDestinationRanges(currRange, rangeSet.first, rangeSet.second)
                    if (found) {
                        newRanges.clear()
                        newRanges.addAll(ranges)
                        break
                    } else {
                        newRanges.add(currRange)
                    }
                }
                newRanges.addAll(temp)
            }
            return newRanges.toSet().toList()
        }

        var sourceName = "seed"
        var destName = namesMap[sourceName] ?: return 0
        val currentRanges = mutableSetOf(seedsRange)
        while (destName != "location") {
            val newRanges = findNewRanges(sourceName, destName, currentRanges.toList())
            sourceName = destName
            destName = namesMap[sourceName] ?: return 0
            currentRanges.clear()
            currentRanges.addAll(newRanges)
        }
        val newRanges = findNewRanges(sourceName, destName, currentRanges.toList())
        return newRanges.filter { it.first != 0L }.minOf { it.first }
    }

    private fun findLocation(seed: Long, namesMap: MutableMap<String, String>, map: MutableMap<Pair<String, String>, MutableList<Pair<LongRange, LongRange>>>): Long {

        fun findNewValueToFollow(rangesList: List<Pair<LongRange, LongRange>>, currentValueToFollow: Long): Long? {
            for (ranges in rangesList) {
                val sourceRange = ranges.first
                val destRange = ranges.second
                if (currentValueToFollow in sourceRange) {
                    return (currentValueToFollow - sourceRange.first) + destRange.first
                }
            }
            return null
        }

        var sourceName = "seed"
        var destName = namesMap[sourceName] ?: return 0
        var valueToFollow = seed

        while (destName != "location") {
            val rangesList = map[sourceName to destName] ?: return 0
            valueToFollow = findNewValueToFollow(rangesList, valueToFollow) ?: valueToFollow
            sourceName = destName
            destName = namesMap[sourceName] ?: return 0
        }

        val rangesList = map[sourceName to destName] ?: return 0
        return findNewValueToFollow(rangesList, valueToFollow) ?: valueToFollow
    }

    private fun createMaps(input: List<String>): Pair<MutableMap<String, String>, MutableMap<Pair<String, String>, MutableList<Pair<LongRange, LongRange>>>> {
        val cleanedInput = input.filter { it.isNotEmpty() }
        val map = mutableMapOf<Pair<String, String>, MutableList<Pair<LongRange, LongRange>>>()
        val namesMap = mutableMapOf<String, String>()
        var currentPair = "" to ""
        for (idx in 1..<cleanedInput.size) {
            val line = cleanedInput[idx]
            if (line[0].isLetter()) {
                currentPair = line.toPairsMap()
                namesMap[currentPair.first] = currentPair.second
            } else {
                val ranges = extractNumbersRanges(line)
                map[currentPair]?.add(ranges) ?: map.put(currentPair, mutableListOf(ranges))
            }
        }
        return namesMap to map
    }

    private fun extractNumbersRanges(line: String): Pair<LongRange, LongRange> {
        val (destination, source, range) = line.split(' ').map { it.toLong() }
        return (source..<source + range) to (destination..<destination + range)
    }

    private fun getDestinationRanges(currentRange: LongRange, sourceRange: LongRange, destRange: LongRange): Pair<MutableList<LongRange>, Boolean>{
        val res = mutableListOf<LongRange>()
        val startDiff = abs(currentRange.first - sourceRange.first)
        val endDiff = abs(currentRange.last - sourceRange.last)
        var found = true
        if (currentRange.first in sourceRange && currentRange.last in sourceRange) {
            res.add(destRange.first + startDiff..destRange.last - endDiff)
        } else if (sourceRange.first in currentRange && sourceRange.last in currentRange) {
            if (startDiff > 0) {
                res.add(currentRange.first..<currentRange.first + startDiff)
            }
            if (endDiff > 0) {
                res.add(currentRange.last - endDiff + 1..currentRange.last)
            }
            res.add(destRange)
        } else if (sourceRange.first in currentRange) {
            res.add(currentRange.first..<currentRange.first + startDiff)
            res.add(destRange.first..destRange.last - endDiff)
        } else if (sourceRange.last in currentRange) {
            res.add(destRange.first + startDiff..destRange.last)
            res.add(currentRange.last - endDiff + 1..currentRange.last)
        } else {
            res.add(currentRange)
            found = false
        }
        return res to found
    }

    private fun String.toPairsMap(): Pair<String, String> { // expects input to be valid
        val names = this.split(' ')[0].split('-')
        return names[0] to names[2]
    }
}