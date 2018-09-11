import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class CodeCheckScraperTest {
    private lateinit var scraper: CodeCheckScraper

    @Before
    fun setUp() {
        scraper = CodeCheckScraper()
    }

    @Test
    fun shouldGetLinksFromFile() {
        assertThat(scraper.getCategoryLinksFromFile()!![0], `is`("https://www.codecheck.info/kosmetik_koerperpflege/aetherische_oele_pflanzenauszuege/aetherische_oele_mischungen.kat"))
        assertThat(scraper.getCategoryLinksFromFile()!![1], `is`("https://www.codecheck.info/kosmetik_koerperpflege/aetherische_oele_pflanzenauszuege/aetherische_oele_100_rein.kat"))
    }

    @Test
    fun shouldWriteProductWithIngredients() {
        val product: com.beust.klaxon.JsonObject = scraper.writeProductWithIngredients("https://www.codecheck.info/kosmetik_koerperpflege/koerperpflege/koerperpeelings/ean_3259552003503/id_518462/EISENBERG_Exfoliating_Body_Gel.pro")

        assertThat(product.get("name").toString(), `is`("EISENBERG - Exfoliating Body Gel"))
        assertThat(product.get("ean").toString(), `is`("3259552003503"))
        assertThat(product.get("category").toString(), `is`("Körperpeelings"))

        val ingredients: List<String> = product.get("ingredients") as List<String>
        assertThat(ingredients.size, `is`(40))
        assertThat(ingredients.get(0), `is`("AQUA (WATER)"))
        assertThat(ingredients.get(39), `is`("LACTIC ACID."))
    }

    @Test
    fun shouldWriteProductWithIngredientsWithoutIngredients() {
        val product: com.beust.klaxon.JsonObject = scraper.writeProductWithIngredients("https://www.codecheck.info/kosmetik_koerperpflege/geschenk_probesets/ean_4086900444935/id_1942479142/Kennenlern_Reiseset_Sensitivpflege.pro")

        assertThat(product.get("name").toString(), `is`("Kennenlern- & Reiseset Sensitivpflege"))
        assertThat(product.get("ean").toString(), `is`("4086900444935"))
        assertThat(product.get("category").toString(), `is`("Geschenk- & Probesets"))

        val ingredients: List<String> = product.get("ingredients") as List<String>
        assertThat(ingredients.size, `is`(0))
    }

    @Test
    fun shouldWriteProductsWithIngredientsByCategoryPage() {
        val pair = scraper.writeProductWithIngredientsByCategoryPage("https://www.codecheck.info/kosmetik_koerperpflege/koerperpflege/koerperpeelings.kat", 1)
        assertThat(pair.first, `is`(true))

        val products = pair.second.toList()
        assertThat(products.size, `is`(50))

        val product = products.get(0)
        assertThat(product.get("name").toString(), `is`(notNullValue()))
        assertThat(product.get("ean").toString(), `is`(notNullValue()))
        assertThat(product.get("category").toString(), `is`(notNullValue()))
    }

    @Test
    @Ignore
    fun shouldWriteProductsWithIngredientsByCategory()  {
        scraper.writeProductWithIngredientsByCategory("https://www.codecheck.info/kosmetik_koerperpflege/koerperpflege/koerperpeelings.kat")
    }
}
