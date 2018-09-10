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

    fun writeAllProductsByCategory(categoryUrl: String) {
        var counter = 1
        var hasNext = writeProductToFile(categoryUrl, counter)

        while (hasNext) {
            counter++
            hasNext = writeProductToFile(categoryUrl, counter)
        }
    }

    fun writeProductToFile(categoryUrl: String, pageNumber: Int = 1): Boolean {
        val url = categoryUrl.split(".kat")[0]

        try {
            val page: CategoryPage = parser.parseCategoryPage(Jsoup.connect("$url/page-$pageNumber.kat").get())

            page.getUrls().parallelStream().forEach {
                try {
                    val page: ProductPage = parser.parseProductPage(Jsoup.connect(it).get())

                    if (page.hasMicroplastic() && page.getEan() != "Bitte ergänzen") {
                        val text = "${page.getEan()},${page.getMicroplastic()}\n"
                        File("beadables.csv").appendText(text)
                    }

                } catch (e: Exception) {
                    print("Deadlink for product link: $it")
                }

            }
            return page.hasNext()
        } catch (e: Exception) {
            print("Deadlink for category link: $url")
            return false
        }

    }
}