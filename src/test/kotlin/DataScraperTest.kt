import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class DataScraperTest {
    private lateinit var scraper: DataScraper

    @Before
    fun setUp() {
        scraper = DataScraper()
    }

    @Test
    @Ignore("This test is only for manual testing, it takes > 5 minutes")
    fun shouldGetAllProductsByCategoryUrl()  {
        scraper.getAllProductsBy("https://www.codecheck.info/kosmetik_koerperpflege/koerperpflege/koerperpeelings.kat")
    }
}
