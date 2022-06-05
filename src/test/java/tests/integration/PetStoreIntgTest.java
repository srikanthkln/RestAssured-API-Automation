package tests.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import testbase.TestBase;
import testbase.endpoints.PetApi;
import testbase.endpoints.StoreApi;
import testbase.models.requests.pets.Category;
import testbase.models.requests.pets.PetRequest;
import testbase.models.requests.pets.Tag;
import testbase.models.requests.pets.enums.PetStatus;
import testbase.models.requests.store.OrderRequest;
import testbase.models.responses.pets.PetResponse;
import testbase.models.responses.store.OrderResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PetStoreIntgTest extends TestBase {

    private PetRequest petCreateReq;
    private PetResponse petCreateResp;
    private OrderRequest storeOrderReq;
    private OrderResponse storeOrderResp;


    // -----------------Add new pet request data----------------
    private int petId = faker.number().numberBetween(1002, 2002);
    private String petName = faker.dog().name();
    private String petStatus = PetStatus.AVAILABLE.getValue();
    private int catgId = faker.number().numberBetween(101, 201);
    private String catgname = faker.dog().breed();
    private int tagId1 = faker.number().numberBetween(6000, 7000);
    private String tagName1 = "tag-" + faker.dog().sound();
    private int tagId2 = faker.number().numberBetween(7000, 8000);
    private String tagName2 = "tag-" + faker.dog().sound();

    // --------------- Place Order Data -------------------------
    private int orderId = faker.number().numberBetween(3000, 4000);
    private int quantity = faker.number().numberBetween(1, 10);
    private Date shipDate = faker.date().past(10, TimeUnit.DAYS);
    private String orderStatus = "placed"; //placed, approved, delivered
    private boolean complete = true;

    private Category categoryreq ;
    private List<Tag> tags;


    @BeforeAll
    public void beforeALl(){
        log.info("\n\n------- Integration Tests STARTED -------\n\n");
    }
    /* Test :
        1. Pet Api -  Add new pet to store
        2. Store Api - Place order for the above pet
     */
    @Test
    public void AddPetToStoreAndPlaceOrder(){

        //----------  1.  Pet Api -  Add new pet to store  -------------------
        Category categoryreq = Category.builder().id(catgId).name(catgname).build();
        // Add Tags Data
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(tagId1, tagName1));
        tags.add(new Tag(tagId2, tagName2));

        // Build Add New Pet request data
        petCreateReq = PetRequest.builder()
                .id(petId)
                .name(petName)
                .category(categoryreq)
                .tags(tags)
                .status(petStatus)
                .build();

        //ACTION: Send the REQUEST and extract the RESPONSE upon sucess
        ExtractableResponse<Response> eresp = PetApi.createAddNewPet(petCreateReq)
                .then().statusCode(200)
                .extract();

//        System.out.println("Response is : " + eresp.asString());
        petCreateResp = eresp.body().as(PetResponse.class);
        int petId_actual = petCreateResp.getId();

        //------------  2. Store Api - Place order for the above pet----------

        storeOrderReq = OrderRequest.builder()
                .id(orderId)
                .petId(petId_actual)  // Get the pet id from above create pet
                .quantity(quantity)
                .shipDate(shipDate)
                .status(orderStatus)
                .build();

        ExtractableResponse<Response> oresp = StoreApi.createOrderForPet(storeOrderReq)
                .then()
                .statusCode(200)
                .extract();

        storeOrderResp = oresp.body().as(OrderResponse.class);
        int orderId_act = storeOrderResp.getId();

        assertThat(orderId_act).isEqualTo(orderId);
        assertThat(petCreateResp.id).isEqualTo(storeOrderResp.getPetId());

    }


}
