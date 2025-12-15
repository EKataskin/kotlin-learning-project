import com.jayway.jsonpath.JsonPath
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.verify
import org.mockito.kotlin.argThat
import org.mockito.kotlin.mock
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import ru.ekataskin.booktracker.api.v1.apiV1Mapper
import ru.ekataskin.booktracker.api.v1.models.BookCreateRequest
import ru.ekataskin.booktracker.app.kafka.Listener
import ru.ekataskin.booktracker.common.RequestV1Processor
import ru.ekataskin.booktracker.common.interfaces.IRequestProcessor
import ru.ekataskin.booktracker.domain.Domain
import ru.ekataskin.booktracker.stubs.Stubs
import java.util.concurrent.CompletableFuture

class KafkaTests {
    private val processor: IRequestProcessor = RequestV1Processor(domain = Domain())

    private val kafkaTemplate: KafkaTemplate<String, String> = mock()
    private val responseTopic = "test-output"

    private val listener = Listener(
        processor = processor,
        template = kafkaTemplate,
        responseTopic = responseTopic
    )

    @Test
    fun `listener processes BookCreateRequest and sends response`() {
        // arrange
        val request = BookCreateRequest(book = Stubs.BOOK_CREATE_OBJECT1)
        val requestJson = apiV1Mapper.writeValueAsString(request)

        val future: CompletableFuture<SendResult<String, String>> = CompletableFuture.completedFuture(null)
        org.mockito.kotlin.whenever(kafkaTemplate.send(anyString(), anyString()))
            .thenReturn(future)

        // act
        listener.listen(ConsumerRecord("test-input", 0, 0L, "key-1", requestJson))

        // assert
        verify(kafkaTemplate).send(
            org.mockito.kotlin.eq(responseTopic),
            argThat { json: String ->
                JsonPath.read<String>(json, "\$.result") == "success"
            }
        )
    }
}