import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class CsvToJsonTest {
    @Test
    fun shouldMapJsonToCsv() {
        val parser = CsvToJsonParser()
        val result: List<AsinScopeCsvLine> = parser.parseAsinCSV()
        assertThat(result[0]).isEqualTo(AsinScopeCsvLine("someAsin", "someEan"))
    }
}