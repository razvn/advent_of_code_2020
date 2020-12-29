import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() = Day24()

// as e, se, sw, w, nw, and ne
enum class Color {
    WHITE, BLACK
}

data class HexaTile(
    var color: Color = Color.WHITE,
    val depth: Int,
    val location: String
) {
    var e: HexaTile? = null
    var se: HexaTile? = null
    var sw: HexaTile? = null
    var w: HexaTile? = null
    var nw: HexaTile? = null
    var ne: HexaTile? = null

    fun flip() {
        this.color = if (this.color == Color.WHITE) Color.BLACK else Color.WHITE
    }

    private fun nbBlackAdjecents(): Int {
        return listOfNotNull(e, se, sw, w, nw, ne).map { it.color }.filter { it == Color.BLACK }.count()
    }

    fun shouldFlip(): Boolean  = when (this.color) {
            Color.BLACK -> with (nbBlackAdjecents()) { this == 0 || this > 2 }
            Color.WHITE -> with (nbBlackAdjecents()) { this == 2 }
        }
}

@ExperimentalTime
object Day24 : Day(24, testData = false, "") {
    private val allTiles = mutableSetOf<HexaTile>()
    override fun invoke() {
        val input = getInputData { it }
        // .map { tilesPath(it) }

        part1(input) { data ->
            val startTile = HexaTile(depth = 0, location = "")
            buildPlayground(data.map { it.length }.maxOrNull() ?: 1, setOf(startTile))
            data.map {
                tilesPath(it, startTile)
            }.count {
                it.color == Color.BLACK
            }.toString()
        }

        part2(input) { data ->
            allTiles.clear()
            val startTile = HexaTile(depth = 0, location = "")
            val maxLen = data.map { it.length }.maxOrNull() ?: 1
            // not sure why the +27 (70 levels) but after this value the numbers at 100 stays constant before it varies,
            buildPlayground(maxLen + 27, setOf(startTile))
            data.map {
                tilesPath(it, startTile)
            }
            println("Nb tiles: ${allTiles.size}")

            var newBlack = 0
            (1..100).forEach { nb ->
                allTiles
                    .filter { it.shouldFlip() }
                    .forEach {
                        it.flip()
                    }

                newBlack = allTiles.count { it.color == Color.BLACK }
                println("$nb : Black tiles: $newBlack")
            }

            "$newBlack"
        }
    }

    tailrec fun buildPlayground(size: Int, tiles: Set<HexaTile>) {
        // println("Size: $size, tiles: ${tiles.size}, depth: ${tiles.map { it.depth }.sum() / if (tiles.isNotEmpty()) tiles.size else 1}")
        val newTiles = tiles.map {
            val currentTile = it

            val newTile = HexaTile(depth = currentTile.depth + 1, location = currentTile.location)
            if (currentTile.ne == null) {
                currentTile.ne = newTile.copy(location = "${newTile.location},ne")
                currentTile.ne!!.sw = currentTile
            }
            if (currentTile.nw == null) {
                currentTile.nw = newTile.copy(location = "${newTile.location},nw")
                currentTile.nw!!.se = currentTile
            }
            if (currentTile.w == null) {
                currentTile.w = newTile.copy(location = "${newTile.location},w")
                currentTile.w!!.e = currentTile
            }
            if (currentTile.sw == null) {
                currentTile.sw = newTile.copy(location = "${newTile.location},sw")
                currentTile.sw!!.ne = currentTile
            }
            if (currentTile.se == null) {
                currentTile.se = newTile.copy(location = "${newTile.location},se")
                currentTile.se!!.nw = currentTile
            }
            if (currentTile.e == null) {
                currentTile.e = newTile.copy(location = "${newTile.location},e")
                currentTile.e!!.w = currentTile
            }

            if (currentTile.ne!!.w == null) currentTile.ne!!.w = currentTile.nw
            if (currentTile.ne!!.se == null) currentTile.ne!!.se = currentTile.e

            if (currentTile.nw!!.e == null) currentTile.nw!!.e = currentTile.ne
            if (currentTile.nw!!.sw == null) currentTile.nw!!.sw = currentTile.w

            if (currentTile.w!!.ne == null) currentTile.w!!.ne = currentTile.nw
            if (currentTile.w!!.se == null) currentTile.w!!.se = currentTile.sw

            if (currentTile.sw!!.nw == null) currentTile.sw!!.nw = currentTile.w
            if (currentTile.sw!!.e == null) currentTile.sw!!.e = currentTile.se

            if (currentTile.se!!.ne == null) currentTile.se!!.ne = currentTile.e
            if (currentTile.se!!.w == null) currentTile.se!!.w = currentTile.sw

            if (currentTile.e!!.nw == null) currentTile.e!!.nw = currentTile.ne
            if (currentTile.e!!.sw == null) currentTile.e!!.sw = currentTile.se

            allTiles.add(currentTile)

            setOf(
                currentTile.ne!!,
                currentTile.nw!!,
                currentTile.w!!,
                currentTile.sw!!,
                currentTile.se!!,
                currentTile.e!!
            ).filter {
                it.depth <= size && it.depth == newTile.depth
            }
        }

        if (newTiles.isNotEmpty()) buildPlayground(size, newTiles.flatten().toSet())
    }

    fun tilesPath(intput: String, startTile: HexaTile): HexaTile {
        var currentTile = startTile
        //esew
        var idx = 0
        while (idx < intput.length) {
            currentTile = when (val first = intput[idx]) {
                'e' -> currentTile.e!!
                'w' -> currentTile.w!!
                'n', 's' -> {
                    idx++
                    when (val second = intput[idx]) {
                        'e' -> if (first == 'n') currentTile.ne!! else currentTile.se!!
                        'w' -> if (first == 'n') currentTile.nw!! else currentTile.sw!!
                        else -> throw Exception("Unexpected value: $second")
                    }
                }
                else -> throw Exception("Unexpected value $first")
            }
            idx++
        }
        currentTile.flip()
        return currentTile
    }
}