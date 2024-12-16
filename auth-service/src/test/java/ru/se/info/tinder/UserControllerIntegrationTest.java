package ru.se.info.tinder;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.se.info.tinder.dto.RequestUserDto;
import ru.se.info.tinder.model.Roles;
import ru.se.info.tinder.model.UserEntity;
import ru.se.info.tinder.model.enums.RoleName;
import ru.se.info.tinder.repository.UserRepository;
import ru.se.info.tinder.utils.JwtTokensUtils;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@TestPropertySource(properties = {
        "spring.test.database.replace=none"
})
public class UserControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokensUtils jwtTokensUtils;

    private String validJwtToken;

    private UserEntity testEntity;

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
    void setUp() {
        testEntity = UserEntity.builder()
                .id(1L)
                .username("admin")
                .password("adminadmin")
                .role(new Roles(1L, RoleName.ADMIN))
                .build();
        validJwtToken = jwtTokensUtils.generateToken(testEntity);
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
                .header("Authorization", "Bearer " + validJwtToken)
                .body(userDto)
                .when()
                .post("/api/v1/user-management/users/new")
                .then()
                .log().all();

        response.statusCode(201);

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
                .header("Authorization", "Bearer " + validJwtToken)
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
        RequestUserDto updatedUserDto = new RequestUserDto();
        updatedUserDto.setUsername("admin");
        updatedUserDto.setPassword("updatedPassword");

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + validJwtToken)
                .body(updatedUserDto)
                .when()
                .put("/api/v1/user-management/users/" + testEntity.getId())
                .then()
                .log().all();

        response.statusCode(200)
                .body("username", equalTo("admin"));

        UserEntity updatedUser = userRepository.findById(testEntity.getId()).orElse(null);
        Assertions.assertNotNull(updatedUser);
    }

    @Test
    void testDeleteUserById() {
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user = userRepository.save(user);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + validJwtToken)
                .when()
                .delete("/api/v1/user-management/users/" + user.getId())
                .then()
                .log().all();

        response.statusCode(200);

        UserEntity deletedUser = userRepository.findById(user.getId()).orElse(null);
        Assertions.assertNull(deletedUser);
    }

    @AfterAll
    static void tearDown() {
        postgreSQLContainer.stop();
    }
}
