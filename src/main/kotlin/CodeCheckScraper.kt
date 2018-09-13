import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.google.gson.Gson
import org.jsoup.Jsoup
import java.io.File
import java.io.InputStream
import java.time.LocalDateTime


class CodeCheckScraper {
    private var parser: CodeCheckParser = CodeCheckParser()

    fun writeProductWithIngredientsByCategory(categoryUrl: String, filename: String) {
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

        File(filename).writeText(Gson().toJson(categoryJson))
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
            return JsonObject(mapOf(
                    Pair("url", productUrl),
                    Pair("timestamp", LocalDateTime.now()),
                    Pair("ean", page.getEan()),
                    Pair("name", page.getProductName()),
                    Pair("category", page.getCategory()),
                    Pair("ingredients", page.getIngredients())))
        } catch (e: Exception) {
            println("Deadlink for product link: $productUrl with error $e")
        }
        return JsonObject()
    }
}