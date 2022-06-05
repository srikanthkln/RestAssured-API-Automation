package testbase.endpoints;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import testbase.TestBase;
import testbase.models.requests.store.OrderRequest;

import java.net.URL;

import static io.restassured.RestAssured.given;

@Log4j2
public class StoreApi {

    private static final String apikey= TestBase.getApikey().getKey();
    private static final URL baseUrl = TestBase.getEnv().getDomainurl();
    private static final String apiVer = TestBase.getEnv().getApiver();

    private static final String storePath = "store";
    private static final RequestSpecification spec = TestBase.getRequestSpecification(baseUrl,apiVer);


    public static Response createOrderForPet(OrderRequest bodyRequest){
        String endPointPath = "/order";
        log.info("Create new order for pet");
        return given()
                .header("Content-Type", "application/json")
                .spec(spec)
                .body(bodyRequest)
                .when()
                .post(storePath+endPointPath);
    }
}
