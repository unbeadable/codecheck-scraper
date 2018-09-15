import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Test



class CsvToJsonTest {
    @Test
    fun shouldMapJsonToCsv() {
        val parser = CsvToJsonParser()
        val result: List<AsinScopeCsvLine> = parser.parseAsinCSV()
        Assert.assertThat(result[0], `is`(AsinScopeCsvLine("someAsin", "someEan")))
    }
}