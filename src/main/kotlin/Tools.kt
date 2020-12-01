import java.nio.charset.Charset

object Tools {
    const val fileTemplate = "Day%02dInput.txt"
    fun readInput(day: Int): List<String> {
        val fileName = fileTemplate.format(day)
        val resource = this::class.java.getResource(fileName) ?: throw Exception("File $fileName not found")
        return resource.readText(Charset.defaultCharset()).split("\n").map { it.trim() }
    }

    fun readInputInts(day: Int) = readInput(day).mapNotNull { it.toIntOrNull() }
}

