import Colors.green
import Colors.yellow
import Log.log
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() = Day06()

@ExperimentalTime
object Day06 : Day(6, false) {
    override fun invoke() {
        val input = getInputData()

        part1(input) {
            mapData(input).map { chars ->
                chars.size.also { log("Set: ${yellow(chars)} - size: ${green(it)}") }
            }.sum().toString()
        }

        part2(input) {
            mapData2(input).map { chars ->
                chars.size.also { log("Set: ${yellow(chars)} - size: ${green(it)}") }
            }.sum().toString()
        }
    }

    private fun mapData(input: List<String>): List<Set<Char>> {
        val buffer = mutableSetOf<Char>()

        return input.mapNotNull { line ->
            if (line.isNotBlank()) {
                buffer.addAll(line.toList())
                null
            } else {
                buffer.toSet().also { buffer.clear()  }
            }
        }.let { // handle the last input line if the file does not end with an empty line
            if(buffer.isEmpty()) it else it + listOf(buffer.toSet())
        }
    }

    private fun mapData2(input: List<String>): List<Set<Char>> {
        val buffer = mutableSetOf<Char>()
        var firstOfGroup = true

        return input.mapNotNull { line ->
            if (line.isNotBlank()) {
                val lineAnswers = line.toList()
                if (firstOfGroup) buffer.addAll(lineAnswers)
                else {
                    buffer.removeAll(buffer - lineAnswers)
                }
                firstOfGroup = false
                null
            } else {
                firstOfGroup = true
                buffer.toSet().also { buffer.clear() }
            }
        }.let {
            if(buffer.isEmpty()) it else it + listOf(buffer.toSet())
        }
    }
}
