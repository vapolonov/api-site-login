import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class Registration {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com/";
    }
}
