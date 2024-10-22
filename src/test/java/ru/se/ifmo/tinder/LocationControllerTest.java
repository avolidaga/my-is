package ru.se.ifmo.tinder;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.se.ifmo.tinder.dto.location.RequestLocationDto;
import ru.se.ifmo.tinder.dto.user.RequestUserDto;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.repository.LocationRepository;
import ru.se.ifmo.tinder.repository.UserRepository;
import ru.se.ifmo.tinder.service.UserService;

import static io.restassured.RestAssured.given;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    RequestUserDto userDto;

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
        userDto = RequestUserDto.builder()
                .username("testUser")
                .password("testPassword")
                .build();
        userService.createUser(userDto);
    }

    @Test
    public void createNewLocationTest() {
        RequestLocationDto dto = RequestLocationDto.builder()
                .name("test")
                .description("test")
                .temperature(0d)
                .build();
        ValidatableResponse response = given()
                .auth().basic(userDto.getUsername(), userDto.getPassword())
                .header("Content-type", "application/json")
                .and()
                .body(dto)
                .when()
                .post("/api/v1/locations/new")
                .then();

        response.log().all().statusCode(201);
    }

    @Test
    public void createNewLocationWithIncorrectDataTest() {
        RequestLocationDto dto = RequestLocationDto.builder()
                .temperature(0d)
                .build();
        ValidatableResponse response = given()
                .auth().basic(userDto.getUsername(), userDto.getPassword())
                .header("Content-type", "application/json")
                .and()
                .body(dto)
                .when()
                .post("/api/v1/locations/new")
                .then();

        response.log().all().statusCode(400);
    }

    @Test
    public void getLocationByIdTest() {
        ValidatableResponse response = given()
                .auth().basic(userDto.getUsername(), userDto.getPassword())
                .header("Content-type", "application/json")
                .and()
                .pathParam("locationId", 1)
                .when()
                .get("/api/v1/locations/{locationId}")
                .then();

        response.log().all().statusCode(200);
    }

    @Test
    public void getAllLocationsTest() {
        ValidatableResponse response = given()
                .auth().basic(userDto.getUsername(), userDto.getPassword())
                .header("Content-type", "application/json")
                .and()
                .queryParam("size", 10)
                .queryParam("page", 0)
                .when()
                .get("/api/v1/locations")
                .then();

        response.log().all().statusCode(200);
    }

    @AfterEach
    void tearDown() {
        userRepository.delete(userRepository.findByUsername(userDto.getUsername()).get());
    }

    @AfterAll
    static void pgStop() {
        postgreSQLContainer.stop();
    }
}