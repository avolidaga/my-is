package ru.se.ifmo.tinder;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.se.ifmo.tinder.dto.RequestDto;
import ru.se.ifmo.tinder.dto.UserDto;
import ru.se.ifmo.tinder.dto.UserSpacesuitDataDto;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.enums.SearchStatus;
import ru.se.ifmo.tinder.model.enums.Status;
import ru.se.ifmo.tinder.repository.UserRepository;
import ru.se.ifmo.tinder.repository.UserSpacesuitDataRepository;
import ru.se.ifmo.tinder.service.UserService;
import ru.se.ifmo.tinder.service.UserSpacesuitDataService;

import java.security.Principal;

import static io.restassured.RestAssured.given;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RequestControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserSpacesuitDataRepository userSpacesuitDataRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserSpacesuitDataService userSpacesuitDataService;

    private UserDto userDto;
    private RequestDto requestDto;

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
        UserSpacesuitDataDto spacesuitDataDto = UserSpacesuitDataDto.builder()
                .chest(20)
                .hips(30)
                .waist(60)
                .height(150)
                .head(30)
                .build();

        Principal principal = new Principal() {
            @Override
            public String getName() {
                return userDto.getUsername();
            }
        };
        Integer id = userSpacesuitDataService.insertUserSpacesuitData(spacesuitDataDto, principal);
        requestDto = new RequestDto(id);
    }

    @ParameterizedTest
    @EnumSource(SearchStatus.class)
    public void getUsersRequestsByStatusTest(SearchStatus searchStatus) {

        ValidatableResponse response = given()
                .auth().basic(userDto.getUsername(), userDto.getPassword())
                .header("Content-type", "application/json")
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("status", searchStatus)
                .when()
                .get("/api/user-request-management/user-request")
                .then();

        response.statusCode(200);
    }

    @ParameterizedTest
    @EnumSource
    public void putRequestStatusTest(Status status) {
        ValidatableResponse response = given()
                .auth().basic(userDto.getUsername(), userDto.getPassword())
                .header("Content-type", "application/json")
                .queryParam("status", status)
                .body(requestDto)
                .when()
                .get("/api/user-request-management/user-request")
                .then();
        if (status == Status.NEW) {
            response.statusCode(400);
        } else {
            response.statusCode(200);
        }
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