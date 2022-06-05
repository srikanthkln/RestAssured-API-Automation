package tests.components;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import testbase.TestBase;
import testbase.endpoints.PetApi;
import testbase.models.requests.pets.Category;
import testbase.models.requests.pets.PetRequest;
import testbase.models.requests.pets.enums.PetStatus;
import testbase.models.requests.pets.Tag;
import testbase.models.responses.pets.PetResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PetsApiTest extends TestBase {

    private PetRequest requestBody;

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

    private  Category categoryreq ;
    private List<Tag> tags;

    private ExtractableResponse<Response> eresp;

    @BeforeAll
    public void beforeAll(){

        log.info("\n\n------- PetsAPITest STARTED -------\n\n");
        // Add a new Pet to the Store -- Use this for all other tests -- Delete in After All
        // Buid Category request
        categoryreq = Category.builder()
                .id(catgId)
                .name(catgname)
                .build();

        // Add Tags Data
        tags = new ArrayList<>();
        Tag tag1 = new Tag(tagId1, tagName1);
        Tag tag2 = new Tag(tagId2, tagName2);
        tags.add(tag1);
        tags.add(tag2);

        // Build Add New Pet request data
        requestBody = PetRequest.builder()
                .id(petId)
                .name(petName)
                .category(categoryreq)
                .tags(tags)
                .status(petStatus)
                .build();

        eresp = PetApi.createAddNewPet(requestBody)
                .then().statusCode(200)
                .extract();

        PetResponse petResponse = eresp.body().as(PetResponse.class);
        log.info("Pet Id :" + petResponse.getId());
    }

    @Test
    @Order(1)
    @DisplayName("Add new pet to store")
    public void AddNewPet() {

        //------------- Build Request payload ---------------

       int petId = faker.number().numberBetween(2002, 3002);
        // Buid Category request
        Category categoryreq = Category.builder().id(catgId).name(catgname).build();
        // Add Tags Data
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(tagId1, tagName1));
        tags.add(new Tag(tagId2, tagName2));

        // Build Add New Pet request data
        requestBody = PetRequest.builder()
                .id(petId)
                .name(petName)
                .category(categoryreq)
                .tags(tags)
                .status(petStatus)
                .build();

         //ACTION: Send the REQUEST and extract the RESPONSE upon sucess
        ExtractableResponse<Response> eresp = PetApi.createAddNewPet(requestBody)
                .then().statusCode(200)
                .extract();

        PetResponse petResponse = eresp.body().as(PetResponse.class);
        int petId_actual = petResponse.getId();
        log.info("Pet Id in Response : {}", petId_actual);
        assertEquals(petId, petId_actual);
    }

    @Test
    @Order(2)
    @DisplayName("Get Pets by Pet status")
    public void GetPetByStatus() throws JsonProcessingException {

        // ACTION: Send the REQUEST and extract the RESPONSE upon sucess
        ExtractableResponse<Response> eresp = PetApi.getPetsByStatus(petStatus)
                .then().statusCode(200)
                .extract();
        log.info("Geting Pet by Status - {}", petStatus);

        // List<PetResponse> petResponses = eresp.body().jsonPath().getList("",PetResponse.class);
        // PetResponse[] pets = mapper.readValue(eresp.asString(), PetResponse[].class);
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        List<PetResponse> allPets = mapper.readValue(eresp.asString(), new TypeReference<>() {});
        log.info(" New Pets total: {}", allPets.size());
        for(PetResponse pet : allPets)
            log.info("Pet id : {}",pet.getId());
    }

    @AfterAll
    public void deleteAll(){

        PetResponse petResponse = eresp.body().as(PetResponse.class);
        log.info("Pet Id : {}", petResponse.getId());
        ExtractableResponse<Response> eresp = PetApi.deletePetById(petResponse.getId())
                .then().statusCode(200)
                .extract();

        // Check if deleted using the get api
        ExtractableResponse<Response> eresp2 = PetApi.getPetById(petResponse.getId())
                .then().statusCode(404).extract();
        log.info("Get Pet Response is :  {}", eresp2.statusCode());

    }
}
