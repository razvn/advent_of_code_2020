import kotlin.math.sqrt
import kotlin.time.ExperimentalTime

@ExperimentalStdlibApi
@ExperimentalTime
fun main() = Day20()

@ExperimentalStdlibApi
@ExperimentalTime
object Day20 : Day(20, testData = false) {
    override fun invoke() {
        val input = getInputData { it }
        val tiles = makeTiles(input)
        var image: Tile20? = null
        part1(input) { _ ->
            val squareSize = sqrt(tiles.count().toDouble()).toInt()
            println("Nb tiles: ${tiles.count()} | square size: $squareSize")

            //tiles.mapIndexed { idx, tile ->
            //    tile.print(true, color = colors[idx % 3])
            // val otherTiles = tiles.filter { it.id != it.id }
            // createImageWithStartTile(it, otherTiles)
            // }

            val corner = tiles.first { currentTile ->
                val otherTiles = tiles.filter { currentTile.id != it.id }
                currentTile.isACorner(otherTiles)
            }

            val assemble = Assemble(squareSize, corner, tiles.filter { it.id != corner.id })


            assemble.print()

            image = assemble.toImage()
            assemble.corners()
        }

        part2(input) { data ->
            val roughness = searchMonster(image!!)
            println(roughness)

            "${roughness.minOrNull()}"
        }
    }

    fun makeTiles(input: List<String>): List<Tile20> {
        val tiles = mutableListOf<Tile20>()
        val tileLines = mutableListOf<String>()
        var tileId: Int? = null
        input.forEach { line ->
            when {
                line.startsWith("Tile") -> {
                    tileId = line.removePrefix("Tile").dropLast(1).trim().toInt()
                    tileLines.clear()
                }
                line.startsWith("#") || line.startsWith(".") -> {
                    tileLines.add(line)
                }
                line.isBlank() -> if (tileLines.isNotEmpty()) tiles.add(Tile20(tileId ?: 0, tileLines))
            }
        }

        return tiles
    }

    fun searchMonster(tile: Tile20): List<Int> {
        val monster =
                """
                                  # 
                #    ##    ##    ###
                 #  #  #  #  #  #                   
                """.trimIndent()

        return tile.variations.map { t ->
            println("Variation - ${t.info}")
            t.print(false, Colors::red)

            val img = t
            val middle = "#....##....##....###"
            val bottom = ".#..#..#..#..#..#"
            val middlepattern = middle.toRegex()
            val bottompatten = bottom.toRegex()
            val middleIdx = middle.withIndex().filter { it.value == '#' }.map { it.index }
            val bottomIdx = bottom.withIndex().filter { it.value == '#' }.map { it.index }

            (1 until img.data.indices.last).forEach { idx ->
                val currentLine = img.data[idx].joinToString("")
                var match = middlepattern.find(currentLine)
                while (match != null) {
                    val range = match.range

                    val prevLineHead = img.data[idx - 1][range.last - 1]
                    if (prevLineHead == '#') {
                        //on cherche la bas
                        val bottomline = img.data[idx + 1].joinToString("").substring(range)
                        if (bottompatten.find(bottomline) != null) {
                            img.data[idx - 1][range.last - 1] = '0'
                            middleIdx.forEach {
                                img.data[idx][range.first + it] = '0'
                            }
                            bottomIdx.forEach {
                                img.data[idx + 1][range.first + it] = '0'
                            }
                        }
                    }
                    match = match.next()
                }
            }
            println("Monsters  - ${t.info}")
            img.print(false, Colors::yellow)
            img.data.map { line ->
                line.count { it == '#' }
            }.sum()
        }
    }

}

@ExperimentalStdlibApi
class Assemble(val s: Int, corner: Tile20, tiles: List<Tile20>) {
    private val image: List<MutableList<Tile20?>> = buildList {
        repeat(s) {
            add(
                buildList<Tile20?> {
                    repeat(s) { add(null) }
                }.toMutableList()
            )
        }
    }

    init {
        val all = tiles.toMutableList()

        image.indices.forEach { line ->
            image.indices.forEach { col ->
                if (line == 0 && col == 0) {
                    // add the first one
                    val current = cornerVariationEmptyBorders(corner, listOf('T', 'L'), all)
                    image[line][col] = current
                } else {
                    val matchingBorder = when {
                        col == 0 -> mapOf('T' to image[line - 1][col]!!.bordersMap['B']!!)
                        line == 0 -> mapOf('L' to image[line][col - 1]!!.bordersMap['R']!!)
                        else -> mapOf(
                            'T' to image[line - 1][col]!!.bordersMap['B']!!,
                            'L' to image[line][col - 1]!!.bordersMap['R']!!
                        )
                    }

                    image[line][col] = try {
                        val s = findMatchingBorder(matchingBorder, all)
                        all.removeIf { it.id == s.id }
                        s
                    } catch (e: Exception) {
                        println("Search error: $line,$col : $matchingBorder - ${all.size} ")
                        val colors = listOf(Colors::green, Colors::yellow, Colors::blue, Colors::red)
                        print()
                        all.forEach {
                            it.print(true, colors.random())
                        }
                        throw e
                    }
                }
            }
        }
    }

    fun findMatchingBorder(bordersToMatch: Map<Char, String>, tiles: List<Tile20>): Tile20 {
        return tiles.flatMap { it.variations }.first { tile ->
            bordersToMatch.all { border ->
                tile.bordersMap[border.key] == border.value
            }
        }
    }

    fun cornerVariationEmptyBorders(tile: Tile20, emptyBorders: List<Char>, tiles: List<Tile20>): Tile20 {
        val otherTilesBorders = tiles.flatMap { it.bordersVariations }.toSet()
        return tile.variations.first { t ->
            emptyBorders.none { b ->
                otherTilesBorders.contains(t.bordersMap[b])
            }
        }
    }

    fun corners(): String = buildString {
        val max = image.size - 1
        val cor = listOf(image[0][0]!!.id, image[0][max]!!.id, image[max][0]!!.id, image[max][max]!!.id)
        val res = cor.fold(1L) { acc, value -> acc * value }

        append(cor.joinToString(" x ", postfix = " = $res"))
    }

    fun toImage(): Tile20 {
        val newSize = image.first().first()!!.data.size - 2
        val lineData = buildList {
            repeat(image.size * newSize) {
                add("")
            }
        }.toMutableList()

        image.forEachIndexed { idxLine, lineOfTiles ->
            lineOfTiles.forEach { tile ->
                tile!!.data.drop(1).dropLast(1).forEachIndexed { index, chars ->
                    val idx = idxLine * newSize + index
                    lineData[idx] += chars.drop(1).dropLast(1).joinToString("")
                    //println(lineData)
                }
            }
        }

        return (Tile20(0, lineData))
    }

    fun print() {
        val colors = listOf(Colors::green, Colors::yellow, Colors::blue, Colors::red)
        val clrs = colors.shuffled()

        image.forEach { listTiles ->
            println(listTiles.mapIndexedNotNull { idx, value ->
                value?.let {
                    clrs[idx % colors.size](it.id)
                }
            }.joinToString(" | "))
        }

        image.forEachIndexed { idx, line ->
            val lineData = buildList {
                repeat(line.first()!!.size) {
                    add("")
                }
            }.toMutableList()
            line.mapNotNull { it?.data }.forEachIndexed { index, mutableList ->

                val color = clrs[(index + idx) % clrs.size]
                mutableList.forEachIndexed { tileIdx, value ->
                    lineData[tileIdx] += color(value.joinToString(""))
                }
            }
            lineData.forEach { println(it) }
        }
    }
}

@ExperimentalStdlibApi
data class Tile20(
    val id: Int,
    val lines: List<String>,
    val size: Int = lines.size,
    val info: String = ""
) {
    val data: MutableList<MutableList<Char>> = buildList {
        repeat(lines.size) {
            val values = ".".repeat(lines.size).toMutableList()
            this.add(values)
        }
    }.toMutableList()

    init {
        lines.forEachIndexed { idxLine, line ->
            line.withIndex()
                // .filter { it.value == '#' }
                .forEach { data[idxLine][it.index] = it.value }
        }
    }

    fun print(withId: Boolean = false, color: (Any) -> String = { a -> "$a" }) {
        if (withId) println(color("Tile $id: $info"))

        data.iterator().forEach {
            println(color(it.iterator().asSequence().toList().joinToString("")))
        }
    }

    val top = data[0].joinToString("")

    val buttom = data[data.size - 1].joinToString("")

    val left = data.indices.map { data[it][0] }.joinToString("")

    val right = data.indices.map { data[it][data[it].size - 1] }.joinToString("")

    val variations: List<Tile20> by lazy { buildVariations(this) }

    val borders: List<String> = buildList {
        add(top)
        add(buttom)
        add(left)
        add(right)
    }

    val bordersVariations: Set<String> by lazy {
        variations.flatMap { it.borders }.toSet()
    }

    val bordersMap: Map<Char, String> = mapOf(
        'T' to top,
        'B' to buttom,
        'L' to left,
        'R' to right
    )

    fun isACorner(tiles: List<Tile20>): Boolean {
        val tileBordersWithNoCorrespondence = this.borders.filter { border ->
            tiles.map { it.bordersVariations }.flatten().none { it.contains(border) }
        }.count()
        return tileBordersWithNoCorrespondence >= 2
    }

    private fun buildVariations(origin: Tile20): List<Tile20> = buildList {
        add(origin)
        add(flip())
        val rotated = rotateClockwise()
        add(rotated)
        add(rotated.flip())
        val rotated2 = rotated.rotateClockwise(2)
        add(rotated2)
        add(rotated2.flip())
        val rotated3 = rotated2.rotateClockwise(3)
        add(rotated3)
        add(rotated3.flip())
    }

    fun rotateClockwise(index: Int = 1): Tile20 {
        val size = this.size
        val tmp: MutableList<MutableList<Char>> = buildList {
            repeat(size) {
                add(".".repeat(size).toMutableList())
            }
        }.toMutableList()

        for (i in this.data.indices) {
            for (j in this.data[i].indices) {
                tmp[j][size - 1 - i] = this.data[i][j]
            }
        }

        return Tile20(this.id, tmp.map { it.joinToString("") }, this.size, this.info + "rotate $index")
    }

    fun flip(): Tile20 {
        val newData = this.data.map { it.reversed().joinToString("") }
        return Tile20(this.id, newData, this.size, this.info + " + flipped")
    }
}


@ExperimentalStdlibApi
data class Tile202(
    val id: Int,
    val lines: List<String>,
    val size: Int = 10,
    val info: String = ""
) {
    val data: Array<Array<Char>> = Array(size) { Array(size) { '.' } }

    init {
        lines.forEachIndexed { idxLine, line ->
            line.withIndex()
                // .filter { it.value == '#' }
                .forEach { data[idxLine][it.index] = it.value }
        }
    }

    fun print(withId: Boolean = false, color: (Any) -> String = { a -> "$a" }) {
        if (withId) println(color("Tile $id: $info"))

        data.iterator().forEach {
            println(color(it.iterator().asSequence().toList().joinToString("")))
        }
    }

    val top: String
        get() = data[0].joinToString("")

    val buttom: String
        get() = data[data.size - 1].joinToString("")

    val left: String
        get() = data.indices.map { data[it][0] }.joinToString("")

    val right: String
        get() = data.indices.map { data[it][data[it].size - 1] }.joinToString("")

    val variations: List<Tile202>
        get() = buildVariations(this)

    val borders: List<String>
        get() = buildList {
            add(top)
            add(buttom)
            add(left)
            add(right)
        }

    fun isACorner(tiles: List<Tile20>): Boolean {
        val tileBordersWithNoCorrespondence = this.borders.filter { border ->
            tiles.map { it.variations }.flatten().none { it.borders.contains(border) }
        }.count()
        return tileBordersWithNoCorrespondence >= 2
    }

    fun correspondingBorders(tiles: List<Tile20>): Int {
        val tileBordersWithNoCorrespondence = this.borders.filter { border ->
            tiles.none { tile -> tile.variations.any { it.borders.contains(border) } }
        }
        return tileBordersWithNoCorrespondence.count()
    }

    private fun buildVariations(origin: Tile202): List<Tile202> = buildList {
        add(origin)
        add(flip(Tile202(origin.id, origin.lines, origin.size, "flipped")))
        val rotated = rotateClockwise(Tile202(origin.id, origin.lines, origin.size, "Rotate 1"))
        add(rotated)
        add(flip(Tile202(origin.id, origin.lines, origin.size, rotated.info + " + flipped")))
        val rotated2 = rotateClockwise(rotateClockwise(Tile202(origin.id, origin.lines, origin.size, "Rotate 2")))
        add(rotated2)
        add(flip(rotated2.copy(info = rotated2.info + " + flipped")))
        val rotated3 = rotateClockwise(rotated2.copy(info = "Rotate 3"))
        add(rotated3)
        add(flip(rotated3.copy(info = rotated3.info + " + flipped")))
    }

    companion object {
        fun rotateClockwise(tile: Tile202): Tile202 {
            val size = tile.data.size
            val tmp: Array<Array<Char>> = Array(size) { Array(size) { '.' } }
            for (i in tile.data.indices) {
                for (j in tile.data[i].indices) {
                    tmp[j][size - 1 - i] = tile.data[i][j]
                }
            }
            for (i in tmp.indices) {
                for (j in tmp[i].indices) {
                    tile.data[i][j] = tmp[i][j]
                }
            }
            return tile
        }

        fun flip(tile: Tile202): Tile202 {
            val size = tile.data.size
            for (i in tile.data.indices) {
                for (j in 0..(tile.data[i].indices.last / 2)) {
                    val tmp = tile.data[i][j]
                    tile.data[i][j] = tile.data[i][size - 1 - j]
                    tile.data[i][size - 1 - j] = tmp
                }
            }
            return tile
        }
    }
}
