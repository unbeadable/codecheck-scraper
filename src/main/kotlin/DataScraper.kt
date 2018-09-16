import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import pages.CategoryPage
import pages.ProductPage

data class ScrapingResult(
        val products: List<Product>,
        val visitedLinks: Int,
        val brokenLinks: Int)

class DataScraper {
    private var document2page: DocumentToPage = DocumentToPage()
    private var page2data: PageToData = PageToData()

    private var linksVisited: Int = 0
    private var deadLinks: Int = 0

    private fun getDocumentBy(url: String): Document? {
        return try {
            Jsoup.connect(url).get()
        } catch (e: Exception) {
            println("Could not get document for: $url with error $e")
            deadLinks++
            null
        }
    }

    private fun getUrlBy(categoryUrl: String, pageNumber: Int): String {
        return "${categoryUrl.split(".kat")[0]}/page-$pageNumber.kat"
    }

    private fun getProductByUrl(productUrl: String): ProductPage {
        linksVisited++
        return document2page.convertToProductPage(getDocumentBy(productUrl), productUrl)
    }

    private fun getProductsBy(categoryUrl: String, pageNumber: Int): Pair<Boolean, Iterable<ProductPage>> {
        val url = getUrlBy(categoryUrl, pageNumber)
        val page: CategoryPage = document2page.convertToCategoryPage(getDocumentBy(url))
        val products = page.productUrls.map(this::getProductByUrl)

        return Pair(page.hasNext(), products)
    }

    fun getAllProductsBy(categoryUrl: String): ScrapingResult {
        linksVisited = 0
        deadLinks = 0

        var counter = 1
        var pair = getProductsBy(categoryUrl, counter)
        var hasNext = pair.first
        var productPages = pair.second

        while (hasNext) {
            counter++
            pair = getProductsBy(categoryUrl, counter)
            hasNext = pair.first
            productPages = productPages.union(pair.second)
        }

        val products = productPages.map { it -> page2data.convert(it) }

        return ScrapingResult(products, linksVisited, deadLinks)
    }
}