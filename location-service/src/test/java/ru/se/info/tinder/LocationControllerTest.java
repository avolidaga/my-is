package ru.se.info.tinder;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.se.info.tinder.dto.RequestLocationDto;
import ru.se.info.tinder.service.LocationService;
import ru.se.info.tinder.repository.LocationRepository;

import static io.restassured.RestAssured.given;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationRepository locationRepository;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("tinder")
            .withUsername("postgres")
            .withPassword("root");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @BeforeAll
    static void pgStart() {
        postgreSQLContainer.start();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.defaultParser = Parser.JSON;
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
    public void updateLocationTest() {
        RequestLocationDto locationDto = RequestLocationDto.builder()
                .name("Updated Location")
                .description("Updated test location description")
                .temperature(30.0)
                .build();

        Long locationId = 1L;

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
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
        Long locationId = 1L;

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
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
                .when()
                .delete("/api/v1/locations/{locationId}", locationId)
                .then();

        response.log().all().statusCode(200);
    }

    @Test
    public void getAllLocationsTest() {
        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/api/v1/locations")
                .then();

        response.log().all().statusCode(200)
                .body("size()", Matchers.greaterThan(0));
    }

}
