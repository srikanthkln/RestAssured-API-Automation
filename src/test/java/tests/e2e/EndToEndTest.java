package tests.e2e;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import testbase.TestBase;
import testbase.endpoints.PetApi;
import testbase.endpoints.StoreApi;
import testbase.endpoints.UserApi;
import testbase.models.requests.pets.Category;
import testbase.models.requests.pets.PetRequest;
import testbase.models.requests.pets.Tag;
import testbase.models.requests.pets.enums.PetStatus;
import testbase.models.requests.store.OrderRequest;
import testbase.models.requests.users.UserRequest;
import testbase.models.responses.User.UserResponse;
import testbase.models.responses.pets.PetResponse;
import testbase.models.responses.store.OrderResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EndToEndTest extends TestBase {

    private UserRequest userLoginReq;
    private UserResponse userLoginResp;
    private PetRequest petCreateReq;
    private PetResponse petCreateResp;
    private OrderRequest storeOrderReq;
    private OrderResponse storeOrderResp;

    private Category categoryreq ;
    private List<Tag> tags;

    //------------- User Request Data ----------------------------
    private int userId = faker.number().numberBetween(200,300);
    private String username = faker.name().username();
    private String firstName =faker.name().firstName();
    private String lastName = faker.name().lastName();
    private String email =faker.internet().emailAddress();
    private String password = faker.name().fullName();
    private String phone = faker.phoneNumber().phoneNumber();
    private int userStatus = 1;

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


    @BeforeAll
    public void beforeALl(){
        log.info("\n\n------- EndToEndTest STARTED -------\n\n");
    }
    /* Test :
     1. User Api -  User Logs In
     2. Pet Api -   Search Pet
     2. Store Api - Place the Order for the pet
    */
    @Test
    public void UserLoginAndSearchPetAndPlaceOrder(){

        // ------------1 User Api - User logs into System ------------

        // Create New User
        log.info("User loging into System");
        userLoginReq = UserRequest.builder()
                .id(userId)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .phone(phone)
                .userStatus(userStatus)
                .build();

        ExtractableResponse<Response> uresp = UserApi.createNewUser(userLoginReq)
                .then()
                .statusCode(200)
                .extract();

        log.info(" New User {} Creation & status - {}",username,userStatus);
        // User log into system
        userLoginResp = uresp.body().as(UserResponse.class);
        String username = userLoginResp.getUsername();
        String pwd = userLoginResp.getPassword();

        ExtractableResponse<Response> lresp = UserApi.userLogin(username,pwd)
                .then()
                .statusCode(200)
                .extract();

        //System.out.println("Response: \n" + lresp.asString());
        log.info("User login Sucessful & Response - {}",lresp.asString());
        String expResponse = "Logged in user session";
        assertThat(lresp.asString()).contains(expResponse);

        //----------  2.0  Pet Api -  Add new pet to store  -------------------

        // Add new pet to the store
        Category categoryreq = Category.builder().id(catgId).name(catgname).build();
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(tagId1, tagName1));
        tags.add(new Tag(tagId2, tagName2));

        petCreateReq = PetRequest.builder()
                .id(petId)
                .name(petName)
                .category(categoryreq)
                .tags(tags)
                .status(petStatus)
                .build();

        ExtractableResponse<Response> eresp = PetApi.createAddNewPet(petCreateReq)
                .then().statusCode(200)
                .extract();

        petCreateResp = eresp.body().as(PetResponse.class);
        int petId_actual = petCreateResp.getId();
        log.info("New Pet with pet id {} added sucessfully to the store", petId_actual);

        // Search Pet By Pet-Id
        ExtractableResponse<Response> presp = PetApi.getPetById(petId_actual)
                .then().statusCode(200)
                .extract();
        PetResponse petSearchResp = eresp.body().as(PetResponse.class);
        assertThat(petSearchResp.getId()).isEqualTo(petId_actual);
        log.info("Search Pet by Pet id {} is sucessful",petId_actual);


        //------------  3. Store Api - Place order for the above pet----------

        // Place Order for the Pet searched
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
        log.info("Place order for the Pet {} - {}",petId_actual,petName);

        assertThat(orderId_act).isEqualTo(orderId);
        assertThat(petCreateResp.id).isEqualTo(storeOrderResp.getPetId());


    }
}
