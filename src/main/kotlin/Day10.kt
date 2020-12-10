import Log.log
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() = Day10()

operator fun Pair<Int, Int>.plus(value: Pair<Int, Int>) = (this.first + value.first) to (this.second + value.second)

@ExperimentalTime
object Day10 : Day(10, testData = false, "") {
    override fun invoke() {
        val fileInput = getInputData() { it.toInt() }.sorted()
        val device = (fileInput.maxOrNull() ?: 0) + 3
        val input = listOf(0) + fileInput + (listOf(device))

        part1(input) { data ->
            data
                .asSequence()
                .zipWithNext()
                .map {
                    when (it.second - it.first) {
                        1 -> 1 to 0
                        3 -> 0 to 1
                        else -> throw Exception("Should never happen [$it]")
                    }
                }
                .fold(0 to 0) { acc, value -> acc + value }
                .let { "${it.first} * ${it.second} = ${it.first * it.second}" }
        }

        part2(input) { data ->
            val startNode = Node10(0, 1L)
            val nodes = mutableListOf(startNode)

            data.drop(1).forEach { value ->
                val number = nodes.sumOf { it.count(value) }
                log("$value: nb: $number | nodes: $nodes")
                if (nodes.size >= 3) nodes.removeFirst()
                nodes.add(Node10(value, number))
            }
            "${nodes.last().number}"
        }
    }

}

data class Node10(val value: Int, val number: Long) {
    fun count(v: Int) = if (v - value <= 3) number else 0L
}
