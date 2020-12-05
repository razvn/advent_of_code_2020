import Colors.green
import Log.log
import kotlin.math.ceil

fun main() = Day05()

object Day05 : Day(5) {
    override fun invoke() {
        val input = getInputData { decodePosition(it) }

        part1(input) { seats ->
            seats.maxByOrNull { it.id }?.toString() ?: "<NULL>"
        }

        part2(input) { seats ->
            val seatsIds = seats.map(Seat::id)

            seatsIds.firstOrNull {
                val isValidPreviousId = !seatsIds.contains(it + 1) && seatsIds.contains(it + 2)
                log("Id: $it - ${isValidPreviousId.toColor()}")
                isValidPreviousId
            }?.let {
                (it + 1).toString() // the place it's the next id to the one found
            } ?: "<NULL>"
        }
    }


    private fun decodePosition(data: String): Seat {
        val row = calcPosition(data.take(7), 'F', 127)
        val col = calcPosition(data.takeLast(3), 'L', 7)
        return Seat(row, col)
    }

    private fun calcPosition(input: String, lowerChar: Char, maxInput: Int): Int {
        var max = maxInput
        var min = 0
        input.forEach {
            val half = ceil((max - min) / 2.0).toInt()
            if (it == lowerChar) {
                max -= half
            } else {
                min += half
            }
            log("\tinput: $input | char: $it | min: $min, max: $max | ${green(half)}")
        }
        log("input: $input | min: $min, max: $max | ${green(min)}")
        return max
    }

    data class Seat(val row: Int, val col: Int, val id: Int = row * 8 + col)
}