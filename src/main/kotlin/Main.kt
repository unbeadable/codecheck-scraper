import com.beust.klaxon.Klaxon
import java.io.File
import java.io.InputStream
import java.time.LocalDateTime

fun main(args: Array<String>) {
    data class Link(val category: String, val url: String, val processed: Boolean)
    data class Runbook(
            var name: String = "",
            var url: String = "",
            var start: LocalDateTime = LocalDateTime.MIN,
            var end: LocalDateTime = LocalDateTime.MIN,
            var visitedLinks: Int = -1,
            var brokenLinks: Int = -1,
            var invalidData: Int = -1)

    val stream: InputStream = DataScraper::class.java.getResource("categoryLinks.json").openStream()
    val links = Klaxon().parseArray<Link>(stream) ?: throw RuntimeException("Could not parse json from file.")
    links.filterNot(Link::processed).forEach {
        val start = LocalDateTime.now()

        val result: Result = DataScraper().getAllProductsBy(categoryUrl = it.url)
        File("results/data/${it.category}.json").writeText(Klaxon().toJsonString(result.products))

        val cleanedProducts = DataCleaner().clean(result.products)
        File("results/data/cleaned-${it.category}.json").writeText(Klaxon().toJsonString(cleanedProducts))

        val invalidData = result.visitedLinks - result.brokenLinks - cleanedProducts.size
        val runbook = Runbook(it.category, it.url, start, LocalDateTime.now(), result.visitedLinks, result.brokenLinks, invalidData)
        File("results/data/runbook-${it.category}.json").writeText(Klaxon().toJsonString(runbook))
    }
}