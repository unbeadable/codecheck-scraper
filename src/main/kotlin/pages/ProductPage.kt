package pages

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class ProductPage(private val document: Document) {
    fun getEan(): String? {
        val find: Element? = document.getElementsByClass("product-info-item")
                .find { it.getElementsByClass("product-info-label").text() == "Strichcode-Nummer" }
        return find?.child(1)?.text()
    }
    fun hasMicroplastic(): Boolean {
        return document.getElementsByClass("c-2")
                .any { it -> it.text() == "Enthält Mikroplastik" }
    }
    fun getMicroplastic(): String {
        val find: Element? = document.getElementsByClass("rating-group-header")
                .find { it -> it.getElementsByClass("c-2").text() == "Enthält Mikroplastik" }

        return find?.getElementsByClass("c-3")?.first()?.text().orEmpty()
    }
    fun getProductName(): String {
        return document.getElementsByClass("page-title-headline").first().getElementsByTag("h1").first().text().orEmpty()
    }
    fun getCategory(): String? {
        return document.getElementsByClass("product-info-item").first().getElementsByTag("a").first().text().orEmpty()
    }
    fun getIngredients(): List<String> {
        val elementsByClass = document.getElementsByClass("rated-ingredients").orEmpty()
        return (if (elementsByClass.isEmpty()) "" else elementsByClass[0].text())
                .split(",")
                .map { it.trim() }
                .filter { it != "" }
    }
}