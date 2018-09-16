import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class DocumentFetcher {

    fun get(url: String): Document? {
        return try {
            Jsoup.connect(url).get()
        } catch (e: Exception) {
            println("Could not get document for: $url with error $e")
            null
        }
    }
}