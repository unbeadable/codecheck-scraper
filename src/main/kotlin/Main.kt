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
    val scraper = DataScraper()

    links.filterNot(Link::processed).forEach {
        val start = LocalDateTime.now()

        val scrapingResult: ScrapingResult = scraper.getAllProductsBy(it.url)
        File("results/data/${it.category}.json").writeText(Klaxon().toJsonString(scrapingResult.products))

        val cleanedProducts = scrapingResult.products.clean()
        File("results/data/cleaned-${it.category}.json").writeText(Klaxon().toJsonString(cleanedProducts))

        val invalidData = scrapingResult.visitedLinks - scrapingResult.brokenLinks - cleanedProducts.size
        val logBook = LogBook(it.category, it.url, start, LocalDateTime.now(), scrapingResult.visitedLinks, scrapingResult.brokenLinks, invalidData)
        File("results/data/logbook-${it.category}.json").writeText(Klaxon().toJsonString(logBook))
    }
}

fun List<Product>.clean(): List<Product> {
    return this
            .asSequence()
            .filter { it.ingredients.orEmpty().toList().isNotEmpty() }
            .filter { it.hasValidEan() }
            .toList()
}

private fun Product.hasValidEan(): Boolean {
    val thirteenDigits = "^(\\d{13})\$"
    return this.ean.orEmpty().matches(Regex(thirteenDigits))
}