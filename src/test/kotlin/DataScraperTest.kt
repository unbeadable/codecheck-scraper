import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class DataScraperTest {
    private lateinit var scraper: DataScraper

    @BeforeEach
    fun setUp() {
        scraper = DataScraper()
    }

    @Test
    @Disabled("This test is only for manual testing, it takes > 5 minutes")
    fun shouldGetAllProductsByCategoryUrl()  {
        scraper.getAllProductsBy("https://www.codecheck.info/kosmetik_koerperpflege/koerperpflege/koerperpeelings.kat")
    }
}
