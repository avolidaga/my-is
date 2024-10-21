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
import ru.se.ifmo.tinder.dto.user_data.CreateUserDataDto;
import ru.se.ifmo.tinder.dto.user.UserDto;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.enums.Sex;
import ru.se.ifmo.tinder.repository.UserDataRepository;
import ru.se.ifmo.tinder.repository.UserRepository;
import ru.se.ifmo.tinder.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserDataControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    UserService userService;

    private UserDto userDto;

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
        userDto = UserDto.builder()
                .username("testUser")
                .password("testPassword")
                .build();
        userService.createUser(userDto);
    }

    @Test
    public void postUserFormWithCorrectDataTest() {
        CreateUserDataDto createUserDataDto = CreateUserDataDto.builder()
                .sex(Sex.MEN)
                .birth_date(LocalDate.now().minusYears(10))
                .firstname("testUser")
                .hair_color("blue")
                .height(160)
                .weight(50)
                .location(List.of(1))
                .build();

        ValidatableResponse response = given()
                .auth().basic(userDto.getUsername(), userDto.getPassword())
                .header("Content-type", "application/json")
                .and()
                .body(createUserDataDto)
                .when()
                .post("/api/user-form")
                .then();

        Integer id = response.statusCode(200)
                .body("", Matchers.isA(Integer.class))
                .extract()
                .body().as(Integer.class);
        userDataRepository.deleteById(id);
    }

    @Test
    public void postUserFormWithInCorrectDataTest() {
        CreateUserDataDto createUserDataDto = CreateUserDataDto.builder()
                .sex(Sex.MEN)
                .birth_date(LocalDate.now())
                .firstname("testUser")
                .hair_color("blue")
                .height(160)
                .weight(50)
                .build();

        ValidatableResponse response = given()
                .auth().basic(userDto.getUsername(), userDto.getPassword())
                .header("Content-type", "application/json")
                .and()
                .body(createUserDataDto)
                .when()
                .post("/api/user-form")
                .then();

        response.statusCode(400);
    }

    @AfterEach
    void tearDown() {
        User savedUser = userRepository.findByUsername(userDto.getUsername()).get();
        userRepository.delete(savedUser);
    }

    @AfterAll
    static void pgStop() {
        postgreSQLContainer.stop();
    }
}