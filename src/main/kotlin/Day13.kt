import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() = Day13()


@ExperimentalTime
object Day13 : Day(13, testData = false, "5") {

    fun validDivider(nb: Long, data: List<Pair<Long, Int>>): Boolean =
        data.all {
            (nb + it.second) % it.first == 0L
        }

    override fun invoke() {
        val input = getInputData() { it }

        part1(input) { data ->
            val timestamp = data.first().toInt()
            val buses = data[1].split(",").filter { it != "x" }.map { it.toInt() }

            buses.map { id ->
                val t = timestamp / id + if (timestamp % id != 0) 1 else 0
                id to t * id
            }.minByOrNull { it.second - timestamp }
                ?.let {
                    "${it.first * (it.second - timestamp)}"
                } ?: "NONE FOUND"
        }

        part2(input) { data ->
            val buses = data[1].split(",").mapIndexedNotNull { index, value ->
                if (value == "x") null else value.toInt() to index
            }

            var inc = buses.first().first.toLong()
            var index = 1
            var nb = inc

            // for optimisation we try to find the first number that is Ok for each previous items and use that number as step
            while(index < buses.count()) {
                val (currentValue, currentIndex) = buses[index]
                if ((nb + currentIndex) % currentValue == 0L) {
                    inc *= currentValue // step is multiplied by each valid value this gives a great boost
                    index++
                } else {
                    nb += inc
                }
            }

            "$nb"
        }
            /* SLOW for the solution takes +1h to find it
            part2(input) { data ->

                val buses = data[1].split(",").mapIndexedNotNull { index, value ->
                    if (value == "x") null else value.toLong() to index
                }

                val ids = buses.map {
                    it.first
                }
                val primes = ids.filter { nb ->
                    ids.none { it > nb && it % nb == 0L }
                }


                val max = buses.maxByOrNull { it.first } ?: throw Exception("Should have a max")
                val opt = buses.map { it.first to (it.second - max.second) }

                println(buses)
                val firstValue = max.first

                // var nb =max.first
                // they say it's higher than 100000000000000 so let's gain some time starting from after that
                var nb = (100000000000000 / firstValue + 1) * firstValue // we start from the next max divider higher than that value

                var exit = false
                //val executor = Executors.newScheduledThreadPool(1)
                //executor.scheduleAtFixedRate({ println("Testing: $nb") }, 1, 1, TimeUnit.MINUTES)

                while (!exit) {
                    nb += firstValue
                    // println("Testing: $nb")
                    exit = opt.all {
                        (nb + it.second) % it.first == 0L
                    }
                }
                // executor.shutdownNow()

            }
             */
    }
}