import kotlin.math.abs
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day12()
}

@ExperimentalTime
object Day12 : Day(12, testData = false) {
    override fun invoke() {
        val input = getInputData { it.first() to it.substring(1).toInt() }

        part1(input) { data ->
            var location = Point12(0, 0)

            var direction = Directions12.E

            // println("START | Direction: $direction - location: $location")
            data.forEach { (order, value) ->
                when(order) {
                    'F' -> location = location.move(value, direction)
                    'N' -> location = location.move(value, Directions12.N)
                    'S' -> location = location.move(value, Directions12.S)
                    'E' -> location = location.move(value, Directions12.E)
                    'W' -> location = location.move(value, Directions12.W)
                    'L' -> direction = direction.moveLeft(value / 90)
                    'R' -> direction = direction.moveRight(value / 90)
                }
                // println("-".repeat(100))
                // println("$order-$value | Direction: $direction - location: $location")
            }
            // println("=".repeat(100))
            // println("Direction: $direction - location: $location")

            location.manhattan.toString()
        }

        part2(input) { data ->
            var ship = Point12(0, 0)
            var waypoint = Point12(10, 1)
            // println("START | Waypoint: $waypoint - location: $ship")
            data.forEach { (order, value) ->
                when(order) {
                    'N' -> waypoint = waypoint.move(value, Directions12.N)
                    'S' -> waypoint = waypoint.move(value, Directions12.S)
                    'E' -> waypoint = waypoint.move(value, Directions12.E)
                    'W' -> waypoint = waypoint.move(value, Directions12.W)
                    'F' -> ship = ship.moveShip(value, waypoint)
                    'L' -> waypoint = waypoint.orientationLeft(value / 90)
                    'R' -> waypoint = waypoint.orientationRight(value / 90)
                }
                // println("-".repeat(100))
                // println("$order-$value | Waypoint: $waypoint - location: $ship")
            }
            ship.manhattan.toString()
        }
    }
}

data class Point12(val x: Int, val y: Int) {
    val manhattan: Int
        get() = abs(x)  + abs(y)
    operator fun plus(value: Point12) = Point12(this.x + value.x, this.y + value.y)

    fun move(value: Int, direction: Directions12) = Point12(this.x + direction.point.x * value, this.y + direction.point.y * value)
    fun moveShip(value: Int, waypoint: Point12) = Point12(this.x + waypoint.x * value, this.y + waypoint.y * value)

    fun orientationLeft(nb: Int): Point12 {
        var point = this
        repeat(nb) {
            point = point.copy(y = point.x, x = -point.y)
        }
        return point
    }

    fun orientationRight(nb: Int): Point12 {
        var point = this
        repeat(nb) {
            point = point.copy(x = point.y, y = -point.x)
        }
        return point
    }
}

enum class Directions12(val point: Point12) {
    N(Point12(0, 1)),
    E(Point12(1, 0)),
    S(Point12(0, -1)),
    W(Point12(-1, 0));

    fun moveLeft(value: Int): Directions12 {
        val newIndex = (this.ordinal - value + values().count()) % values().count()
        return values()[newIndex]
    }

    fun moveRight(value: Int): Directions12 {
        val newIndex = (this.ordinal + value) % values().count()
        return values()[newIndex]
    }
}