import com.beust.klaxon.Klaxon
import org.jsoup.Jsoup
import java.io.File
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
        return Product(page.getEan(), page.getMicroplastic())
    }

    fun writeProductToFile(categoryUrl: String) {
        getProductLinksForCategory(categoryUrl).forEach {
            val page: ProductPage = parser.parseProductPage(Jsoup.connect(it).get())

            if (page.hasMicroplastic() && page.getEan() != null) {
                val text = "${page.getEan()},${page.getMicroplastic()}\n"
                File("beadables.csv").appendText(text)
            }
        }

    }
}

class Product(val ean: String?, microplastic: String?)