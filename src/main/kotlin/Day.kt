abstract class Day(private val number: Int, private val testData: Boolean = false) {
    fun <T> getInputData(mapFun: (String) -> T): List<T> = Tools.readInput(number, testData).map(mapFun)
    fun getInputData(): List<String> = Tools.readInput(number, testData)

    fun <T> part1(input: List<T>, process: (List<T>) -> String) {
        printResult(1, process(input))
    }

    fun <T> part2(input: List<T>, process: (List<T>) -> String) {
        printResult(2, process(input))
    }

    private fun printResult(part: Int, response: String) {
        val colorFun = if (part == 1) Colors::yellow else Colors::green
        println("PART $part: ${colorFun(response)}")
    }

    abstract operator fun invoke()
}
