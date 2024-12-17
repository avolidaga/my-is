package ru.se.info.tinder.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
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
import ru.se.info.tinder.dto.RequestFabricTextureDto;

import java.io.IOException;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Import(DbInitializer.class)
@AutoConfigureWebMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@WireMockTest
@ContextConfiguration(classes = {WireMockConfig.class, AuthServiceMock.class})
public class FabricTextureControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private WireMockServer mockAuthService;

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
        AuthServiceMock.setupMockValidateResponse(mockAuthService);
    }

    @Test
    public void createFabricTextureTest() {
        RequestFabricTextureDto fabricTextureDto = RequestFabricTextureDto.builder()
                .name("Test Fabric")
                .description("Test fabric texture")
                .build();

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .and()
                .body(fabricTextureDto)
                .when()
                .post("/api/v1/fabric-textures/new")
                .then();

        response.log().all().statusCode(200)
                .body("name", Matchers.equalTo("Test Fabric"))
                .body("description", Matchers.equalTo("Test fabric texture"));
    }

    @Test
    public void updateFabricTextureTest() {
        RequestFabricTextureDto fabricTextureDto = RequestFabricTextureDto.builder()
                .name("Updated Fabric")
                .description("Updated fabric texture description")
                .build();

        Long fabricTextureId = 2L;

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .and()
                .body(fabricTextureDto)
                .when()
                .put("/api/v1/fabric-textures/{fabricTextureId}", fabricTextureId)
                .then();

        response.log().all().statusCode(200)
                .body("name", Matchers.equalTo("Updated Fabric"))
                .body("description", Matchers.equalTo("Updated fabric texture description"));
    }

    @Test
    public void deleteFabricTextureTest() {
        Long fabricTextureId = 1L;

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .when()
                .delete("/api/v1/fabric-textures/{fabricTextureId}", fabricTextureId)
                .then();

        response.log().all().statusCode(200);
    }

    @Test
    public void getFabricTextureByIdTest() {
        Long fabricTextureId = 2L;

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .when()
                .get("/api/v1/fabric-textures/{fabricTextureId}", fabricTextureId)
                .then();

        response.log().all().statusCode(200)
                .body("id", Matchers.equalTo(fabricTextureId.intValue()));
    }

    @Test
    public void getAllFabricTexturesTest() {
        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + validToken)
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/api/v1/fabric-textures")
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
