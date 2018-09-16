import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class DocumentToPage {
    fun convertToProductPage(document: Document?, url: String): ProductPage {
        return ProductPage(document, url)
    }

    fun convertToCategoryPage(document: Document?): CategoryPage {
        return CategoryPage(document)
    }
}

class ProductPage(private val document: Document?, private val url: String?) {
    fun getEan(): String {
        val find: Element? = document?.getElementsByClass("product-info-item")
                .orEmpty()
                .find { it.getElementsByClass("product-info-label").text() == "Strichcode-Nummer" }
        return find?.child(1)?.text().orEmpty()
    }

    fun getProductName(): String {
        return document?.getElementsByClass("page-title-headline")
                .orEmpty()
                .first().getElementsByTag("h1").first().text().orEmpty()
    }

    fun getCategory(): String {
        return document?.getElementsByClass("product-info-item")
                .orEmpty()
                .first().getElementsByTag("a").first().text().orEmpty()
    }

    fun getIngredients(): List<String> {
        val elementsByClass = document?.getElementsByClass("rated-ingredients").orEmpty()
        return (if (elementsByClass.isEmpty()) "" else elementsByClass[0].text())
                .split(",")
                .asSequence()
                .map { it.trim() }
                .filter { it != "" }
                .toList()
    }

    fun getUrl(): String {
        return url.orEmpty()
    }
}

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