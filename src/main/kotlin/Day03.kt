fun main() = Day03()

object Day03 : Day(3) {
    override fun invoke() {
        val input = getInputData().filter { it.isNotBlank() }

        part1(input) {
            countTrees(input, 3, 1).toString()
        }

        part2(input) {
            val result = listOf(
                1 to 1,
                3 to 1,
                5 to 1,
                7 to 1,
                1 to 2
            ).map {
                it to countTrees(input, it.first, it.second)
            }.toMap()

            result.values.reduce { acc, i -> acc * i }.toString()
        }
    }

    private fun countTrees(input: List<String>, stepRight: Int, stepDown: Int): Int {
        return input
            .filterIndexed { idx, _ -> idx % stepDown == 0 }
            .filterIndexed { idx, it ->
                val relativeRight = (idx * stepRight) % it.length
                it[relativeRight] == '#'
            }
            .count()
    }
}
