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

    fun getProductLinksForCategory(categoryUrl: String, pageNumber: Int = 1): List<String> {
        val url = categoryUrl.split(".kat")[0]
        val page: CategoryPage = parser.parseCategoryPage(Jsoup.connect("$url/page-$pageNumber.kat").get())
        return page.getUrls()
    }

    fun getProductInformationForUrl(productUrl: String): Product {
        val page: ProductPage = parser.parseProductPage(Jsoup.connect(productUrl).get())
        return Product(page.getEan(), page.getMicroplastic())
    }

    fun writeAllProductsByCategory(categoryUrl: String) {
        var counter = 1
        var hasNext = writeProductToFile(categoryUrl, counter)

        while(hasNext) {
            print("Currently on page: $counter")
            counter++
            hasNext = writeProductToFile(categoryUrl, counter)
        }
    }

    fun writeProductToFile(categoryUrl: String, pageNumber: Int = 1): Boolean {
        val url = categoryUrl.split(".kat")[0]
        val page: CategoryPage = parser.parseCategoryPage(Jsoup.connect("$url/page-$pageNumber.kat").get())

        page.getUrls().forEach {
            val page: ProductPage = parser.parseProductPage(Jsoup.connect(it).get())

            if (page.hasMicroplastic() && page.getEan() != "Bitte ergänzen" ) {
                val text = "${page.getEan()},${page.getMicroplastic()}\n"
                File("beadables.csv").appendText(text)
            }
        }
        return page.hasNext()
    }
}

class Product(val ean: String?, microplastic: String?)