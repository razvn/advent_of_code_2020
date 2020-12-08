import Log.log

fun main() = Day08()

object Day08 : Day(8, false) {
    override fun invoke() {
        val input = getInputData { it }

        part1(input) { inputdata ->
            val (index, acc) = runProgram(inputdata)
            "line: $index - acc: $acc"
        }

        part2(input) { inputdata ->
            var index = 0
            var acc = 0
            var line = 0
            while (index < inputdata.size - 1) {
                val linevalue = inputdata[line]
                val newCommand = when {
                    linevalue.startsWith("jmp") -> linevalue.replace("jmp", "nop")
                    linevalue.startsWith("nop") -> linevalue.replace("nop", "jmp")
                    else -> ""
                }

                if (newCommand.isNotEmpty()) {
                    // flog("New line for $line: $newCommand")
                    val newProgram = inputdata.toMutableList()
                    newProgram[line] = newCommand
                    val result = runProgram(newProgram)
                    index = result.first
                    acc = result.second
                }
                line++
            }

            "line: $index - acc: $acc"
        }
    }

    private fun runProgram(inputdata: List<String>): Pair<Int, Int> {
        val idxRun = mutableSetOf<Int>()
        var acc = 0
        var index = 0

        do {
            idxRun += index
            val (cmd, valueTxt) = inputdata[index].split(" ")
            val value = valueTxt.toInt()
            when (cmd) {
                "nop" -> index++
                "acc" -> {
                    acc += value
                    index++
                }
                "jmp" -> index += value
                else -> throw Exception("Unknown command")
            }
            log("line: $index - acc: $acc - cmd: $cmd")
        } while (!idxRun.contains(index) && index < inputdata.size - 1)

        return index to acc
    }
}
