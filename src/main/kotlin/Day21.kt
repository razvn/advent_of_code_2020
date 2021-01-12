import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() = Day21()

data class Food21(
    val ingredients: List<String>,
    val allergens: List<String>
)

@ExperimentalTime
object Day21 : Day(21, testData = false) {
    override fun invoke() {
        val input = getInputData { line ->
            val (ing, all)  = line.split(" (contains ")
            Food21(
                ing.split(" ").map { it.trim() },
                all.dropLast(1).split(",").map { it.trim() }
            )
        }

        part1(input) { data ->
            val allIng = mutableMapOf<String, MutableSet<String>>()

            data.forEach { food ->
                food.allergens.forEach { allIng.getOrPut(it) { mutableSetOf<String>() }.addAll(food.ingredients) }
            }
            data.forEach { food ->
                food.allergens.forEach { allerg ->
                    val alergIng = allIng[allerg]!!.toList()
                    alergIng.forEach {
                        if (!food.ingredients.contains(it)) allIng[allerg]!!.remove(it)
                    }

                }
            }
            val remainAlerg = allIng.values.flatten().toSet()
            println(allIng)
            data.map { food ->
                food.ingredients.filter { !remainAlerg.contains(it) }.count()
            }.sum().toString()
        }

        part2(input) { data ->
            val allIng = mutableMapOf<String, MutableSet<String>>()

            data.forEach { food ->
                food.allergens.forEach { allIng.getOrPut(it) { mutableSetOf<String>() }.addAll(food.ingredients) }
            }
            data.forEach { food ->
                food.allergens.forEach { allerg ->
                    val alergIng = allIng[allerg]!!.toList()
                    alergIng.forEach {
                        if (!food.ingredients.contains(it)) allIng[allerg]!!.remove(it)
                    }

                }
            }
            println("Multiple: $allIng")
            while (allIng.any { it.value.size > 1 }) {
                allIng.filter { it.value.size == 1 }.forEach { ai ->
                    val ingToRemove = ai.value.first()
                    allIng.filter {
                        it.value.count() > 1 && it.value.contains(ingToRemove)
                    }.forEach { _, u ->  u.remove(ingToRemove)}
                }
            }
            println("Unic: $allIng")
            allIng.keys.sorted().map {
                allIng[it]!!.first()
            }.joinToString(",")
        }
    }
}