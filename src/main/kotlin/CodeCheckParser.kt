import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class CodeCheckParser {
    fun parseProductPage(document: Document): ProductPage {
        return ProductPage(document)
    }

    fun parseSearchResultsPage(document: Document): SearchResultsPage {
        return SearchResultsPage(document)
    }

    fun parseCategoryPage(document: Document): CategoryPage {
        return CategoryPage(document)
    }
}

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
    fun getMicroplastic(): String? {
        val find: Element? = document.getElementsByClass("rating-group-header")
                .find { it -> it.getElementsByClass("c-2").text() == "Enthält Mikroplastik" }

        return find?.getElementsByClass("c-3")?.first()?.text()
    }
    fun getProductName(): String {
        return document.getElementsByClass("page-title-headline").first().getElementsByTag("h1").first().text()
    }
    fun getCategory(): String? {
        return document.getElementsByClass("product-info-item").first().getElementsByTag("a").first().text()
    }
    fun getIngredients(): List<String> {
        return document.getElementsByClass("rated-ingredients").get(0).text()
                .split(",")
                .map { it.trim() }
                .filter { it != "" }
    }
}

class SearchResultsPage(private val document: Document) {
    fun getProducts(): List<SearchResultProduct>? {
        return document.getElementsByClass("search-result")
                .filter { it.isProduct() }
                .map { it.asProduct() }
    }
}

private fun Element.asProductUrl(): String {
    val relativePath = this.getElementsByAttribute("href").attr("href")
    return "https://www.codecheck.info$relativePath"
}

class CategoryPage(private val document: Document) {
    fun getProducts(): List<SearchResultProduct>? {
        return document.getElementsByClass("cell")
                .map { it.asProduct() }
    }
    fun getUrls(): List<String> {
        return document.getElementsByClass("cell")
                .map { it.asProductUrl()}
    }

    fun hasNext(): Boolean {
        return document.getElementsByClass("next").size > 0
    }
}

private fun Element.asProduct(): SearchResultProduct {
    val relativePath = this.getElementsByAttribute("href").attr("href")
    return SearchResultProduct("https://www.codecheck.info$relativePath")
}

private fun Element.isProduct(): Boolean {
    return this.getElementsByClass("middle").first().getElementsByClass("lower").size == 1
}

class SearchResultProduct(val url: String)