package tests.components;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import testbase.TestBase;
import testbase.endpoints.UserApi;
import testbase.models.requests.users.UserRequest;
import testbase.models.responses.User.UserResponse;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserApiTest extends TestBase {

    private UserRequest requestBody;
//    private UserRequest.UserRequestBuilder requestBuilder;
    private ExtractableResponse<Response> eresp;

    private int userId = faker.number().numberBetween(200,300);
    private String username = faker.name().username();
    private String firstName =faker.name().firstName();
    private String lastName = faker.name().lastName();
    private String email =faker.internet().emailAddress();
    private String password = faker.name().fullName();
    private String phone = faker.phoneNumber().phoneNumber();
    private int userStatus = 1;

    @BeforeAll
    public void beforeAll(){
        log.info("\n\n------- UserApiTest STARTED -------\n\n");

        log.info("Building the common post request body for User");
        requestBody = UserRequest.builder()
                .id(userId)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .phone(phone)
                .userStatus(userStatus)
                .build();
    }

    @Test
    public void createNewUser(){

        eresp = UserApi.createNewUser(requestBody)
                        .then()
                        .statusCode(200)
                        .extract();

        UserResponse user = eresp.body().as(UserResponse.class);
        log.info("Created new user with User Id - {}",user.getId());
    }

    @Test
    public void GetUserLoginToSystem(){

        // Create New User
        eresp = UserApi.createNewUser(requestBody)
                .then()
                .statusCode(200)
                .extract();
        UserRequest user = eresp.body().as(UserRequest.class);
        String username = user.getUsername();
        String pwd = user.getPassword();

        // Verify login with new user
        ExtractableResponse<Response> lresp = UserApi.userLogin(username,pwd)
                .then()
                .statusCode(200)
                .extract();

        String expResponse = "Logged in user session";
        log.info("Logged in User Respons - {}",lresp.asString());
        assertThat(lresp.asString()).contains(expResponse);
    }
    @AfterAll
    public void afterAll(){

    }
}
