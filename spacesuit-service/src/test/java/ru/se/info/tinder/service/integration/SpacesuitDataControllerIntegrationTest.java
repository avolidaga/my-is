package ru.se.info.tinder.service.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import ru.se.info.tinder.dto.CreateSpacesuitDataDto;
import ru.se.info.tinder.dto.UpdateSpacesuitDataDto;

import java.io.IOException;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@TestPropertySource(properties = {
        "spring.test.database.replace=none"
})
@WireMockTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = {WireMockConfig.class, AuthServiceMock.class, SpacesuitServiceMock.class})
public class SpacesuitDataControllerIntegrationTest {

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
        SpacesuitServiceMock.setupMockFabricTextureResponse(wireMockService);
    }

    @Test
    @Order(1)
    public void createSpacesuitDataTest() {
        CreateSpacesuitDataDto dto = CreateSpacesuitDataDto.builder()
                .id(1L)
                .chest(10)
                .hips(10)
                .waist(10)
                .head(10)
                .height(160)
                .footSize(10)
                .fabricTextureId(1L)
                .build();

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .and()
                .body(dto)
                .when()
                .post("/api/v1/spacesuit-data/new")
                .then();

        response.log().all().statusCode(201)
                .body("user_request_id", Matchers.notNullValue())
                .body("status", Matchers.equalTo("NEW"));
    }

    @Test
    @Order(2)
    public void updateSpacesuitDataTest() {
        UpdateSpacesuitDataDto dto = UpdateSpacesuitDataDto.builder()
                .id(1L)
                .chest(10)
                .hips(10)
                .waist(10)
                .head(10)
                .height(160)
                .footSize(20)
                .fabricTextureId(1L)
                .build();

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .and()
                .body(dto)
                .when()
                .put("/api/v1/spacesuit-data/{spacesuitDataId}", 1L)
                .then();

        response.log().all().statusCode(200)
                .body("id", Matchers.equalTo(1))
                .body("foot_size", Matchers.equalTo(20));
    }

    @Test
    @Order(3)
    public void deleteSpacesuitDataTest() {
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + validToken)
                .when()
                .delete("/api/v1/spacesuit-data/{spacesuitDataId}", 1L)
                .then();

        response.log().all().statusCode(204);
    }

    @Test
    @Order(2)
    public void getSpacesuitDataByIdTest() {
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + validToken)
                .when()
                .get("/api/v1/spacesuit-data/{spacesuitDataId}", 1L)
                .then();

        response.log().all().statusCode(200)
                .body("id", Matchers.equalTo(1));
    }

    @Test
    @Order(2)
    public void getCurrentUserSpacesuitDataTest() {
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + validToken)
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/api/v1/spacesuit-data/my")
                .then();

        response.log().all().statusCode(200)
                .body("size()", Matchers.greaterThan(0));
    }
}
