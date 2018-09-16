import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.jsoup.Jsoup
import org.junit.Before
import org.junit.Test
import pages.CategoryPage
import pages.ProductPage
import java.io.File

class DocumentToPageTest {

    private lateinit var converterDocumentTo: DocumentToPage

    @Before
    fun setUp() {
        converterDocumentTo = DocumentToPage()
    }

    @Test
    fun shouldFindEAN() {
        val filePath: String = DocumentToPageTest::class.java.getResource("product-with-microplastic.html").file
        val page: ProductPage = converterDocumentTo.convertToProductPage(Jsoup.parse(File(filePath), "UTF-8"), "some.url")
        assertThat(page.getEan(), `is`("3059944022392"))
    }

    @Test
    fun shouldGetProductName() {
        val filePath: String = DocumentToPageTest::class.java.getResource("product-with-microplastic.html").file
        val page: ProductPage = converterDocumentTo.convertToProductPage(Jsoup.parse(File(filePath), "UTF-8"), "some.url")
        assertThat(page.getProductName(), `is`("VEET Suprem Essence Kaltwachsstreifen"))
    }

    @Test
    fun shouldGetCategory() {
        val filePath: String = DocumentToPageTest::class.java.getResource("product-with-microplastic.html").file
        val page: ProductPage = converterDocumentTo.convertToProductPage(Jsoup.parse(File(filePath), "UTF-8"), "some.url")
        assertThat(page.getCategory(), `is`("Warm- & Kaltwachs"))
    }

    @Test
    fun shouldGetIngredients() {
        val filePath: String = DocumentToPageTest::class.java.getResource("product-with-microplastic.html").file
        val page: ProductPage? = converterDocumentTo.convertToProductPage(Jsoup.parse(File(filePath), "UTF-8"), "some.url")

        assertThat(page!!.getIngredients().size, `is`(11))
        assertThat(page.getIngredients()[0], `is`("Triethylene Glycol Rosinate"))
        assertThat(page.getIngredients()[1], `is`("Silica"))
        assertThat(page.getIngredients()[2], `is`("Polyethylene"))
        assertThat(page.getIngredients()[10], `is`("Butylphenyl"))
    }

    @Test
    fun shouldFindUrlsOnCategoryPage() {
        val filePath: String = DocumentToPageTest::class.java.getResource("category.html").file
        val page: CategoryPage? = converterDocumentTo.convertToCategoryPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page!!.getUrls()[0], `is`("https://www.codecheck.info/kosmetik_koerperpflege/rasur_enthaarung/warm_kaltwachs/id_865576968/Balea_Kaltwachsstreifen_Koerper.pro"))
        assertThat(page.getUrls()[1], `is`("https://www.codecheck.info/kosmetik_koerperpflege/rasur_enthaarung/warm_kaltwachs/ean_8718924877340/id_2040985225/Soft_Touch_Cold_Wax_Strips_Sensitive_Skin.pro"))
    }

    @Test
    fun hasNext_returnTrue_ifHasNextPage() {
        val filePath: String = DocumentToPageTest::class.java.getResource("category.html").file
        val page: CategoryPage? = converterDocumentTo.convertToCategoryPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page!!.hasNext(), `is`(true))
    }

    @Test
    fun hasNext_returnFalse_ifLastPage() {
        val filePath: String = DocumentToPageTest::class.java.getResource("category-last-page.html").file
        val page: CategoryPage? = converterDocumentTo.convertToCategoryPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page!!.hasNext(), `is`(false))
    }
}


