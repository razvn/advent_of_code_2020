import kotlin.streams.asStream

object Day01 {
    private const val DAY = 1
    private var tests = 0
    operator fun invoke() {
        tests = 0
        val input = getInput().asSequence()
        val (n1, n2) = findSum(input, 2020) ?: throw Exception("None found")
        println("$n1 * $n2 = ${n1 * n2} (tests: $tests)")

        tests = 0
        val (n21, n22, n23) = findSum3(input, 2020) ?: throw Exception("None found")
        println("$n21 * $n22 * $n23 = ${n21 * n22 * n23} (tests: $tests)")
    }

    private fun getInput(): List<Int> = Tools.readInputInts(DAY)

    private fun findSum(values: Sequence<Int>, sum: Int): Pair<Int, Int>? {
        return values.mapNotNull { n1 ->
            tests ++
            val n2 = sum - n1
            if (values.contains(n2)) n1 to n2 else null
        }.firstOrNull()
    }
    private fun findSum3(values: Sequence<Int>, sum: Int): Triple<Int, Int, Int>? {
        return values.mapNotNull { n1 ->
            val subSum = sum - n1
            // val n2n3 = values.mapNotNull { findSum(values, subSum) }.firstOrNull()
            val n2n3 = values.mapNotNull { findSum(values.filter { it < subSum }, subSum) }.firstOrNull()
            n2n3?.let { Triple(n1, it.first, it.second)  }
        }.firstOrNull()
    }
}

fun main() {
    Day01()
}
