package ru.se.info.tinder.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.se.info.tinder.dto.RequestLocationDto;

import java.io.IOException;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EnableConfigurationProperties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {WireMockConfig.class, AuthServiceMock.class})
public class LocationControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private WireMockServer mockAuthService;

    private final String validToken = "Valid token";

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("tinder")
            .withUsername("postgres")
            .withPassword("root");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://${postgres.host}:${postgres.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT)}/${postgres.databaseName}");
        registry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        registry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
    }

    @BeforeAll
    static void pgStart() {
        postgreSQLContainer.start();
    }

    @BeforeEach
    void setUp() throws IOException {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.defaultParser = Parser.JSON;
        AuthServiceMock.setupMockValidateResponse(mockAuthService);
    }

    @Test
    @Order(2)
    public void createLocationTest() {
        RequestLocationDto locationDto = RequestLocationDto.builder()
                .name("Test Location")
                .description("This is a test location")
                .temperature(23.5)
                .build();

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .and()
                .body(locationDto)
                .when()
                .post("/api/v1/locations/new")
                .then();

        response.log().all().statusCode(200)
                .body("name", Matchers.equalTo("Test Location"))
                .body("description", Matchers.equalTo("This is a test location"))
                .body("temperature", Matchers.equalTo(23.5f));
    }

    @Test
    @Order(2)
    public void updateLocationTest() {
        RequestLocationDto locationDto = RequestLocationDto.builder()
                .name("Updated Location")
                .description("Updated test location description")
                .temperature(30.0)
                .build();

        Long locationId = 1L;

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .and()
                .body(locationDto)
                .when()
                .put("/api/v1/locations/{locationId}", locationId)
                .then();

        response.log().all().statusCode(200)
                .body("name", Matchers.equalTo("Updated Location"))
                .body("description", Matchers.equalTo("Updated test location description"))
                .body("temperature", Matchers.equalTo(30.0f));
    }

    @Test
    @Order(3)
    public void getLocationByIdTest() {
        Long locationId = 1L;

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .when()
                .get("/api/v1/locations/{locationId}", locationId)
                .then();

        response.log().all().statusCode(200)
                .body("id", Matchers.equalTo(locationId));
    }

    @Test
    @Order(5)
    public void deleteLocationTest() {
        Long locationId = 1L;

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .when()
                .delete("/api/v1/locations/{locationId}", locationId)
                .then();

        response.log().all().statusCode(200);
    }

    @Test
    @Order(1)
    public void getAllLocationsTest() {
        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/api/v1/locations")
                .then();

        response.log().all().statusCode(200)
                .body("size()", Matchers.greaterThan(0));
    }

}