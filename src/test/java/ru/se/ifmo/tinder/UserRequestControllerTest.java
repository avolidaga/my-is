package ru.se.ifmo.tinder;

import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRequestControllerTest {

//    @LocalServerPort
//    private Integer port;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    UserSpacesuitDataRepository userSpacesuitDataRepository;
//
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    UserSpacesuitDataService userSpacesuitDataService;
//
//    private UserDto userDto;
//    private GetUserRequestDto getUserRequestDto;
//
//    @Container
//    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
//            .withDatabaseName("tinder")
//            .withUsername("postgres")
//            .withPassword("root");
//
//    @DynamicPropertySource
//    static void postgresqlProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
//        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
//        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
//    }
//
//    @BeforeAll
//    static void pgStart() {
//        postgreSQLContainer.start();
//    }
//
//    @BeforeEach
//    void setUp() {
//        RestAssured.baseURI = "http://localhost:" + port;
//        RestAssured.defaultParser = Parser.JSON;
//        userDto = UserDto.builder()
//                .username("testUser")
//                .password("testPassword")
//                .build();
//        userService.createUser(userDto);
//        UserSpacesuitDataDto spacesuitDataDto = UserSpacesuitDataDto.builder()
//                .chest(20)
//                .hips(30)
//                .waist(60)
//                .height(150)
//                .head(30)
//                .build();
//
//        Principal principal = new Principal() {
//            @Override
//            public String getName() {
//                return userDto.getUsername();
//            }
//        };
//        Integer id = userSpacesuitDataService.createUserSpacesuitData(spacesuitDataDto, principal);
//        getUserRequestDto = new GetUserRequestDto(id);
//    }
//
//    @ParameterizedTest
//    @EnumSource(SearchStatus.class)
//    public void getUsersRequestsByStatusTest(SearchStatus searchStatus) {
//
//        ValidatableResponse response = given()
//                .auth().basic(userDto.getUsername(), userDto.getPassword())
//                .header("Content-type", "application/json")
//                .queryParam("page", 1)
//                .queryParam("size", 10)
//                .queryParam("status", searchStatus)
//                .when()
//                .get("/api/user-request-management/user-request")
//                .then();
//
//        response.statusCode(200);
//    }
//
//    @ParameterizedTest
//    @EnumSource
//    public void putRequestStatusTest(RequestStatus status) {
//        ValidatableResponse response = given()
//                .auth().basic(userDto.getUsername(), userDto.getPassword())
//                .header("Content-type", "application/json")
//                .queryParam("status", status)
//                .body(getUserRequestDto)
//                .when()
//                .get("/api/user-request-management/user-request")
//                .then();
//        if (status == RequestStatus.NEW) {
//            response.statusCode(400);
//        } else {
//            response.statusCode(200);
//        }
//    }
//
//    @AfterEach
//    void tearDown() {
//        User savedUser = userRepository.findByUsername(userDto.getUsername()).get();
//        userRepository.delete(savedUser);
//    }
//
//    @AfterAll
//    static void pgStop() {
//        postgreSQLContainer.stop();
//    }
}