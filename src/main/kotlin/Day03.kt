object Day03 {
    private const val DAY = 3
    operator fun invoke() {
        val input = getInput()

        part1(input)
        part2(input)
    }

    private fun getInput(): List<String> {
        return Tools.readInput(DAY, test = false)
                .filter { it.isNotBlank() }
    }

    private fun part1(input: List<String>) {
        val treeCount = countTrees(input, 3, 1)
        println("Part 1: trees (3,1): ${Colors.yellow(treeCount)}")
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

    private fun part2(input: List<String>) {
        val res = listOf(
                1 to 1,
                3 to 1,
                5 to 1,
                7 to 1,
                1 to 2
        ).map {
            it to countTrees(input, it.first, it.second)
        }.toMap()

        print("Part 2: trees ")
        res.map { (k, v) -> print("$k: $v | ") }
        println("Result: ${res.values} = ${Colors.green(res.values.reduce { acc, i -> acc * i })}")
    }
}

fun main() {
    Day03()
}
