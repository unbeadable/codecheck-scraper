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
            var linksVisited: Int = -1,
            var deadLinks: Int = -1)

    val scraper = DataScraper()

    val stream: InputStream = DataScraper::class.java.getResource("categoryLinks.json").openStream()
    Klaxon().parseArray<Link>(stream)?.filter { it -> it.processed.not() }?.forEach {
        val url = it.url
        val category = it.category
        val start = LocalDateTime.now()

        val result: Result = scraper.getAllProductsBy(categoryUrl = url)
        File("results/data/$category.json").writeText(Klaxon().toJsonString(result.products))

        val runbook = Runbook(category, url, start, LocalDateTime.now(), result.visitedLinks, result.invalidLinks)
        File("results/data/runbook-$category.json").writeText(Klaxon().toJsonString(runbook))
    }
}