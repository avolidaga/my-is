package ru.se.info.tinder.controllers.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import ru.se.info.tinder.dto.CreateUserDataDto;
import ru.se.info.tinder.dto.UpdateUserDataDto;
import ru.se.info.tinder.model.enums.Sex;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@TestPropertySource(properties = {
        "spring.test.database.replace=none"
})
@WireMockTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = {WireMockConfig.class, AuthServiceMock.class, LocationServiceMock.class})
public class UserDataControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private WireMockServer wireMockService;

    private final String validToken = "Valid token";

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("tinder")
            .withUsername("postgres")
            .withPassword("root");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeAll
    static void pgStart() {
        postgreSQLContainer.withInitScript("scripts/init_script.sql");
        postgreSQLContainer.start();
    }

    @BeforeEach
    void setUp() throws IOException {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.defaultParser = Parser.JSON;
        AuthServiceMock.setupMockValidateResponse(wireMockService);
        LocationServiceMock.setupMockLocationResponse(wireMockService);
    }

    @Test
    @Order(1)
    public void createUserDataTest() {
        CreateUserDataDto userDataDto = CreateUserDataDto.builder()
                .userId(1L)
                .birthDate(LocalDate.of(1990, 1, 1))
                .firstname("John")
                .lastname("Doe")
                .sex(Sex.MEN)
                .weight(70)
                .height(175)
                .hairColor("Brown")
                .locations(List.of(1L))
                .build();

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .and()
                .body(userDataDto)
                .when()
                .post("/api/v1/user-data/new")
                .then();

        response.log().all().statusCode(201)
                .body("id", Matchers.equalTo(1))
                .body("firstname", Matchers.equalTo("John"))
                .body("lastname", Matchers.equalTo("Doe"))
                .body("sex", Matchers.equalTo("MEN"))
                .body("weight", Matchers.equalTo(70))
                .body("height", Matchers.equalTo(175))
                .body("hair_color", Matchers.equalTo("Brown"))
                .body("locations", Matchers.containsInAnyOrder(1));
    }

    @Test
    @Order(2)
    public void updateUserDataTest() {
        UpdateUserDataDto updateUserDataDto = UpdateUserDataDto.builder()
                .birthDate(LocalDate.of(1995, 5, 15))
                .firstname("Jane")
                .lastname("Smith")
                .sex(Sex.WOMEN)
                .weight(60)
                .height(165)
                .hairColor("Blonde")
                .locations(List.of(1L))
                .build();

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .and()
                .body(updateUserDataDto)
                .when()
                .put("/api/v1/user-data/{id}", 1)
                .then();

        response.log().all().statusCode(200)
                .body("firstname", Matchers.equalTo("Jane"))
                .body("lastname", Matchers.equalTo("Smith"))
                .body("sex", Matchers.equalTo("WOMEN"))
                .body("weight", Matchers.equalTo(60))
                .body("height", Matchers.equalTo(165))
                .body("hair_color", Matchers.equalTo("Blonde"))
                .body("locations", Matchers.containsInAnyOrder(1));
    }


    @Test
    @Order(2)
    public void updateUserDataValidationErrorTest() {
        UpdateUserDataDto invalidUpdateDto = UpdateUserDataDto.builder()
                .birthDate(LocalDate.now().plusDays(1))
                .firstname("")
                .lastname("D")
                .sex(null)
                .weight(-70)
                .height(500)
                .hairColor("")
                .locations(List.of())
                .build();

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .and()
                .body(invalidUpdateDto)
                .when()
                .put("/api/v1/user-data/{id}", 1)
                .then();

        response.log().all().statusCode(400);
//                .body("message", Matchers.containsString("Birth date must be in the past"))
//                .body("message", Matchers.containsString("Firstname is required"))
//                .body("message", Matchers.containsString("Lastname name must be between 2 and 50 characters"))
//                .body("message", Matchers.containsString("Sex is required"))
//                .body("message", Matchers.containsString("Weight must be a positive number"))
//                .body("message", Matchers.containsString("Height must be less than or equal to 300"))
//                .body("message", Matchers.containsString("Hair color is required"))
//                .body("message", Matchers.containsString("Specify at least one location"));
    }


    @Test
    @Order(4)
    public void deleteUserDataTest() {
        Long userId = 1L;

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .when()
                .delete("/api/v1/user-data/{id}", userId)
                .then();

        response.log().all()
                .statusCode(204);
    }

    @Test
    @Order(3)
    public void getUserDataByIdTest() {
        Long userId = 1L;

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .when()
                .get("/api/v1/user-data/{id}", userId)
                .then();

        response.log().all()
                .statusCode(200)
                .body("id", Matchers.equalTo(userId.intValue()));
    }

    @Test
    @Order(3)
    public void getUsersByLocationIdTest() {
        Long locationId = 1L;

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/api/v1/user-data/location/{locationId}", locationId)
                .then();

        response.log().all()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0));
    }

    @Test
    @Order(3)
    public void getAllUsersDataTest() {
        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/api/v1/user-data")
                .then();

        response.log().all()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0));
    }
}
