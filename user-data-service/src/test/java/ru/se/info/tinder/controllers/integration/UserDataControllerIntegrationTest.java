package ru.se.info.tinder.controllers.integration;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import ru.se.info.tinder.dto.CreateUserDataDto;
import ru.se.info.tinder.dto.UpdateUserDataDto;
import ru.se.info.tinder.model.enums.Sex;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
//@Import(DbInitializer.class)
@AutoConfigureWebMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@WireMockTest
public class UserDataControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

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
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
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
                .locations(List.of(1L, 2L))
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
                .body("user_id", Matchers.equalTo(1))
                .body("firstname", Matchers.equalTo("John"))
                .body("lastname", Matchers.equalTo("Doe"))
                .body("birth_date", Matchers.equalTo("1990-01-01"))
                .body("sex", Matchers.equalTo("MALE"))
                .body("weight", Matchers.equalTo(70))
                .body("height", Matchers.equalTo(175))
                .body("hair_color", Matchers.equalTo("Brown"))
                .body("locations", Matchers.containsInAnyOrder(1, 2));
    }

    @Test
    public void updateUserDataTest() {
        // Создаем объект с обновленными данными
        UpdateUserDataDto updateUserDataDto = UpdateUserDataDto.builder()
                .birthDate(LocalDate.of(1995, 5, 15))
                .firstname("Jane")
                .lastname("Smith")
                .sex(Sex.WOMEN)
                .weight(60)
                .height(165)
                .hairColor("Blonde")
                .locations(List.of(3L, 4L))
                .build();

        // Отправляем PUT запрос на обновление
        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .and()
                .body(updateUserDataDto)
                .when()
                .put("/api/v1/user-data/{id}", 1)
                .then();

        // Проверяем успешный статус и обновленные данные
        response.log().all().statusCode(200)
                .body("birth_date", Matchers.equalTo("1995-05-15"))
                .body("firstname", Matchers.equalTo("Jane"))
                .body("lastname", Matchers.equalTo("Smith"))
                .body("sex", Matchers.equalTo("FEMALE"))
                .body("weight", Matchers.equalTo(60))
                .body("height", Matchers.equalTo(165))
                .body("hair_color", Matchers.equalTo("Blonde"))
                .body("locations", Matchers.containsInAnyOrder(3, 4));
    }


    @Test
    public void updateUserDataValidationErrorTest() {
        // Создаем объект с некорректными данными
        UpdateUserDataDto invalidUpdateDto = UpdateUserDataDto.builder()
                .birthDate(LocalDate.now().plusDays(1)) // Некорректная дата
                .firstname("") // Пустое имя
                .lastname("D") // Слишком короткая фамилия
                .sex(null) // Не указан пол
                .weight(-70) // Отрицательный вес
                .height(500) // Высота больше 300
                .hairColor("") // Пустой цвет волос
                .locations(List.of()) // Пустой список локаций
                .build();

        // Отправляем PUT запрос с некорректными данными
        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .and()
                .body(invalidUpdateDto)
                .when()
                .put("/api/v1/user-data/{id}", 1)
                .then();

        // Ожидаем статус 400 и проверяем сообщение об ошибке
        response.log().all().statusCode(400)
                .body("message", Matchers.containsString("Birth date must be in the past"))
                .body("message", Matchers.containsString("Firstname is required"))
                .body("message", Matchers.containsString("Lastname name must be between 2 and 50 characters"))
                .body("message", Matchers.containsString("Sex is required"))
                .body("message", Matchers.containsString("Weight must be a positive number"))
                .body("message", Matchers.containsString("Height must be less than or equal to 300"))
                .body("message", Matchers.containsString("Hair color is required"))
                .body("message", Matchers.containsString("Specify at least one location"));
    }


    @Test
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
