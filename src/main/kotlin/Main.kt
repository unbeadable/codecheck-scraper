import com.beust.klaxon.Klaxon
import java.io.InputStream

fun main(args: Array<String>) {
    val scraper = CodeCheckScraper()

    val stream: InputStream = CodeCheckScraper::class.java.getResource("codecheckLinks.json").openStream()
    Klaxon().parseArray<Link>(stream)?.forEach {
        val url = it.url
        println("visiting category: $url")

        scraper.writeProductWithIngredientsByCategory(url, "results/${it.category}.json")
    }
}

class Link(val category: String, val url: String)

