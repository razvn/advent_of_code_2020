import java.lang.Integer.max
import kotlin.Exception
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() = Day14()


@ExperimentalTime
object Day14 : Day(14, testData = false, "1") {
    override fun invoke() {
        val input = getInputData {
            val (command, value) = it.split("=").map { it.trim() }
            if (command == "mask") command to value
            else {
                command.replace("mem[", "").replace("]", "") to value
            }
        }

        part1(input) { data ->
            val memory = mutableMapOf<String, Long>()
            var mask = emptyList<Char>()
            data.forEach { (command, value) ->
                if (command == "mask") mask = value.toList()
                else {
                    val binList = value.toIntOrNull()?.toString(2)?.toList() ?: throw Exception("Should be an int: $value")
                    val delta = max(mask.size - binList.size, 0)
                    val newValue = mask.mapIndexed { idx, v ->
                        if (v == 'X') { binList.getOrNull(idx-delta) ?: '0' }
                        else v
                    }.joinToString("").toLong(2)
                    memory[command] = newValue
                }
            }

            // println(memory)
            val result = memory.values.sum()
            result.toString()
        }

        part2(input) { data ->
            val memory = mutableMapOf<String, Long>()
            var mask = emptyList<Char>()
            data.forEach { (command, value) ->
                if (command == "mask") mask = value.toList()
                else {
                    val binList = command.toInt().toString(2).toList()
                    val delta = max(mask.size - binList.size, 0)
                    val maskedAddress = mask.mapIndexed { idx, v ->
                        when(v) {
                            '0' -> binList.getOrNull(idx-delta) ?: '0'
                            '1' -> '1'
                            'X' -> 'X'
                            else -> throw Exception("Unexpected value: $v")
                        }
                    }
                    val newAddressesList: List<Long> = possibleValues(maskedAddress)
                    newAddressesList.forEach {
                        memory[it.toString()] = value.toLong()
                    }
                }
            }

            // println(memory)
            val result = memory.values.sum()
            result.toString()
        }
    }

    private fun possibleValues(value: List<Char>): List<Long> {
        val values = getAlternatives(value)
        return values.map {
            it.joinToString("").toLong(2)
        }
    }

    private fun getAlternatives(values: List<Char>): List<List<Char>> {
        return if (!values.contains('X')) listOf(values)
        else {
            val idx = values.indexOf('X')
            val list1 = values.toMutableList()
            list1[idx] = '0'
            val list2 = values.toMutableList()
            list2[idx] = '1'
            listOf(getAlternatives(list1), getAlternatives(list2)).flatten()
        }
    }
}
