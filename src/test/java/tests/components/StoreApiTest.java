package tests.components;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import testbase.TestBase;
import testbase.endpoints.StoreApi;
import testbase.models.requests.store.OrderRequest;
import testbase.models.responses.store.OrderResponse;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StoreApiTest extends TestBase {

    private OrderRequest requestBody;
    private ExtractableResponse<Response> eresp;

    private int orderId = faker.number().numberBetween(3000, 4000);
    private int petId = faker.number().numberBetween(4000, 5000); // get pet id from getPetByid
    private int quantity = faker.number().numberBetween(1, 10);
    private Date shipDate = faker.date().past(10, TimeUnit.DAYS);
    private String orderStatus = "placed"; //placed, approved, delivered
    private boolean complete = true;

    @BeforeAll
    public void beforeAll() {

        log.info("\n\n------- SoreApiTest STARTED -------\n\n");
        log.info("Building the common post request body for Store Oder");
        requestBody = OrderRequest.builder()
                .id(orderId)
                .petId(petId)  // petid to actually comes from getpetbyid
                .quantity(quantity)
                .shipDate(shipDate)
                .status(orderStatus)
                .build();
    }

    @Test
    public void CreateNewOrderForPet() {

        eresp = StoreApi.createOrderForPet(requestBody)
                .then()
                .statusCode(200)
                .extract();

        log.info("Placed sucessful the order");
        System.out.println("Place order for pet - Response : \n" + eresp.asString());
    }

    @AfterAll
    public void afterAll() {

        OrderResponse oresp = eresp.body().as(OrderResponse.class);
        int orderId = oresp.getId();
        // call delete api


    }
}
