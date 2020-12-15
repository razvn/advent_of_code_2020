import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() = Day15()

@ExperimentalTime
object Day15 : Day(15, testData = false) {
    override fun invoke() {
        val input = getInputData { it }.first().split(",").map { it.toInt() }

        part1(input) { data ->
            val spokenNumbers = data.toMutableList()
            var lastSpoken = data.last()
            while (spokenNumbers.size < 2020) {
                lastSpoken = spokenNumbers.dropLast(1)
                    .lastIndexOf(lastSpoken)
                    .takeIf { it > -1 }
                    ?.let {
                        spokenNumbers.size - 1 - it
                    } ?: 0
                spokenNumbers.add(lastSpoken)
            }
            "$lastSpoken"
        }

        part2(input) { data ->
            val spokenNumbers = mutableMapOf<Int, Int>()
            var lastSpoken = data.last()
            data.dropLast(1).forEachIndexed { idx, value ->
                spokenNumbers[value] = idx + 1
            }
            var turn = spokenNumbers.size + 1
            while (turn < 30000000) {
                val newlastSpoken = spokenNumbers[lastSpoken]?.let {
                    turn - it
                } ?: 0
                spokenNumbers[lastSpoken] = turn
                lastSpoken = newlastSpoken
                turn++
            }
            "$lastSpoken"
        }
    }
}