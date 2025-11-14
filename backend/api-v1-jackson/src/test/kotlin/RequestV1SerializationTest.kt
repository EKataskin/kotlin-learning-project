package ru.ekataskin.booktracker.api.v1

import ru.ekataskin.booktracker.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request = BookCreateRequest(
        mode = RequestDebugMode.STUB,
        stub = RequestDebugStubs.BAD_TITLE,
        book = BookCreateObject(
            author = "author name",
            title = "book title",
            year = 1999,
            bookState = BookState.PLANNED
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"author\":\\s*\"author name\""))
        assertContains(json, Regex("\"title\":\\s*\"book title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as BookCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"book": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, BookCreateRequest::class.java)

        assertEquals(null, obj.book)
    }
}
