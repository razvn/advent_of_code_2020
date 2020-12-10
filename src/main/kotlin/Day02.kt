import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() = Day02()

@ExperimentalTime
object Day02: Day(2) {
    override fun invoke() {
        val regex = "(\\d+)-(\\d+) (\\w): (\\w+)".toRegex()
        val input = getInputData().mapIndexedNotNull { idx, it ->
            val matchResult = regex.find(it)
            if (matchResult != null) {
                val (start, end, ch, pwd) = matchResult.destructured
                PwdEntry(start.toInt(), end.toInt(), ch.first(), pwd)
            } else {
                if (it.isNotBlank()) println("ERROR line: $idx no match for: $it")
                null
            }
        }

        part1(input) { pwdList ->
            "${pwdList.count { it.isValid()}}"
        }

        part2(input) { pwdList ->
            "${pwdList.count { it.isValid2()}}"
        }
    }

    data class PwdEntry(
        val start: Int,
        val end: Int,
        val char: Char,
        val password: String
    ) {
        fun isValid(): Boolean = password.filter { it == char }.count() in IntRange(start, end)
        fun isValid2(): Boolean = listOf(password[start - 1], password[end - 1]).count { it == char } == 1
    }
}


