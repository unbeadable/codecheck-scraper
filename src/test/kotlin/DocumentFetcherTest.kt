import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import org.assertj.core.api.Assertions.assertThat
import org.jsoup.nodes.Document
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DocumentFetcherTest {

    private lateinit var fetcher: DocumentFetcher
    private lateinit var server: WireMockServer

    @BeforeEach
    fun setUp() {
        server = WireMockServer(options().port(8089))
        fetcher = DocumentFetcher()
        server.start()
        WireMock.configureFor("localhost", server.port())
    }

    @AfterEach
    fun tearDown() {
        server.stop()
    }

    @Test
    fun shouldReturnDocumentForValidResponse() {
        stubFor(get(urlEqualTo("/valid"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("Hello world!")))

        val result: Document = fetcher.get("http://localhost:8089/valid")!!
        val html = result.body().html()
        assertThat(html).isEqualTo("Hello world!")
    }

    @Test
    fun shouldReturnNullOnErrorResponse() {
        stubFor(get(urlEqualTo("/invalid"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "text/plain")))

        val result: Document? = fetcher.get("http://localhost:8089/invalid")
        assertThat(result).isNull()
    }
}