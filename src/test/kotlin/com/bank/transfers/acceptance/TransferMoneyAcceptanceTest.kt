package com.bank.transfers.acceptance

import com.bank.transfers.infrastructure.config.module
import io.ktor.application.Application
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TransferMoneyAcceptanceTest {

    private lateinit var server: ApplicationEngine

    private val appPort = 8080

    @BeforeEach
    fun `set up`() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = 8080
        embeddedServer(
            factory = Netty,
            port = appPort,
            module = Application::module
        ).start()
        Runtime.getRuntime().addShutdownHook(Thread { server.stop(0, 0) })
    }

    @Test
    fun `should move money from one customer to another`() {

//        RestAssured
//            .given()
//            .contentType(ContentType.JSON)
//            .body(
//                """
//					{
//						"amount": 10.00,
//                        "from": "fd1096fb-a7c5-46de-8433-e1b8c3db1ed5",
//                        "to": "b6b473da-2d0f-41c9-ac4a-89a08a05ab36"
//					}
//				"""
//            )
//            .`when`()
//            .port(appPort)
//            .post("/transfers")
//            .then()
//            .assertThat()
//            .statusCode(201)
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
