import Colors.green
import Colors.red
import Colors.yellow
import Log.log
import kotlin.math.ceil

object Day05 {
    private const val DAY = 5
    operator fun invoke() {
        val input = getInput()

        part1(input)
        part2(input)
    }

    private fun getInput(): List<Seat> = Tools.readInput(DAY, test = false)
        .map { decodePosition(it) }

    data class Seat(val row: Int, val col: Int, val id: Int = row * 8 + col)

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

    private fun part1(input: List<Seat>) {
        input.map {
            log("\t Seat: ${yellow(it)}")
        }
        input.maxByOrNull { it.id }?.let {
            println("Part 1: Max seat: ${yellow(it)}")
        } ?: println("Part 1: NULL")
    }

    private fun part2(input: List<Seat>) {
        val idsList = input.map {
            it.id
        }
        idsList.filter {
            val contains = !idsList.contains(it + 1) && idsList.contains(it + 2)
            log("Id: $it - ${contains.toColor()}")
            contains
        }.also {
            it.firstOrNull()?.let { id ->
                println("Part 2: Seat ID: ${green(id + 1)}")
            } ?: println(red("Part 2: None found"))
        }
    }
}

fun main() {
    Day05()
}
