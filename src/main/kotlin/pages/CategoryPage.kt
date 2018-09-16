package pages

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class CategoryPage(private val document: Document?) {
    private fun Element.asProductUrl(): String {
        val relativePath = this.getElementsByAttribute("href").attr("href")
        return "https://www.codecheck.info$relativePath"
    }

    fun getUrls(): List<String> {
        return document?.getElementsByClass("t")
                .orEmpty()
                .map { it.asProductUrl() }
    }

    fun hasNext(): Boolean {
        return document?.getElementsByClass("next")
                .orEmpty().isNotEmpty()
    }
}