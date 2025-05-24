package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;

public class AuthorizedSpec {

    public static RequestSpecification requestAuthSpec = with()
            .filter(withCustomTemplates())
            .log().all()
            .contentType(JSON)
            .baseUri("https://demoqa.com")
            .header("Authorization", "Bearer " + getWebDriver().manage().getCookieNamed("token").getValue());

    public static ResponseSpecification getAuthResponseWithStatusCode(int statusCode) {
        return new ResponseSpecBuilder()
                .log(ALL)
                .expectStatusCode(statusCode)
                .build();
    }
}
