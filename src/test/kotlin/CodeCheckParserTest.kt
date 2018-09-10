import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.jsoup.Jsoup
import org.junit.Before
import org.junit.Test
import java.io.File

class CodeCheckParserTest {

    private lateinit var parser: CodeCheckParser

    @Before
    fun setUp() {
        parser = CodeCheckParser()
    }

    @Test
    fun shouldFindEAN() {
        val filePath: String = CodeCheckParserTest::class.java.getResource("product-with-microplastic.html").file
        val page: ProductPage = parser.parseProductPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page.getEan(), `is`("3059944022392"))
    }

    @Test
    fun shouldCheckForMicroplastics() {
        val filePath: String = CodeCheckParserTest::class.java.getResource("product-with-microplastic.html").file
        val page: ProductPage = parser.parseProductPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page.hasMicroplastic(), `is`(true))
    }

    @Test
    fun shouldGetMicroplastics() {
        val filePath: String = CodeCheckParserTest::class.java.getResource("product-with-microplastic.html").file
        val page: ProductPage = parser.parseProductPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page.hasMicroplastic(), `is`(true))
        assertThat(page.getMicroplastic(), `is`("Polyethylene"))
    }

    @Test
    fun shouldGetProductName() {
        val filePath: String = CodeCheckParserTest::class.java.getResource("product-with-microplastic.html").file
        val page: ProductPage = parser.parseProductPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page.getProductName(), `is`("VEET Suprem Essence Kaltwachsstreifen"))
    }

    @Test
    fun shouldGetCategory() {
        val filePath: String = CodeCheckParserTest::class.java.getResource("product-with-microplastic.html").file
        val page: ProductPage = parser.parseProductPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page.getCategory(), `is`("Warm- & Kaltwachs"))
    }

    @Test
    fun shouldGetIngredients() {
        val filePath: String = CodeCheckParserTest::class.java.getResource("product-with-microplastic.html").file
        val page: ProductPage = parser.parseProductPage(Jsoup.parse(File(filePath), "UTF-8"))

        assertThat(page.getIngredients().size, `is`(11))
        assertThat(page.getIngredients().get(0), `is`("Triethylene Glycol Rosinate"))
        assertThat(page.getIngredients().get(1), `is`("Silica"))
        assertThat(page.getIngredients().get(2), `is`("Polyethylene"))
        assertThat(page.getIngredients().get(10), `is`("Butylphenyl"))
    }

    @Test
    fun shouldFindProductUrlsOnSearchResultsPage() {
        val filePath: String = CodeCheckParserTest::class.java.getResource("searchResults.html").file
        val page: SearchResultsPage = parser.parseSearchResultsPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page.getProducts()!![0].url, `is`("https://www.codecheck.info/kosmetik_koerperpflege/gesichtspflege/gesichtsmasken/id_2096663156/Tony_Moly_Timeless_Ferment_Snail_Gel_Mask.pro"))
        assertThat(page.getProducts()!![1].url, `is`("https://www.codecheck.info/kosmetik/gesichtspflege/gesichtscremen/ean_20714806323/id_34370165/Clinique_Pep_Start_Hydroblur_Moisturizer_Gesichtscreme_50_ml.pro"))
    }

    @Test
    fun shouldFindUrlsOnCategoryPage() {
        val filePath: String = CodeCheckParserTest::class.java.getResource("category.html").file
        val page: CategoryPage = parser.parseCategoryPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page.getUrls()[0], `is`("https://www.codecheck.info/kosmetik_koerperpflege/rasur_enthaarung/warm_kaltwachs/id_865576968/Balea_Kaltwachsstreifen_Koerper.pro"))
        assertThat(page.getUrls()[1], `is`("https://www.codecheck.info/kosmetik_koerperpflege/rasur_enthaarung/warm_kaltwachs/ean_8718924877340/id_2040985225/Soft_Touch_Cold_Wax_Strips_Sensitive_Skin.pro"))
    }

    @Test
    fun shouldFindProductsOnCategoryPage() {
        val filePath: String = CodeCheckParserTest::class.java.getResource("category.html").file
        val page: CategoryPage = parser.parseCategoryPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page.getProducts()!![0].url, `is`("https://www.codecheck.info/kosmetik_koerperpflege/rasur_enthaarung/warm_kaltwachs/id_865576968/Balea_Kaltwachsstreifen_Koerper.pro"))
        assertThat(page.getProducts()!![1].url, `is`("https://www.codecheck.info/kosmetik_koerperpflege/rasur_enthaarung/warm_kaltwachs/ean_8718924877340/id_2040985225/Soft_Touch_Cold_Wax_Strips_Sensitive_Skin.pro"))
    }

    @Test
    fun hasNext_returnTrue_ifHasNextPage() {
        val filePath: String = CodeCheckParserTest::class.java.getResource("category.html").file
        val page: CategoryPage = parser.parseCategoryPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page.hasNext(), `is`(true))
    }

    @Test
    fun hasNext_returnFalse_ifLastPage() {
        val filePath: String = CodeCheckParserTest::class.java.getResource("category-last-page.html").file
        val page: CategoryPage = parser.parseCategoryPage(Jsoup.parse(File(filePath), "UTF-8"))
        assertThat(page.hasNext(), `is`(false))
    }
}


