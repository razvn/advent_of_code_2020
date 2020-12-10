import Log.flog
import Log.log
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() = Day07()

data class TreeNode(val name: String, val nb: Int) {
    private val children: MutableList<TreeNode> = mutableListOf()

    fun add(child: TreeNode) = children.add(child)

    fun forEachDepthFirst(visit: (TreeNode) -> Unit) {
        visit(this)
        children.forEach {
            it.forEachDepthFirst(visit)
        }
    }

    fun nodeValue(): Int {
        return  this.nb + this.nb * children.map { it.nodeValue() }.sum()
    }

    override fun toString(): String {
        return "TreeNode: name=$name, nb=$nb, children=${children.map { it.toString() }}"
    }
}

@ExperimentalTime
object Day07 : Day(7, false ) {

    private tailrec fun containsName(nodes:List<TreeNode>, name: String, map: Map<String, List<TreeNode>>): Boolean  = when {
        nodes.any { it.name == name } -> true
        nodes.isEmpty() -> false
        else -> {
            val grandchilds = nodes.flatMap { map[it.name]!! }
            containsName(grandchilds, name, map)
        }
    }

    fun getNodeChildren(node: TreeNode, map:Map<String, List<TreeNode>>): TreeNode {
        val nodeChildren = map[node.name] ?: emptyList<TreeNode>()
        return if (nodeChildren.isEmpty()) node
        else {
            nodeChildren.forEach {
                node.add(getNodeChildren(it, map))
            }
            node
        }
    }

    override fun invoke() {
        val search = "shiny gold"
        val input = getInputData()
        part1(input) { data ->
            val mapData = mapToTree(data)

            val calcPair = mapData.map {
               it to containsName(it.value, search, mapData)
            }
            calcPair.map { log("\t$it") }

            "${calcPair.count { it.second }}"
        }

        part2(input) { data ->
            val mapData = mapToTree(data)

            val goldBagContent = mapData[search]!!
            goldBagContent.map { child ->
                getNodeChildren(child, mapData)
            }
            goldBagContent.forEach {
                log(it.toString())
            }
            "${goldBagContent.map { it.nodeValue() }.sum()}"
        }
    }

    private fun mapToTree(data: List<String>) = data.map { line ->
        val clean = line.replace(" bags", "")
            .replace(" bag", "")
            .removeSuffix(".")
        val bag = clean.substringBefore(" contain ").trim()

        val second = clean
            .substringAfterLast(" contain ")
            .trim()
            .split(",")
            .mapNotNull { element ->
                val nb = element.trim().substringBefore(' ').toIntOrNull()
                nb?.let {
                    val name = element.trim().substringAfter(" ").trim()
                    TreeNode(name, it)
                }
            }
        bag to second
    }.toMap()
}