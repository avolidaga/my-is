package ru.se.info.tinder;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.se.info.tinder.dto.RequestUserDto;
import ru.se.info.tinder.model.UserEntity;
import ru.se.info.tinder.repository.UserRepository;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private UserRepository userRepository;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("tinder")
            .withUsername("postgres")
            .withPassword("root");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    void testCreateUser() {
        RequestUserDto userDto = new RequestUserDto();
        userDto.setUsername("testUser");
        userDto.setPassword("testPassword");

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(userDto)
                .when()
                .post("/api/v1/user-management/users/new")
                .then()
                .log().all();

        response.statusCode(201)
                .body("username", equalTo("testUser"));

        UserEntity savedUser = userRepository.findByUsername("testUser").orElse(null);
        Assertions.assertNotNull(savedUser);
        userRepository.delete(savedUser);
    }

    @Test
    void testGetUserById() {
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user = userRepository.save(user);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .when()
                .get("/api/v1/user-management/users/" + user.getId())
                .then()
                .log().all();

        response.statusCode(200)
                .body("username", equalTo("testUser"));

        userRepository.delete(user);
    }

    @Test
    void testUpdateUserById() {
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user = userRepository.save(user);

        RequestUserDto updatedUserDto = new RequestUserDto();
        updatedUserDto.setUsername("updatedUser");
        updatedUserDto.setPassword("updatedPassword");

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(updatedUserDto)
                .when()
                .put("/api/v1/user-management/users/" + user.getId())
                .then()
                .log().all();

        response.statusCode(200)
                .body("username", equalTo("updatedUser"));

        UserEntity updatedUser = userRepository.findById(user.getId()).orElse(null);
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("updatedUser", updatedUser.getUsername());

        userRepository.delete(updatedUser);
    }

    @Test
    void testDeleteUserById() {
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user = userRepository.save(user);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/api/v1/user-management/users/" + user.getId())
                .then()
                .log().all();

        response.statusCode(204);

        UserEntity deletedUser = userRepository.findById(user.getId()).orElse(null);
        Assertions.assertNull(deletedUser);
    }

    @AfterAll
    static void tearDown() {
        postgreSQLContainer.stop();
    }
}
