package pages

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class CategoryPage(private val document: Document) {
    class SearchResultProduct(val url: String)

    private fun Element.asProduct(): SearchResultProduct {
        val relativePath = this.getElementsByAttribute("href").attr("href")
        return SearchResultProduct("https://www.codecheck.info$relativePath")
    }
    private fun Element.asProductUrl(): String {
        val relativePath = this.getElementsByAttribute("href").attr("href")
        return "https://www.codecheck.info$relativePath"
    }

    fun getProducts(): List<SearchResultProduct>? {
        return document.getElementsByClass("cell")
                .map { it.asProduct() }
    }
    fun getUrls(): List<String> {
        return document.getElementsByClass("t").map { it.asProductUrl()}
    }
    fun hasNext(): Boolean {
        return document.getElementsByClass("next").size > 0
    }
}