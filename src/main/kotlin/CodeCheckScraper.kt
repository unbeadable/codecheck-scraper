import com.beust.klaxon.JsonObject
import com.google.gson.Gson
import org.jsoup.Jsoup
import pages.CategoryPage
import pages.ProductPage
import java.io.File
import java.time.LocalDateTime

data class Result(val products: JsonObject, val linksVisited: Int, val deadlinks: Int)

class CodeCheckScraper {
    private var parser: CodeCheckParser = CodeCheckParser()
    private var linksVisited: Int = 0
    private var deadlinks: Int = 0

    fun writeProductWithIngredientsByCategory(categoryUrl: String): Any {

        linksVisited = 0
        deadlinks = 0

        var counter = 1
        var pair = writeProductWithIngredientsByCategoryPage(categoryUrl, counter)
        var hasNext = pair.first
        var products = pair.second

        while (hasNext) {
            counter++
            pair = writeProductWithIngredientsByCategoryPage(categoryUrl, counter)
            hasNext = pair.first
            products = products.union(pair.second)
        }

        val categoryJson = JsonObject(mapOf(
                Pair("url", categoryUrl),
                Pair("timestamp", LocalDateTime.now()),
                Pair("products", products)
        ))

        return Result(categoryJson, linksVisited, deadlinks)
    }

    fun writeProductWithIngredientsByCategoryPage(categoryUrl: String, pageNumber: Int = 1):Pair<Boolean, Iterable<JsonObject>> {
        val products = mutableListOf<JsonObject>()
        val url = "${categoryUrl.split(".kat")[0]}/page-$pageNumber.kat"

        try {
            val page: CategoryPage = parser.parseCategoryPage(Jsoup.connect(url).get())
            page.getUrls().stream().forEach {
                products.add(writeProductWithIngredients(it))
            }
            return Pair(page.hasNext(), products)
        } catch (e: Exception) {
            println("Deadlink for product link: $categoryUrl with error $e")
        }
        return Pair(false, products)
    }

    fun writeProductWithIngredients(productUrl: String): JsonObject {
        try {
            val page: ProductPage = parser.parseProductPage(Jsoup.connect(productUrl).get())
            linksVisited++
            return JsonObject(mapOf(
                    Pair("url", productUrl),
                    Pair("timestamp", LocalDateTime.now()),
                    Pair("ean", page.getEan()),
                    Pair("name", page.getProductName()),
                    Pair("category", page.getCategory()),
                    Pair("ingredients", page.getIngredients())))
        } catch (e: Exception) {
            deadlinks++
            println("Deadlink for product link: $productUrl with error $e")
        }
        return JsonObject()
    }
}