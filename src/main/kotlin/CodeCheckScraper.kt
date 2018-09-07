import com.beust.klaxon.Klaxon
import org.jsoup.Jsoup
import java.io.InputStream


class CodeCheckScraper {
    private var parser: CodeCheckParser = CodeCheckParser()

    fun getCategoryLinksFromFile(): List<String>? {
        val stream: InputStream = CodeCheckScraper::class.java.getResource("codecheckLinks.json").openStream()
        return Klaxon().parseArray(stream)
    }

    fun getProductLinksForCategory(categoryUrl: String): List<String> {
        val page: CategoryPage = parser.parseCategoryPage(Jsoup.connect(categoryUrl).get())
        return page.getUrls()
    }

    fun getProductInformationForUrl(productUrl: String): Product {
        val page: ProductPage = parser.parseProductPage(Jsoup.connect(productUrl).get())
        return Product(page.getProductName())
    }
}

class Product(val name: String)
