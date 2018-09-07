import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class CodeCheckScraperTest {
    private lateinit var scraper: CodeCheckScraper

    @Before
    fun setUp() {
        scraper = CodeCheckScraper()
    }

    @Test
    fun shouldGetLinksFromFile() {
        assertThat(scraper.getCategoryLinksFromFile()!![0], `is`("https://www.codecheck.info/kosmetik_koerperpflege/aetherische_oele_pflanzenauszuege/aetherische_oele_mischungen.kat"))
        assertThat(scraper.getCategoryLinksFromFile()!![1], `is`("https://www.codecheck.info/kosmetik_koerperpflege/aetherische_oele_pflanzenauszuege/aetherische_oele_100_rein.kat"))
    }

    @Test
    fun shouldGetLinksByCategory() {
        assertThat(scraper.getProductLinksForCategory("https://www.codecheck.info/kosmetik_koerperpflege/aetherische_oele_pflanzenauszuege/aetherische_oele_mischungen.kat")[0],
                `is`(notNullValue()))
    }

    @Test
    fun shouldWriteProductToFile() {
        scraper.writeProductToFile("https://www.codecheck.info/kosmetik_koerperpflege/koerperpflege/koerperpeelings.kat")
    }

    @Test
    @Ignore
    fun shouldWriteAllProductsByCategoryToFile() {
        scraper.writeAllProductsByCategory("https://www.codecheck.info/kosmetik_koerperpflege/koerperpflege/koerperpeelings.kat")
    }

}
