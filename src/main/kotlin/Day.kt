import kotlin.time.ExperimentalTime
import kotlin.time.TimedValue
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

@ExperimentalTime
abstract class Day(private val number: Int, private val testData: Boolean = false, private val testSuffix: String = "") {
    fun <T> getInputData(mapFun: (String) -> T): List<T> = Tools.readInput(number, testData, testSuffix).map(mapFun)
    fun getInputData(): List<String> = Tools.readInput(number, testData, testSuffix)

    fun <T> part1(input: List<T>, process: (List<T>) -> String) {
        printResult(1,  measureTimedValue { process(input) })
    }

    fun <T> part2(input: List<T>, process: (List<T>) -> String) {
        printResult(2,  measureTimedValue { process(input) })
    }

    private fun printResult(part: Int, timedResponse: TimedValue<String>) {
        val colorFun = if (part == 1) Colors::yellow else Colors::green
        println("PART $part: ${colorFun(timedResponse.value)} (${Colors.blue(timedResponse.duration.inMilliseconds)} ms)")
    }

    abstract operator fun invoke()
}
