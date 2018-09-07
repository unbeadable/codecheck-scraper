import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class CodeCheckScraperTest {
    private lateinit var scraper: CodeCheckScraper

    @Before
    fun setUp() {
        scraper = CodeCheckScraper()
    }

    @Test
    fun shouldGetLinksFromFile() {
        assertThat(scraper.getLinksFromFile()!![0], `is`("https://www.codecheck.info/kosmetik_koerperpflege/aetherische_oele_pflanzenauszuege/aetherische_oele_mischungen.kat"))
        assertThat(scraper.getLinksFromFile()!![1], `is`("https://www.codecheck.info/kosmetik_koerperpflege/aetherische_oele_pflanzenauszuege/aetherische_oele_100_rein.kat"))
    }

}
