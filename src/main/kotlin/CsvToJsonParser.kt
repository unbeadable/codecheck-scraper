import java.io.BufferedReader
import java.io.FileReader



data class AsinScopeCsvLine(
        val asin: String,
        val ean: String
)

class CsvToJsonParser {
    fun parseAsinCSV(): List<AsinScopeCsvLine> {
        val file = CsvToJsonParser::class.java.getResource("asin.csv").file
        val fileReader = BufferedReader(FileReader(file))

        removeColumnHeaders(fileReader)

        return fileReader.useLines { lines ->
            lines.map {
                val split: List<String> = it.split(",")
                AsinScopeCsvLine(split[0], split[1])
            }.toList()
        }
    }

    private fun removeColumnHeaders(fileReader: BufferedReader) {
        fileReader.readLine()
    }
}