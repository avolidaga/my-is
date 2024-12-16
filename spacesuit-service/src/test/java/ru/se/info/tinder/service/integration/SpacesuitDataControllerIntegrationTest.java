package ru.se.info.tinder.service.integration;

import io.r2dbc.spi.ConnectionFactory;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import ru.se.info.tinder.dto.CreateSpacesuitDataDto;
import ru.se.info.tinder.dto.UpdateSpacesuitDataDto;

import java.io.IOException;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Import(DbInitializer.class)
@AutoConfigureWebMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@WireMockTest
//@ContextConfiguration(classes = {WireMockConfig.class, AuthServiceMock.class})
public class SpacesuitDataControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

//    @Autowired
//    private WireMockServer mockAuthService;

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
    void setUp() throws IOException {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.defaultParser = Parser.JSON;
//        AuthServiceMock.setupMockValidateResponse(mockAuthService);
    }

    @Test
    public void createSpacesuitDataTest() {
        CreateSpacesuitDataDto dto = CreateSpacesuitDataDto.builder()
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
                .body("userRequestId", Matchers.notNullValue())
                .body("status", Matchers.equalTo("NEW"));
    }

    @Test
    public void updateSpacesuitDataTest() {
        UpdateSpacesuitDataDto dto = UpdateSpacesuitDataDto.builder()
                .id(1L)
                .fabricTextureId(2L)
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
                .body("fabricTextureId", Matchers.equalTo(2));
    }

    @Test
    public void deleteSpacesuitDataTest() {
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + validToken)
                .when()
                .delete("/api/v1/spacesuit-data/{spacesuitDataId}", 1L)
                .then();

        response.log().all().statusCode(204);
    }

    @Test
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

class DbInitializer {
    private static boolean initialized = false;

    @Autowired
    void initializeDb(ConnectionFactory connectionFactory) {
        if (!initialized) {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource[] scripts = new Resource[]{
                    resourceLoader.getResource("classpath:scripts/init_script.sql")
            };
            new ResourceDatabasePopulator(scripts).populate(connectionFactory).block();
            initialized = true;
        }
    }
}
