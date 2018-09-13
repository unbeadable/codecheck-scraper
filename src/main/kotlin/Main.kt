import com.beust.klaxon.Klaxon
import com.google.gson.Gson
import java.io.File
import java.io.InputStream
import java.time.LocalDateTime

fun main(args: Array<String>) {
    data class Link(val category: String, val url: String)
    data class CategoryRunbook(
            var name: String = "",
            var url: String = "",
            var start: LocalDateTime = LocalDateTime.MIN,
            var end: LocalDateTime = LocalDateTime.MIN,
            var linksVisited: Int = -1,
            var deadLinks: Int = -1)
    data class Runbook(
            var start: LocalDateTime = LocalDateTime.MIN,
            var end: LocalDateTime = LocalDateTime.MIN,
            var linksVisited: Int = -1,
            var deadLinks: Int = -1,
            var catgeories: MutableList<CategoryRunbook> = emptyList<CategoryRunbook>().toMutableList())

    val scraper = CodeCheckScraper()
    var linksVisited = 0
    var deadlinks = 0

    val runbook = Runbook()
    runbook.start = LocalDateTime.now()

    val stream: InputStream = CodeCheckScraper::class.java.getResource("codecheckLinks.json").openStream()
    Klaxon().parseArray<Link>(stream)?.forEach {
        val url = it.url
        val category = it.category

        val categoryRunbook = CategoryRunbook()
        categoryRunbook.name = category
        categoryRunbook.url = url
        categoryRunbook.start = LocalDateTime.now()

        val result: Result = scraper.writeProductWithIngredientsByCategory(url) as Result
        File("results/codecheck/$category.json").writeText(Gson().toJson(result.products))

        categoryRunbook.linksVisited = result.linksVisited
        categoryRunbook.deadLinks = result.deadlinks
        categoryRunbook.end = LocalDateTime.now()
        runbook.catgeories.add(categoryRunbook)

        linksVisited += result.linksVisited
        deadlinks += result.deadlinks
    }

    runbook.linksVisited = linksVisited
    runbook.deadLinks = deadlinks
    runbook.end = LocalDateTime.now()

    File("results/codecheck/runbook.json").writeText(Klaxon().toJsonString(runbook))
}