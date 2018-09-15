import com.beust.klaxon.Klaxon
import java.io.File
import java.io.InputStream
import java.time.LocalDateTime

data class Link(val category: String, val url: String, val processed: Boolean)

data class LogBook(
        val name: String,
        val url: String,
        val start: LocalDateTime,
        val end: LocalDateTime,
        val visitedLinks: Int,
        val brokenLinks: Int,
        val invalidData: Int)

fun main(args: Array<String>) {
    val stream: InputStream = DataScraper::class.java.getResource("categoryLinks.json").openStream()
    val links = Klaxon().parseArray<Link>(stream) ?: throw RuntimeException("Could not parse json from file.")
    links.filterNot(Link::processed).forEach {
        val start = LocalDateTime.now()

        val result: Result = DataScraper().getAllProductsBy(categoryUrl = it.url)
        File("results/data/${it.category}.json").writeText(Klaxon().toJsonString(result.products))

        val cleanedProducts = DataCleaner().clean(result.products)
        File("results/data/cleaned-${it.category}.json").writeText(Klaxon().toJsonString(cleanedProducts))

        val invalidData = result.visitedLinks - result.brokenLinks - cleanedProducts.size
        val logBook = LogBook(it.category, it.url, start, LocalDateTime.now(), result.visitedLinks, result.brokenLinks, invalidData)
        File("results/data/logbook-${it.category}.json").writeText(Klaxon().toJsonString(logBook))
    }
}