import org.jsoup.nodes.Document
import pages.CategoryPage
import pages.ProductPage

class DocumentToPage {
    fun convertToProductPage(document: Document?, url: String): ProductPage {
        return ProductPage(document, url)
    }

    fun convertToCategoryPage(document: Document?): CategoryPage {
        return CategoryPage(document)
    }
}

