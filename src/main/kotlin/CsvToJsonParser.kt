import java.io.BufferedReader
import java.io.FileReader



data class AsinScopeCsvLine(
        val asin: String,
        val ean: String
)

class CsvToJsonParser {

    fun doStuff(): List<AsinScopeCsvLine> {
        val file = CsvToJsonParser::class.java.getResource("asin.csv").file
        val fileReader = BufferedReader(FileReader(file))

        fileReader.readLine()

        return fileReader.useLines {
            it.map {
                val split: List<String> = it.split(",")
                AsinScopeCsvLine(split[0], split[1])
            }.toList()
        }
    }
}