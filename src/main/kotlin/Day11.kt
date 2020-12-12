import kotlin.time.ExperimentalTime

@ExperimentalStdlibApi
@ExperimentalTime
fun main() = Day11()


@ExperimentalStdlibApi
@ExperimentalTime
object Day11 : Day(11, testData = false) {
    override fun invoke() {
        val input = getInputData()
        part1(input) { inList ->
            val data = inList.map { it.toCharArray().toTypedArray() }.toTypedArray()
            var exit: Boolean

            do {
                exit = true
                applyChanges(data, maxOccupiedSeats = 4, this::countOccupiedPart1).map { point ->
                    exit = false
                    data[point.y][point.x] = changeValue(data[point.y][point.x])
                }
                // println("-".repeat(50))
                // data.forEach { println(it.joinToString("")) }
                // println("-".repeat(50))
                // println(data.map { it.count { it == '#' } }.sum())
            } while (!exit)

            "${data.map { line -> line.count { it == '#' } }.sum()}"
        }

        part2(input) { inList ->
            val data = inList.map { it.toCharArray().toTypedArray() }.toTypedArray()
            var exit: Boolean

            do {
                exit = true
                applyChanges(data, maxOccupiedSeats = 5, this::countOccupiedPart2).forEach { point ->
                        exit = false
                        data[point.y][point.x] = changeValue(data[point.y][point.x])
                }
                // println("-".repeat(50))
                // d.forEach { println(it) }
                // println("-".repeat(50))
                //println("occupied: $occupied - changed: $changed")
                // if (occupied != 0 && newOcc == occupied) exit = true
                // occupied = newOcc

            } while (!exit)
            "${data.map { line -> line.count { it == '#' } }.sum()}"
        }
    }

    private fun changeValue(char: Char): Char = when(char) {
        '#' -> 'L'
        'L' -> '#'
        else -> throw Exception("Unexpected char value: $char")
    }
    private fun applyChanges(data: Array<Array<Char>>, maxOccupiedSeats: Int, countOccupied: (Point11, Array<Array<Char>>) -> Int): List<Point11> {
        return data.mapIndexed { idx, line ->
            line.mapIndexedNotNull { charIdx, c ->
                val currentPoint = Point11(charIdx, idx)
                val occupied = countOccupied(currentPoint, data)
                when(c) {
                    'L' -> if (occupied == 0) currentPoint else null
                    '#' -> if (occupied >= maxOccupiedSeats) currentPoint else null
                    else -> null
                }
            }
        }.flatten()
    }

    private fun countOccupiedPart1(point: Point11, data: Array<Array<Char>>): Int =
        countOccupied(point,data, true)

    private fun countOccupied(point: Point11, data: Array<Array<Char>>, justAdjacent: Boolean): Int {
        return listOf(Point11(1, 0), Point11(0, 1), Point11(-1, 0), Point11(0, -1),
            Point11(1, 1), Point11(1, -1), Point11(-1, 1), Point11(-1, -1),
        )
            .map {
                if (isSeatInDirectionOccupied(it, point, data, justAdjacent)) 1 else 0
            }
            .sum()
    }

    private fun countOccupiedPart2(point: Point11, data: Array<Array<Char>>): Int =
        countOccupied(point, data, false)

    private fun isSeatInDirectionOccupied(direction: Point11, start: Point11, data: Array<Array<Char>>, justAdjacent: Boolean = false): Boolean {
        var x = start.x + direction.x
        var y = start.y + direction.y
        var exit = 0 // 0 if not empty, 1 if first seat found is occupied, 2 it it is free
        val maxX = data[0].size - 1
        val maxY = data.size - 1
        while (x in 0..maxX && y in 0..maxY && exit == 0) {
            exit = when(data[y][x]) {
                '#' -> 1
                'L' -> 2
                else -> 0
            }
            if (justAdjacent) x = -1 // if we just want the adjacent ones then we get out after the first run
            else {
                x += direction.x
                y += direction.y
            }
        }
        return exit == 1
    }
}

data class Point11(val x: Int, val y: Int)