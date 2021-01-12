import kotlin.streams.toList
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() = Day23()

data class Node23(val value: Int) {
    var next: Node23? = null
}

@ExperimentalTime
object Day23 : Day(23, testData = false) {

    private fun makeNodesList(input: List<Int>): List<Node23> {
        val nodes = mutableListOf<Node23>()

        var prevNode: Node23? = null
        input.forEach {
            val currentNode = Node23(it)
            if (prevNode != null) {
                prevNode!!.next = currentNode
            }
            prevNode = currentNode
            nodes.add(currentNode)
        }
        val first = nodes.first()
        val last = nodes.last()
        last.next = first

        return nodes
    }

    private fun findDestination(node: Node23, picked: IntArray, max: Int, nodes: IntArray): Int {
        var destValue = node.value - 1

        while (destValue > 0 && destValue in picked) {
            destValue--
        }

        if (destValue == 0) {
            destValue = max
            while (destValue in picked) {
                destValue--
            }
        }

        return nodes.binarySearch(destValue)
    }

    private fun valuesAfter(node: Node23): List<Int> {
        val data = mutableListOf<Int>()
        var current = node.next!!

        while (current.value != node.value) {
            data.add(current.value)
            current = current.next!!
        }

        return data
    }

    override fun invoke() {
        val input = getInputData { it }.first().chars().map { it - '0'.toInt() }.toList()

        part1(input) { dataIn ->
            val moves = 100

            val intArray = dataIn.sorted().toIntArray()
            val nodes = makeNodesList(dataIn).sortedBy { it.value }
            val firstValueIdx = intArray.binarySearch(dataIn.first())
            var current = nodes[firstValueIdx]
            val maxValue = intArray.maxOrNull() ?: 0

            (0 until moves).forEach { _ ->
                val pickFirst = current.next!!
                val pickSecond = pickFirst.next!!
                val pickThird = pickSecond.next!!

                val afterPicked = pickThird.next!!

                current.next = afterPicked

                val pickedValues = intArrayOf(pickFirst.value, pickSecond.value, pickThird.value)

                val destinationIdx = findDestination(current, pickedValues, maxValue, intArray)
                val destination = nodes[destinationIdx]
                val destNext = destination.next!!
                destination.next = pickFirst

                pickThird.next = destNext

                current = current.next!!
            }

            val idx1 = intArray.indexOf(1)
            val node1 = nodes[idx1]
            val res = valuesAfter(node1)

            res.joinToString("")
        }

        part2(input) { data ->
            val dataIn = data + (10..1_000_000)
            val maxValue = 1_000_000
            val moves = 10_000_000

            val intArray = dataIn.sorted().toIntArray()
            val nodes = makeNodesList(dataIn).sortedBy { it.value }
            val firstValueIdx = intArray.binarySearch(data.first())
            var current = nodes[firstValueIdx]

            (0 until moves).forEach { move ->
                val pickFirst = current.next!!
                val pickSecond = pickFirst.next!!
                val pickThird = pickSecond.next!!

                val afterPicked = pickThird.next!!

                current.next = afterPicked

                val pickedValues = intArrayOf(pickFirst.value, pickSecond.value, pickThird.value)

                val destinationIdx = findDestination(current, pickedValues, maxValue, intArray)
                val destination = nodes[destinationIdx]

                val destNext = destination.next!!
                destination.next = pickFirst

                pickThird.next = destNext

                current = current.next!!
            }

            val idx1 = intArray.binarySearch(1)
            val node1 = nodes[idx1]
            val value1 = node1.next!!.value
            val value2 = node1.next!!.next!!.value
            val res = value1.toLong() * value2.toLong()
            res.toString()
        }
    }
}