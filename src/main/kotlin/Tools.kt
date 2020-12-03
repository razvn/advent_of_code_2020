import java.nio.charset.Charset

object Tools {
    const val fileTemplate = "Day%02dInput.txt"
    const val testfileTemplate = "Day%02dTest.txt"
    fun readInput(day: Int, test: Boolean = false): List<String> {
        val fileName = (if (test) testfileTemplate else fileTemplate).format(day)
        val resource = this::class.java.getResource(fileName) ?: throw Exception("File $fileName not found")
        return resource.readText(Charset.defaultCharset()).split("\n").map { it.trim() }
    }

    fun readInputInts(day: Int) = readInput(day).mapNotNull { it.toIntOrNull() }
}

object Colors {
    const val ANSI_RESET = "\u001B[0m"
    const val ANSI_BLACK = "\u001B[30m"
    const val ANSI_RED = "\u001B[31m"
    const val ANSI_GREEN = "\u001B[32m"
    const val ANSI_YELLOW = "\u001B[33m"
    const val ANSI_BLUE = "\u001B[34m"
    const val ANSI_PURPLE = "\u001B[35m"
    const val ANSI_CYAN = "\u001B[36m"
    const val ANSI_WHITE = "\u001B[37m"
    const val ANSI_BLACK_BACKGROUND = "\u001B[40m"
    const val ANSI_RED_BACKGROUND = "\u001B[41m"
    const val ANSI_GREEN_BACKGROUND = "\u001B[42m"
    const val ANSI_YELLOW_BACKGROUND = "\u001B[43m"
    const val ANSI_BLUE_BACKGROUND = "\u001B[44m"
    const val ANSI_PURPLE_BACKGROUND = "\u001B[45m"
    const val ANSI_CYAN_BACKGROUND = "\u001B[46m"
    const val ANSI_WHITE_BACKGROUND = "\u001B[47m"

    fun green(s: Any) = "$ANSI_GREEN $s $ANSI_RESET"
    fun yellow(s: Any) = "$ANSI_YELLOW $s $ANSI_RESET"
    fun red(s: Any) = "$ANSI_RED $s $ANSI_RESET"
}
