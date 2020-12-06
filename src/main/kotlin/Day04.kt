import Colors.green
import Colors.red
import Log.log

fun main() = Day04()

object Day04 : Day(4, false) {
    override fun invoke() {
        val input = getInputData()

        part1(input) {
            mapData(it).count().toString()
        }

        part2(input) { listString ->
            mapData(listString).count { it.isValid() }.toString()
        }
    }

    private fun mapData(input: List<String>): List<Passport> {
        val bufferMap = mutableMapOf<String, String>()
        return input.mapNotNull { line ->
            if (line.isNotBlank()) {
                bufferMap.putAll(extractDataFromLine(line))
                null
            } else {
                //end previous passport infos
                createPassport(bufferMap).also { bufferMap.clear() }
            }
        }.let { passports -> // handle last line if not a blank one
            if (bufferMap.isEmpty()) passports
            else createPassport(bufferMap)?.let {
                passports + listOf(it)
            } ?: passports
        }
    }

    private fun extractDataFromLine(line: String) = line.split(" ")
        .mapNotNull { kv ->
            kv.split(":")
                .takeIf { it.size > 1 }
                ?.let { it[0] to it[1] }
        }

    private fun createPassport(data: Map<String, String>): Passport? {
        val reqFields = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
        val isValid = data.keys.containsAll(reqFields)
        log("Data: $data - valid: ${isValid.let { if (it) green(it) else red(it) }}")
        return if (isValid) {
            Passport(
                byr = data["byr"]!!,
                iyr = data["iyr"]!!,
                eyr = data["eyr"]!!,
                hgt = data["hgt"]!!,
                hcl = data["hcl"]!!,
                ecl = data["ecl"]!!,
                pid = data["pid"]!!,
                cid = data["cid"],
            )
        } else null
    }


    data class Passport(
        val byr: String,
        val iyr: String,
        val eyr: String,
        val hgt: String,
        val hcl: String,
        val ecl: String,
        val pid: String,
        val cid: String? = null
    ) {
        private val hairRegex = "#[0-9a-f]{6}".toRegex()
        private val passportRegex = "[0-9]{9}".toRegex()
        private val validEyeColor = "amb blu brn gry grn hzl oth".split(" ")

        fun isValid(): Boolean {
            val resp = isBirthdayValid()
                    &&
                    isIssueValid()
                    &&
                    isExpirationValid()
                    &&
                    isHeightValid()
                    &&
                    isHairValid()
                    &&
                    isEyeValid()
                    &&
                    isPasIdValid()


            log(
                "Passport: [byr: $byr = ${isBirthdayValid().toColor()}] - [iyr: $iyr = ${isIssueValid().toColor()}] - " +
                        "[eyr: $eyr = ${isExpirationValid().toColor()}] - [hgt: $hgt = ${isHeightValid().toColor()}] - [hcl: $hcl = ${isHairValid().toColor()}] - " +
                        "[ecl: $ecl = ${isEyeValid().toColor()}] - [pid: $pid = ${isPasIdValid().toColor()}]"
            )

            return resp
        }

        private fun isExpirationValid() = eyr.isIntInRange(2020, 2030)
        private fun isIssueValid() = iyr.isIntInRange(2010, 2020)
        private fun isBirthdayValid() = byr.isIntInRange(1920, 2002)

        private fun isHeightValid(): Boolean = when {
            hgt.endsWith("cm") -> hgt.removeSuffix("cm").isIntInRange(150, 193)
            hgt.endsWith("in") -> hgt.removeSuffix("in").isIntInRange(59, 76)
            else -> false
        }

        private fun isHairValid(): Boolean = hairRegex.matches(hcl)
        private fun isEyeValid(): Boolean = validEyeColor.contains(ecl)
        private fun isPasIdValid(): Boolean = passportRegex.matches(pid)

        private fun String.isIntInRange(start: Int, end: Int): Boolean = this.toIntOrNull()?.let { it in start..end }
                ?: false
    }
}
