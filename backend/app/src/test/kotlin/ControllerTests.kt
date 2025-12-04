package ru.ekataskin.booktracker.app

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.ekataskin.booktracker.api.v1.models.BookCreateObject
import ru.ekataskin.booktracker.api.v1.models.BookCreateRequest

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTests {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `create book returns response`() {
        val request = BookCreateRequest(
            book = BookCreateObject(
                author = "Александр Пушкин",
                title = "Евгений Онегин",
                url = "https://ru.wikipedia.org/wiki/Евгений_Онегин",
            )
        )

        val mvcResult = mockMvc.perform(
            post("/api/v1/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andReturn()

        // дожидаемся завершения async и получаем финальный ответ
        val finalResult = mockMvc.perform(MockMvcRequestBuilders.asyncDispatch(mvcResult))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result").value("success"))
            .andReturn()
    }
}