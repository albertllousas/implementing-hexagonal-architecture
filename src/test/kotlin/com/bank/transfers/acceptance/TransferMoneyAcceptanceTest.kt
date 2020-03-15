package com.bank.transfers.acceptance

import com.bank.transfers.infrastructure.config.module
import io.ktor.application.Application
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TransferMoneyAcceptanceTest {

    private lateinit var server: ApplicationEngine

    private val appPort = 8080

    @BeforeEach
    fun `set up`() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = appPort
        server = embeddedServer(
            factory = Netty,
            port = appPort,
            module = Application::module
        ).start()
    }

    @AfterEach
    fun `tear down`() {
        server.stop(500, 500)
    }

    @Test
    fun `should`() {

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(
                """
					{ 
						"amount": 10.00, 
                        "from": "fd1096fb-a7c5-46de-8433-e1b8c3db1ed5", 
                        "to": "b6b473da-2d0f-41c9-ac4a-89a08a05ab36" 
					}
				"""
            )
            .`when`()
            .port(appPort)
            .post("/transfers")
            .then()
            .assertThat()
            .statusCode(201)
//            .extract()
//            .response()
//            .also {
//                assertThatJson(it.body.asString()).isEqualTo(
//                    """
//					{
//					  "total": 24.00
//					}
//					"""
//                )
//            }

    }

}
