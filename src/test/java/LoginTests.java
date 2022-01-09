import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class LoginTests {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com/";
        Configuration.baseUrl = "http://demowebshop.tricentis.com/";
    }

    @Test
    void loginUiTest() {
        step("Open login page", () ->
                open("login"));

        step("Fill login form", () -> {
            $("#Email").setValue("test75@test.com");
            $("#Password").setValue("123456")
                    .pressEnter();
        });

        step("Verify successful authorization", () ->
                $(".account").shouldHave(text("test75@test.com"))
        );
    }

    @Test
    void loginApiTest() {
        step("Get cookie by api and set it to browser", () -> {
            String authorizationCookie =
                    given()
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .formParam("Email", "test75@test.com")
                            .formParam("Password", "123456")
                            .when()
                            .post("login")
                            .then()
                            .statusCode(302)
                            .extract()
                            .cookie("NOPCOMMERCE.AUTH");

            step("Open minimal content, because cookie can be set when site is opened", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));

            step("Set cookie to to browser", () ->
                    getWebDriver().manage().addCookie(
                            new Cookie("NOPCOMMERCE.AUTH", authorizationCookie))
            );
        });
        step("Open main page", () ->
                open("")
        );

        step("Verify successful authorization", () ->
                $(".account").shouldHave(text("test75@test.com"))
        );
    }
}
