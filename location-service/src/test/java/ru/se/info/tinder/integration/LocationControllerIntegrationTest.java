package ru.se.info.tinder.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.r2dbc.spi.ConnectionFactory;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import ru.se.info.tinder.dto.RequestLocationDto;

import java.io.IOException;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Import(DbInitializer.class)
@AutoConfigureWebMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@WireMockTest
@ContextConfiguration(classes = {WireMockConfig.class, AuthServiceMock.class})
public class LocationControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private WireMockServer mockLoginService;

    private final String validToken = "Valid token";

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("tinder")
            .withUsername("postgres")
            .withPassword("root");

    @BeforeAll
    static void pgStart() {
        postgreSQLContainer.start();
    }

    @BeforeEach
    void setUp() throws IOException {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.defaultParser = Parser.JSON;
        AuthServiceMock.setupMockValidateResponse(mockLoginService);
    }

    @Test
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
    public void createLocationWithValidationErrorsTest() {
        RequestLocationDto locationDto = RequestLocationDto.builder()
                .name("")
                .description("")
                .build();

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .and()
                .body(locationDto)
                .when()
                .post("/api/v1/locations/new")
                .then();

        response.log().all().statusCode(500);
    }


    @Test
    public void updateLocationTest() {
        RequestLocationDto locationDto = RequestLocationDto.builder()
                .name("Updated Location")
                .description("Updated test location description")
                .temperature(30.0)
                .build();

        Long locationId = 2L;

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
    public void getLocationByIdTest() {
        int locationId = 2;

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

    @Test
    public void getAllLocationsWithCircuitBreakerTest() throws IOException {
        AuthServiceMock.setupMockValidateResponseWithError(mockLoginService);
        ValidatableResponse response = null;
        for (int i = 0; i < 6; i++) {
            response = given()
                    .header("Content-type", "application/json")
                    .header("Authorization", "Bearer " + validToken)
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                    .when()
                    .get("/api/v1/locations")
                    .then();
        }

        response.log().all().statusCode(500);
    }

}

class DbInitializer {
    private static boolean initialized = false;

    @Autowired
    void initializeDb(ConnectionFactory connectionFactory) {
        if (!initialized) {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource[] scripts = new Resource[]{
                    resourceLoader.getResource("classpath:scripts/init_script.sql")
            };
            new ResourceDatabasePopulator(scripts).populate(connectionFactory).block();
            initialized = true;
        }
    }
}