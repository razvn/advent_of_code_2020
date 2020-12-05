fun main() = Day01()

object Day01 : Day(1) {
    private val input = getInputData { it.toIntOrNull() }.filterNotNull()
    override fun invoke() {
        part1(input) {
            val data = it.asSequence()
            val (n1, n2) = findSum(data, 2020) ?: throw Exception("None found")
            "$n1 * $n2 = ${n1 * n2}"
        }

        part2(input) {
            val data = it.asSequence()
            val (n21, n22, n23) = findSum3(data, 2020) ?: throw Exception("None found")
            "$n21 * $n22 * $n23 = ${n21 * n22 * n23}"
        }
    }

    private fun findSum(values: Sequence<Int>, sum: Int): Pair<Int, Int>? {
        return values.mapNotNull { n1 ->
            val n2 = sum - n1
            if (values.contains(n2)) n1 to n2 else null
        }.firstOrNull()
    }

    private fun findSum3(values: Sequence<Int>, sum: Int): Triple<Int, Int, Int>? {
        return values.mapNotNull { n1 ->
            val subSum = sum - n1
            val n2n3 = values.mapNotNull { findSum(values.filter { it < subSum }, subSum) }.firstOrNull()
            n2n3?.let { Triple(n1, it.first, it.second)  }
        }.firstOrNull()
    }
}