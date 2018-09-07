import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.File

fun main(args: Array<String>) {

    val document = Jsoup.parse(File("codecheck.html"), "UTF-8")

    val body: Element = document.body()

    println(body)
}

