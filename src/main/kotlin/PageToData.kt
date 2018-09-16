import pages.ProductPage
import java.time.LocalDateTime

class PageToData {
    fun convert(page: ProductPage?): Product {
        return Product(
                page?.getUrl(),
                LocalDateTime.now(),
                page?.getEan(),
                page?.getProductName(),
                page?.getCategory(),
                page?.getIngredients())
    }
}

data class Product(
        var url: String? = "",
        var timestamp: LocalDateTime = LocalDateTime.MIN,
        var ean: String? = "",
        var name: String? = "",
        var category: String? = "",
        var ingredients: List<String>? = mutableListOf())