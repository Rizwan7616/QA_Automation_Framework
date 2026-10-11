package com.rizwan.qa.api;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UsersApiTests {
    private WireMockServer mockApi;

    @BeforeClass
    public void startMockApi() {
        mockApi = new WireMockServer(0);
        mockApi.start();

        configureFor("localhost", mockApi.port());

        stubFor(get(urlEqualTo("/api/users/42"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody("{\"id\":42,\"name\":\"Amina Hassan\",\"role\":\"tester\"}")));

        stubFor(get(urlEqualTo("/api/users/999"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(404)
                .withBody("{\"error\":\"User not found\"}")));
    }

    @AfterClass(alwaysRun = true)
    public void stopMockApi() {
        if (mockApi != null) {
            mockApi.stop();
        }
    }

    @Test
    public void existingUserReturnsExpectedJson() {
        given()
            .baseUri("http://localhost")
            .port(mockApi.port())
        .when()
            .get("/api/users/42")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .body("id", equalTo(42))
            .body("name", equalTo("Amina Hassan"))
            .body("role", equalTo("tester"));
    }

    @Test
    public void unknownUserReturnsNotFound() {
        given()
            .baseUri("http://localhost")
            .port(mockApi.port())
        .when()
            .get("/api/users/999")
        .then()
            .statusCode(404)
            .body("error", equalTo("User not found"));
    }

    @Test
    public void responseIncludesContentType() {
        given()
            .baseUri("http://localhost")
            .port(mockApi.port())
        .when()
            .get("/api/users/42")
        .then()
            .statusCode(200)
            .header("Content-Type", notNullValue());
    }
}
