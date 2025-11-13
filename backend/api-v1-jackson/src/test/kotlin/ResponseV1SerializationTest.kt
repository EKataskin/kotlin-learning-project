package ru.otus.otuskotlin.marketplace.api.v1

import ru.ekataskin.booktracker.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response = BookCreateResponse(
        ad = BookResponseObject(
            author = "author name",
            title = "book title",
            year = 1999,
            bookState = BookState.PLANNED
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"author\":\\s*\"author name\""))
        assertContains(json, Regex("\"title\":\\s*\"book title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as BookCreateResponse

        assertEquals(response, obj)
    }
}
