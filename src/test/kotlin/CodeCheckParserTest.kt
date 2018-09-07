import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.jsoup.Jsoup
import org.junit.Before
import org.junit.Test
import java.io.File

class CodeCheckParserTest {

    private lateinit var parser: CodeCheckParser

    @Before
    fun setUp() {
        parser = CodeCheckParser()
    }

    @Test
    fun shouldFindEAN() {
        val filePath: String = CodeCheckParserTest::class.java.getResource("codecheck.html").file
        val page: ProductPage = parser.parseProductPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page.getEan(), `is`("30096998"))
    }

    @Test
    fun shouldFindProductUrlsOnFirstPage() {
        val filePath: String = CodeCheckParserTest::class.java.getResource("searchResults.html").file
        val page: SearchResultsPage = parser.parseSearchResultsPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page.getProducts()!![0].url, `is`("https://www.codecheck.info/kosmetik_koerperpflege/gesichtspflege/gesichtsmasken/id_2096663156/Tony_Moly_Timeless_Ferment_Snail_Gel_Mask.pro"))
        assertThat(page.getProducts()!![1].url, `is`("https://www.codecheck.info/kosmetik/gesichtspflege/gesichtscremen/ean_20714806323/id_34370165/Clinique_Pep_Start_Hydroblur_Moisturizer_Gesichtscreme_50_ml.pro"))
    }
}


