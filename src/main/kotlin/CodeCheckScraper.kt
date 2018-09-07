import com.beust.klaxon.Klaxon
import java.io.InputStream


class CodeCheckScraper {
    fun getLinksFromFile(): List<String>? {
        val stream: InputStream = CodeCheckScraper::class.java.getResource("codecheckLinks.json").openStream()
        return Klaxon().parseArray<String>(stream)
    }
}
