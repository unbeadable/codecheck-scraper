import org.assertj.core.api.Assertions.assertThat
import org.jsoup.Jsoup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pages.CategoryPage
import pages.ProductPage
import java.io.File


class DocumentToPageTest {

    private lateinit var converterDocumentTo: DocumentToPage

    @BeforeEach
    fun setUp() {
        converterDocumentTo = DocumentToPage()
    }

    @Test
    fun shouldFindEAN() {
        val filePath: String = DocumentToPageTest::class.java.getResource("product-with-microplastic.html").file
        val page: ProductPage = converterDocumentTo.convertToProductPage(Jsoup.parse(File(filePath), "UTF-8"), "some.url")
        assertThat(page.getEan()).isEqualTo("3059944022392")
    }

    @Test
    fun shouldGetCodeCheckProductId() {
            val filePath: String = DocumentToPageTest::class.java.getResource("product-with-microplastic.html").file
            val page: ProductPage? = converterDocumentTo.convertToProductPage(Jsoup.parse(File(filePath), "UTF-8"), "some.url")
            assertThat(page!!.getCodeCheckProductId()).isEqualTo("511195")
        }

    @Test
    fun shouldGetProductName() {
        val filePath: String = DocumentToPageTest::class.java.getResource("product-with-microplastic.html").file
        val page: ProductPage = converterDocumentTo.convertToProductPage(Jsoup.parse(File(filePath), "UTF-8"), "some.url")
        assertThat(page.getProductName()).isEqualTo("VEET Suprem Essence Kaltwachsstreifen")
    }

    @Test
    fun shouldGetCategory() {
        val filePath: String = DocumentToPageTest::class.java.getResource("product-with-microplastic.html").file
        val page: ProductPage = converterDocumentTo.convertToProductPage(Jsoup.parse(File(filePath), "UTF-8"), "some.url")
        assertThat(page.getCategory()).isEqualTo("Warm- & Kaltwachs")
    }

    @Test
    fun shouldGetIngredients() {
        val filePath: String = DocumentToPageTest::class.java.getResource("product-with-microplastic.html").file
        val page: ProductPage = converterDocumentTo.convertToProductPage(Jsoup.parse(File(filePath), "UTF-8"), "some.url")

        assertThat(page.getIngredients().size).isEqualTo(11)
        assertThat(page.getIngredients()[0]).isEqualTo("Triethylene Glycol Rosinate")
        assertThat(page.getIngredients()[1]).isEqualTo("Silica")
        assertThat(page.getIngredients()[2]).isEqualTo("Polyethylene")
        assertThat(page.getIngredients()[10]).isEqualTo("Butylphenyl")
    }

    @Test
    fun shouldFindUrlsOnCategoryPage() {
        val filePath: String = DocumentToPageTest::class.java.getResource("category.html").file
        val page: CategoryPage = converterDocumentTo.convertToCategoryPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page.productUrls[0]).isEqualTo("https://www.codecheck.info/kosmetik_koerperpflege/rasur_enthaarung/warm_kaltwachs/id_865576968/Balea_Kaltwachsstreifen_Koerper.pro")
        assertThat(page.productUrls[1]).isEqualTo("https://www.codecheck.info/kosmetik_koerperpflege/rasur_enthaarung/warm_kaltwachs/ean_8718924877340/id_2040985225/Soft_Touch_Cold_Wax_Strips_Sensitive_Skin.pro")
    }

    @Test
    fun hasNext_returnTrue_ifHasNextPage() {
        val filePath: String = DocumentToPageTest::class.java.getResource("category.html").file
        val page: CategoryPage = converterDocumentTo.convertToCategoryPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page.hasNext()).isEqualTo(true)
    }

    @Test
    fun hasNext_returnFalse_ifLastPage() {
        val filePath: String = DocumentToPageTest::class.java.getResource("category-last-page.html").file
        val page: CategoryPage = converterDocumentTo.convertToCategoryPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page.hasNext()).isEqualTo(false)
    }
}


