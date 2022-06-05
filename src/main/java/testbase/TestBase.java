package testbase;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import org.junit.jupiter.api.BeforeAll;
import testbase.models.AuthToken;
import testbase.models.Env;

import java.io.FileNotFoundException;
import java.net.URL;


public class TestBase {

    public static Faker faker = new Faker();
    private static final Configuration config = new Configuration();

    @Getter
    private static Env env;
    @Getter
    private static AuthToken apikey;

    @BeforeAll
    public static void beforeAllTests() throws FileNotFoundException {

        env = config.readEnv(Env.class);
        apikey = config.readKeys(AuthToken.class);

    }

    public static RequestSpecification getRequestSpecification(URL url , String basePath){

        if(url== null){
            throw new NullPointerException(String.format("Can't get URL for service %s",basePath));
        }
        return new RequestSpecBuilder()
                .setBaseUri(url.toString())
                .setPort(url.getPort())
                .setBasePath(basePath)
                .build();
    }

}
