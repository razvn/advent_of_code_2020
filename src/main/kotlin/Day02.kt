object Day02 {
    private const val DAY = 2
    operator fun invoke() {
        val input = getInput()
        part1(input)
        part2(input)
    }

    private fun getInput(): List<Entry> {
        val regex = "(\\d+)-(\\d+) (\\w): (\\w+)".toRegex()
        return Tools.readInput(DAY)
                .mapIndexedNotNull { idx, it ->
                    val matchResult = regex.find(it)
                    if (matchResult != null) {
                        val (start, end, ch, pwd) = matchResult.destructured
                        Entry(start.toInt(), end.toInt(), ch.first(), pwd)
                    } else {
                        println("ERROR line: $idx no match for: $it")
                        null
                    }
               }
    }

    data class Entry(
            val start: Int,
            val end: Int,
            val char: Char,
            val password: String
    ) {
        fun isValid(): Boolean = password.filter { it == char }.count() in IntRange(start, end)
        fun isValid2(): Boolean = listOf(password[start - 1], password[end - 1]).count { it == char } == 1

    }

    private fun part1(input: List<Entry>) {
        println("Part 1: number of valid pwd: ${input.count { it.isValid() }}")
    }

    private fun part2(input: List<Entry>) {
        println("Part 2: number of valid pwd: ${input.count { it.isValid2() }}")
    }
}

fun main() {
    Day02()
}
