import org.jsoup.nodes.Document
import pages.CategoryPage
import pages.ProductPage

class CodeCheckParser {
    fun parseProductPage(document: Document): ProductPage {
        return ProductPage(document)
    }

    fun parseCategoryPage(document: Document): CategoryPage {
        return CategoryPage(document)
    }
}