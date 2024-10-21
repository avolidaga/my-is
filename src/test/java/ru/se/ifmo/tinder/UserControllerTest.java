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
import ru.se.ifmo.tinder.dto.user.UserDto;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.repository.UserRepository;
import ru.se.ifmo.tinder.service.UserService;

import static io.restassured.RestAssured.given;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

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
    public void registerUserWithCorrectDataTest() {
        UserDto userDto = UserDto.builder()
                .username("testUser")
                .password("testPassword")
                .build();

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .and()
                .body(userDto)
                .when()
                .post("/api/auth-management/register")
                .then();

        response.statusCode(204);

        User savedUser = userRepository.findByUsername(userDto.getUsername()).get();
        Assertions.assertEquals(savedUser.getUsername(), userDto.getUsername());
        userRepository.delete(savedUser);
    }

    @Test
    public void registerUserWithIncorrectDataTest() {
        UserDto userDto = UserDto.builder()
                .username("n")
                .password("test")
                .build();

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .and()
                .body(userDto)
                .when()
                .post("/api/auth-management/register")
                .then();

        response.statusCode(400)
                .body("username", Matchers.equalTo("Username must be between 3 and 50 characters"))
                .body("password", Matchers.equalTo("Password must be at least 8 characters long"));
    }

    @Test
    public void loginUserWithCorrectDataTest() {
        UserDto userDto = UserDto.builder()
                .username("testUser")
                .password("testPassword")
                .build();
        userService.createUser(userDto);

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .and()
                .body(userDto)
                .when()
                .post("/api/auth-management/loginUser")
                .then();

        response.statusCode(200)
                .body("role", Matchers.equalTo("USER"));

        User savedUser = userRepository.findByUsername(userDto.getUsername()).get();
        userRepository.delete(savedUser);
    }

    @Test
    public void loginUserWithIncorrectDataTest() {
        UserDto userDto = UserDto.builder()
                .username("n")
                .password("test")
                .build();

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .and()
                .body(userDto)
                .when()
                .post("/api/auth-management/loginUser")
                .then();

        response.statusCode(400)
                .body("username", Matchers.equalTo("Username must be between 3 and 50 characters"))
                .body("password", Matchers.equalTo("Password must be at least 8 characters long"));
    }

    @AfterAll
    static void pgStop() {
        postgreSQLContainer.stop();
    }
}