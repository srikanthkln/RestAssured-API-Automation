package testbase.endpoints;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import testbase.TestBase;
import testbase.models.requests.users.UserRequest;

import java.net.URL;

import static io.restassured.RestAssured.given;

@Log4j2
public class UserApi {

    private static final String apikey= TestBase.getApikey().getKey();
    private static final URL baseUrl = TestBase.getEnv().getDomainurl();
    private static final String apiVer = TestBase.getEnv().getApiver();

    private static final String userPath = "user";
    private static final RequestSpecification spec = TestBase.getRequestSpecification(baseUrl,apiVer);

    public static Response createNewUser(UserRequest bodyRequest){

        log.info("Create new user {}",bodyRequest.getUsername());
        return given()
                .header("Content-Type", "application/json")
                .spec(spec)
                .body(bodyRequest)
                .when()
                .post(userPath);
    }

    public static Response userLogin(String uid, String pwd){

        String loginEndPoint = "/login";
        log.info("User Logging into System - User - {} and Pwd - {}", uid, pwd);
        return given()
                .header("Content-Type", "application/json")
                .spec(spec)
                .queryParam("username",uid)
                .queryParam("password",pwd)
                .when()
                .get(userPath+loginEndPoint);
    }
}
