import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.File

fun getCodeCheckPath(nameOfMicroPlastic: String, pageNumber: Int):String {
    return "https://www.codecheck.info/product.search?q=$nameOfMicroPlastic&p=$pageNumber"
}

fun main(args: Array<String>) {
    val document = Jsoup.parse(File("codecheck.html"), "UTF-8")

    val body: Element = document.body()

    println(body)
}

