import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class DataCleaningTest {

    @Test
    fun shouldRemoveProductsWithoutIngredients() {
        val unprocessed = listOf(
                Product("foo.bar", LocalDateTime.MIN, "1234567890123", "", "", emptyList()),
                Product("foo.baz", LocalDateTime.MIN, "1234567890123", "", "", listOf("foo", "bar")))
        val withIngredients = unprocessed.clean()

        assertThat(withIngredients.size).isEqualTo(1)
        assertThat(withIngredients[0].url).isEqualTo("foo.baz")
        assertThat(withIngredients[0].ingredients!!.toList()).isEqualTo(listOf("foo", "bar"))
    }

    @Test
    fun shouldRemoveProductsWithInvalidEans() {
        val unprocessed = listOf(
                Product("foo1.bar", LocalDateTime.MIN, "", "", "", listOf("foo", "bar")),
                Product("foo2.bar", LocalDateTime.MIN, "123", "", "", listOf("foo", "bar")),
                Product("foo3.bar", LocalDateTime.MIN, "abc", "", "", listOf("foo", "bar")),
                Product("foo4.bar", LocalDateTime.MIN, "1234567890123", "", "", listOf("foo", "bar")))
        val validEans = unprocessed.clean()

        assertThat(validEans.size).isEqualTo(1)
        assertThat(validEans[0].url).isEqualTo("foo4.bar")
    }
}