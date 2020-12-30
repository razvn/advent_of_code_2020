import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() = Day25()

@ExperimentalTime
object Day25 : Day(25, testData = false) {
    override fun invoke() {
        val input = getInputData { it }.map { it.toLong() }

        part1(input) { data ->
            val cardKey = data.first()
            val doorKey = data.last()

            var cardEnc = 1L
            var doorEnc = 1L

            while (cardEnc != cardKey) {
                cardEnc = (cardEnc * 7) % 20201227
                doorEnc = (doorEnc * doorKey) % 20201227
            }

            println("card: $cardEnc | door: $doorEnc")
            "$doorEnc"
        }

        part2(input) { data ->
            "TODO"
        }
    }
}