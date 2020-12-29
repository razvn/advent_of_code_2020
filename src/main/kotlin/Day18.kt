import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() = Day18()
@ExperimentalTime
object Day18 : Day(18, testData = false, "1") {
    override fun invoke() {
        val input = getInputData { it }

        part1(input) { data ->
            data.mapIndexed { idx, it ->
                val res = calcPart1(it, 0)
                // val res = calcReplaceingPartheses(it, ::doMathPart1)
                // println("$idx | $it => $res")
                res
            }.sum().toString()
        }

        part2(input) { data ->
            data.mapIndexed { idx, it ->
                // val res = calc(it, 0)
                val res = calcReplaceingPartheses(it, ::doMathPart2)
                // println("$idx | $it => $res")
                res
            }.sum().toString()
        }
    }

    // calculates by resolving each paratheses and replaceing in the string the parantheses with the calculated value
    private fun calcReplaceingPartheses(s: String, mathFun: (String) -> Long): Long {
        var t = s
        val firstP = t.indexOf("(")
        return if (firstP >= 0) {
            val lastP = closeingMatchingParanthese(t.substring(firstP + 1)) + firstP
            val newS = t.substring(firstP + 1, lastP)
            //println("Calc: $s.. deeper: $firstP .. $lastP : $newS")
            val new = calcReplaceingPartheses(newS, mathFun)
            t = buildString {
                if (firstP > 0) append(t.substring(0, firstP))
                append("$new")
                if (lastP < t.indices.last) append(t.substring(lastP + 1))
            }
            calcReplaceingPartheses(t, mathFun)
            //
        } else {
            mathFun(s)
        }
    }

    // searche the matching closing paranthese index
    private fun closeingMatchingParanthese(s: String): Int {
        var idx = 0
        var openParantese = 1
        while ((openParantese != 0) && (idx <= s.indices.last)) {
            when (s[idx]) {
                '(' -> openParantese++
                ')' -> openParantese--
            }
            idx++
        }
        // println("ClosingMatch: $s => $idx")
        return idx
    }

    // calculates values (not parentheses inside) for part 2 (priority to addition)
    private fun doMathPart2(s: String): Long {
        val numbers = s.split("*", "+").map { it.trim().toLong() }
        val operations = s.toList().filter { it in listOf('*', '+') }
        val toMultiplay = mutableListOf<Long>()
        var prev: Long? = null
        operations.forEachIndexed { idx, op ->
            if (op == '*') {
                toMultiplay.add(prev ?: numbers[idx])
                prev =  numbers[idx + 1]
            }
            else {
                prev = (prev ?: numbers[idx]) +  numbers[idx + 1]
            }
        }

        toMultiplay.add(prev ?: 1)

        return  toMultiplay.reduce { acc, value -> acc * value}
    }

    // calculates values (not parentheses inside) for part 1 (no priority) -> it's slower that the other version
    private fun doMathPart1(s: String): Long {
        val numbers = s.split("*", "+").map { it.trim().toLong() }
        val operations = s.toList().filter { it in listOf('*', '+') }
        return numbers.reduceIndexed { idx, acc, value ->
            val op = operations[idx - 1]
            when (op) {
                '+' -> acc + value
                '*' -> acc * value
                else -> throw Exception("Unknown operation: $op")
            }
        }
    }

    //
    private fun calcPart1(s: String, startValue: Long): Long {
        var op1: Long? = null
        var op: Char? = null
        var op2: Long? = null
        var index = 0
        var total = startValue
        while (index in s.indices) {
            when (val char = s[index]) {
                in '0'..'9' -> if (op1 == null) op1 = "$char".toLong() else op2 = "$char".toLong()
                '+', '*' -> {
                    if (op1 == null) op1 = total
                    op = char
                }
                '(' -> {
                    val start = index + 1
                    //val end = s.indexOf(")", startIndex = index)
                    val end = closeingMatchingParanthese(s.substring(startIndex = start)) + start
                    val subOp = s.substring(start, end - 1)
                    // println("Sub op: $start .. $end = $subOp")
                    calcPart1(subOp, total).let {
                        if (op1 == null) {
                            total = it
                            op1 = it
                        } else op2 = it
                    }
                    index = end
                }
            }

            index++
            if (op1 != null && op2 != null && op != null) {
                total = when (op) {
                    '+' -> op1!! + op2!!
                    '*' -> op1!! * op2!!
                    else -> throw Exception("Unknown operation '$op")
                }
                // println("$op1 $op $op2 = $total")
                op1 = total
                op2 = null
            }
        }

        // println("$s => Total: $total")
        return total
    }
}