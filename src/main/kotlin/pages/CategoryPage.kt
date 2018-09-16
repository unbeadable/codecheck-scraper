package pages

import org.jsoup.nodes.Document

class CategoryPage(private val document: Document?) {

    val productUrls: List<String> get() {
        return document?.getElementsByClass("t")
                .orEmpty()
                .map {
                    val relativePath = it.getElementsByAttribute("href").attr("href")
                    "https://www.codecheck.info$relativePath"
                }
    }

    fun hasNext(): Boolean {
        return document?.getElementsByClass("next")
                .orEmpty().isNotEmpty()
    }
}