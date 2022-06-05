package testbase.endpoints;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import testbase.TestBase;
import testbase.models.requests.pets.PetRequest;

import java.net.URL;

import static io.restassured.RestAssured.given;

@Log4j2
public class PetApi {

    private static final String apikey= TestBase.getApikey().getKey();
    private static final URL baseUrl = TestBase.getEnv().getDomainurl();
    private static final String apiVer = TestBase.getEnv().getApiver();

    private static final String petsPath = "pet";
    private static final RequestSpecification spec = TestBase.getRequestSpecification(baseUrl,apiVer);

    public static Response createAddNewPet(PetRequest bodyRequest){

        log.info("Add New Pet to the store");
        return given()
                .header("Content-Type", "application/json")
                .spec(spec)
                .body(bodyRequest)
                .when()
                .post(petsPath);
    }

    public static Response getPetsByStatus(String status){

        log.info("Get Pets by Pet status");
        String endPoint = "/findByStatus";
        return given()
                .header("Content-Type", "application/json")
                .spec(spec)
                .queryParam("status",status)
                .when()
                .get(petsPath+endPoint);
    }

    public static Response getPetById(int petId) {

        log.info("Get Pet by petId");
        return given()
                .header("Content-Type", "application/json")
                .spec(spec)
                .pathParams("petid",petId)
                .when()
                .get(petsPath+"/{petid}");
    }

    public static Response deletePetById(int petId){

        log.info("Delete pet from store by petId");
        String endPoint = "/findByStatus";
        return given()
                .header("Content-Type", "application/json")
                .spec(spec)
                .pathParams("petid",petId)
                .when()
                .delete(petsPath+"/{petid}");
    }
}
