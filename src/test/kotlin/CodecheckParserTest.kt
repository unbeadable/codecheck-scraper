import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.junit.Before
import org.junit.Test
import java.io.File

class CodeCheckParser{
    fun parseProductPage(document: Document): ProductPage {
        return ProductPage(document)
    }

    fun parseSearchResultsPage(document: Document): SearchResultsPage {
        return SearchResultsPage(document)
    }
}

class ProductPage(private val document: Document) {
    fun getEan(): String? {
        val find: Element? = document.getElementsByClass("product-info-item")
                .find { it.getElementsByClass("product-info-label").text() == "Strichcode-Nummer" }
        return find?.child(1)?.text()
    }
}

class SearchResultsPage(private val document: Document) {
    fun getProducts(): List<Product>? {
        return document.getElementsByClass("search-result")
                .filter { it.isProduct() }
                .map { it.asProduct() }
    }
}

private fun Element.asProduct(): Product {
    val relativePath = this.getElementsByAttribute("href").attr("href")
    return Product("https://www.codecheck.info$relativePath")
}

private fun Element.isProduct(): Boolean {
    return this.getElementsByClass("middle").first().getElementsByClass("lower").size == 1
}

class Product(val url: String)

class CodecheckParserTest {

    private lateinit var parser: CodeCheckParser

    @Before
    fun setUp() {
        parser = CodeCheckParser()
    }

    @Test
    fun shouldFindEAN() {
        val filePath: String = CodecheckParserTest::class.java.getResource("codecheck.html").file
        val page: ProductPage = parser.parseProductPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page.getEan(), `is`("30096998"))
    }

    @Test
    fun shouldFindProductUrlsOnFirstPage() {
        val filePath: String = CodecheckParserTest::class.java.getResource("searchResults.html").file
        val page: SearchResultsPage = parser.parseSearchResultsPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page.getProducts()!![0].url, `is`("https://www.codecheck.info/kosmetik_koerperpflege/gesichtspflege/gesichtsmasken/id_2096663156/Tony_Moly_Timeless_Ferment_Snail_Gel_Mask.pro"))
        assertThat(page.getProducts()!![1].url, `is`("https://www.codecheck.info/kosmetik/gesichtspflege/gesichtscremen/ean_20714806323/id_34370165/Clinique_Pep_Start_Hydroblur_Moisturizer_Gesichtscreme_50_ml.pro"))
    }
}


