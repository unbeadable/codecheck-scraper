import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.junit.Before
import org.junit.Test
import java.io.File

class CodeCheckParser{
    fun parse(filePath: String): ProductPage {
        val document: Document = Jsoup.parse(File(filePath), "UTF-8")
        return ProductPage(document)
    }
}

class ProductPage(private val document: Document) {
    fun getEan(): String? {
        val find: Element? = document.getElementsByClass("product-info-item")
                .find { it.getElementsByClass("product-info-label").text() == "Strichcode-Nummer" }
        return find?.child(1)?.text()
    }
}

class CodecheckParserTest {

    private lateinit var parser: CodeCheckParser

    @Before
    fun setUp() {
        parser = CodeCheckParser()
    }

    @Test
    fun shouldFindEAN() {
    	val page: ProductPage = parser.parse("codecheck.html")
        assertThat(page.getEan(), `is`("30096998"))
    }
}