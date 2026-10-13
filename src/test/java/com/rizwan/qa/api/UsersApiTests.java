package com.rizwan.qa.api;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/** Contract-style checks against a local WireMock API; no third-party service is called. */
public class UsersApiTests {
    private WireMockServer mockApi;

    @BeforeClass
    public void startMockApi() {
        mockApi = new WireMockServer(0);
        mockApi.start();

        mockApi.stubFor(get(urlEqualTo("/api/users/42"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody("{\"id\":42,\"name\":\"Amina Hassan\",\"role\":\"tester\"}")));

        mockApi.stubFor(get(urlEqualTo("/api/users/999"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(404)
                .withBody("{\"error\":\"User not found\"}")));

        // Exact known-user stubs take precedence; other numeric IDs behave like missing users.
        mockApi.stubFor(get(urlPathMatching("/api/users/[0-9]+"))
            .atPriority(10)
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
    public void existingUserReturnsHttp200() {
        request("/api/users/42").then().statusCode(200);
    }

    @Test
    public void existingUserReturnsJsonContentType() {
        request("/api/users/42").then().contentType("application/json");
    }

    @Test
    public void existingUserIdIsCorrectJsonNumber() {
        request("/api/users/42").then().body("id", equalTo(42));
    }

    @Test
    public void existingUserNameMatchesContract() {
        request("/api/users/42").then().body("name", equalTo("Amina Hassan"));
    }

    @Test
    public void existingUserRoleMatchesContract() {
        request("/api/users/42").then().body("role", equalTo("tester"));
    }

    @Test
    public void existingUserContainsAllExpectedFields() {
        request("/api/users/42").then()
            .body("$", hasKey("id"))
            .body("$", hasKey("name"))
            .body("$", hasKey("role"));
    }

    @Test
    public void unknownUserReturnsHttp404() {
        request("/api/users/999").then().statusCode(404);
    }

    @Test
    public void unknownUserReturnsJsonErrorMessage() {
        request("/api/users/999").then().body("error", equalTo("User not found"));
    }

    @Test
    public void unknownUserErrorResponseIsJson() {
        request("/api/users/999").then().contentType("application/json");
    }

    @Test
    public void unknownUserErrorDoesNotContainUserFields() {
        request("/api/users/999").then()
            .body("$", hasKey("error"))
            .body("$", not(hasKey("id")))
            .body("$", not(hasKey("name")))
            .body("$", not(hasKey("role")));
    }

    @Test
    public void differentUnknownNumericIdReturnsNotFound() {
        request("/api/users/123").then()
            .statusCode(404)
            .body("error", equalTo("User not found"));
    }

    @Test
    public void unknownRouteReturnsNotFound() {
        request("/api/unknown").then().statusCode(404);
    }

    @Test
    public void postToGetOnlyEndpointReturnsNotFound() {
        given().baseUri("http://localhost").port(mockApi.port())
            .when().post("/api/users/42")
            .then().statusCode(404);
    }

    @Test
    public void putToGetOnlyEndpointReturnsNotFound() {
        given().baseUri("http://localhost").port(mockApi.port())
            .when().put("/api/users/42")
            .then().statusCode(404);
    }

    @Test
    public void deleteToGetOnlyEndpointReturnsNotFound() {
        given().baseUri("http://localhost").port(mockApi.port())
            .when().delete("/api/users/42")
            .then().statusCode(404);
    }

    @Test
    public void userLookupWithTrailingSlashDoesNotMatchExactRoute() {
        request("/api/users/42/").then().statusCode(404);
    }

    private Response request(String path) {
        return given()
            .baseUri("http://localhost")
            .port(mockApi.port())
        .when()
            .get(path);
    }
}
