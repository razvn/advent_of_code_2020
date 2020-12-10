import Log.log
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() = Day09()

class Stack() {
    val data = mutableListOf<Long>()
    val size: Int
        get() = data.size
    val sum: Long
        get() = data.sum()
    val min: Long
        get() = data.minOrNull() ?: 0
    val max: Long
        get() = data.maxOrNull() ?: 0

    fun push(item: Long, isFullCondition: () -> Boolean) {
        while (isFullCondition()) {
            data.removeFirst()
        }
        data.add(item)
    }

    fun checkSum(value: Long): Pair<Long, Long>? {
        log("Search: $value in $data")
        return data.asSequence().mapNotNull { first: Long ->
            val second = (data - first).firstOrNull { first + it == value }
            second?.let { first to second }
        }.firstOrNull()
    }
}

@ExperimentalTime
object Day09 : Day(9, false) {
    override fun invoke() {
        val input = getInputData() { it.toLong() }

        part1(input) { data ->
            val stack = Stack()
            val nb = 25
            val isFull = { stack.size > nb }
            val response = data.asSequence().mapNotNull { item ->
                if (!isFull()) {
                    stack.push(item, isFull)
                    null
                } else {
                    val sum = stack.checkSum(item)
                    if (sum != null) {
                        log("${sum.first} + ${sum.second} = $item")
                        stack.push(item, isFull)
                        null
                    } else {
                        item
                    }
                }
            }.firstOrNull()

            "$response"
        }

        part2(input) { data ->

            // val nb = 127L
            val nb = 248131121L
            val stack = Stack()

            val response = data.dropLast(data.size - data.indexOf(nb)).asSequence().mapNotNull { item ->
                if (stack.sum == nb) {
                    val min = stack.min
                    val max = stack.max
                    Triple(min, max, min + max)
                } else {
                    stack.push(item) { stack.sum + item > nb }
                    null
                }
            }.firstOrNull()

            response?.let { "${it.third} = ${it.first} + ${it.second}" }
                ?: "NOT FOUND"
        }
    }
}
