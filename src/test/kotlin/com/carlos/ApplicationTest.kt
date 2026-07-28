package com.carlos

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationTest {

    @Test
    fun `landing page loads successfully`() = testApplication {
        application {
            module()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertTrue(bodyAsText().contains("Proyectos"))
        }
    }

    @Test
    fun `projects api returns json list`() = testApplication {
        application {
            module()
        }
        client.get("/api/projects").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(ContentType.Application.Json, contentType()?.withoutParameters())
        }
    }

}
