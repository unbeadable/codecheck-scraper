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
    fun hasMicroplastic(): Boolean? {
        return document.getElementsByClass("c-2")
                .any { it -> it.text().equals("Enthält Mikroplastik") }
    }
}

class SearchResultsPage(private val document: Document) {
    fun getProducts(): List<Product>? {
        return document.getElementsByClass("search-result")
                .filter { it.isProduct() }
                .map { it.asProduct() }
    }
}

class CategoryPage(private val document: Document) {
    fun getProducts(): List<Product>? {
        return document.getElementsByClass("cell")
                .map { it.asProduct() }
    }
}



private fun Element.asProduct(): Product {
    val relativePath = this.getElementsByAttribute("href").attr("href")
    return Product("https://www.codecheck.info$relativePath")
}

private fun Element.isProduct(): Boolean {
    return this.getElementsByClass("middle").first().getElementsByClass("lower").size == 1
}

class Product(val url: String)