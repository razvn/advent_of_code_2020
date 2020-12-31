import java.time.LocalDateTime
import kotlin.streams.toList
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@ExperimentalTime
fun main() = Day23()

data class Node23(val value: Int) {
    var next: Node23? = null
    var prev: Node23? = null
}

@ExperimentalTime
object Day23 : Day(23, testData = false) {

    private fun makeNodes(input: List<Int>): Node23 {
        val nodes = mutableListOf<Node23>()

        var prevNode: Node23? = null
        input.forEach {
            val currentNode = Node23(it)
            currentNode.prev =  prevNode
            if (prevNode != null) {
                prevNode!!.next = currentNode
                currentNode.prev = prevNode
            }
            prevNode = currentNode
            nodes.add(currentNode)
        }
        val first = nodes.first()
        val last = nodes.last()
        first.prev = last
        last.next = first

        return first
    }

    private fun makeNodesList(input: List<Int>): Sequence<Node23> {
        val nodes = mutableListOf<Node23>()

        var prevNode: Node23? = null
        input.forEach {
            val currentNode = Node23(it)
            currentNode.prev = prevNode
            if (prevNode != null) {
                prevNode!!.next = currentNode
                currentNode.prev = prevNode
            }
            prevNode = currentNode
            nodes.add(currentNode)
        }
        val first = nodes.first()
        val last = nodes.last()
        first.prev = last
        last.next = first

        return nodes.asSequence()
    }

    private fun findDestination(node: Node23, picked: List<Int>, max: Int, nodes: Sequence<Node23>): Node23 {
        var destValue = node.value - 1

        while(destValue > 0 && destValue in picked) {
            destValue--
        }

        if (destValue == 0) {
            destValue = max
            while (destValue in picked) {
                destValue --
            }
        }

        return nodes.first { it.value == destValue }
    }

    fun findIndex(idx: Int, node: Node23): Node23 {
        var current = node
        while(current.value != idx) current = current.next!!
        return current
    }

    fun valuesAfter(node: Node23): List<Int> {
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
            val maxValue = dataIn.maxOrNull() ?: 0
            val nodes = makeNodesList(dataIn)
            var current = nodes.first()

            (0  until moves).forEach { move ->
                val pickFirst = current.next!!
                val pickSecond = pickFirst.next!!
                val pickThird = pickSecond.next!!

                val afterPicked = pickThird.next!!

                current.next = afterPicked
                afterPicked.prev = current

                val pickedValues = listOf(pickFirst.value, pickSecond.value, pickThird.value)

                val destination = findDestination(current, pickedValues, maxValue, nodes)
                val destNext = destination.next!!
                destination.next = pickFirst
                pickFirst.prev = destination

                pickThird.next = destNext
                destNext.prev = pickThird

                current = current.next!!
            }

            val idx1 = findIndex(1, current)
            val res = valuesAfter(idx1)

            res.joinToString("")
        }

        part2(input) { data ->
            val dataIn = data + (10..1_000_000)
            val maxValue = 1_000_000
            val moves = 10_000_000
            // val nodes = makeNodes(dataIn)
            var destTime = 0

            val nodesList = makeNodesList(dataIn)
            var current = nodesList.first()

            val parts = 100_000
            var startTime = System.currentTimeMillis()
            (0  until moves).forEach { move ->
                if (move % parts == 0) {
                    println("$move: ${LocalDateTime.now()} | dest: $destTime / ${System.currentTimeMillis() - startTime}")
                    destTime = 0
                    startTime = System.currentTimeMillis()
                }

                val pickFirst = current.next!!
                val pickSecond = pickFirst.next!!
                val pickThird = pickSecond.next!!

                val afterPicked = pickThird.next!!

                current.next = afterPicked
                afterPicked.prev = current

                val pickedValues = listOf(pickFirst.value, pickSecond.value, pickThird.value)
                val destinationTime = measureTimedValue { findDestination(current, pickedValues, maxValue, nodesList) }
                destTime += destinationTime.duration.inMilliseconds.toInt()
                val destination = destinationTime.value
                val destNext = destination.next!!

                destination.next = pickFirst
                pickFirst.prev = destination

                pickThird.next = destNext
                destNext.prev = pickThird

                current = current.next!!
            }

            val idx1 = findIndex(1, current)
            val value1 = idx1.next!!.value
            val value2 = idx1.next!!.next!!.value
            val res = value1.toLong() * value2.toLong()

            "$res"
        }
    }
}