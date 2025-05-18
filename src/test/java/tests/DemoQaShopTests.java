package tests;

import com.codeborne.selenide.Selenide;
import io.restassured.response.Response;
import models.AllBooksResponseModel;
import models.LoginResponseModel;
import models.TokenModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import steps.DemoQaSteps;
import utils.ResourceUtils;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static utils.ResourceUtils.getRandomName;
import static utils.ResourceUtils.getRandomPassword;

public class DemoQaShopTests extends TestBase {

    DemoQaSteps demoQaSteps = new DemoQaSteps();
    ResourceUtils utils = new ResourceUtils();

    static String userName, password, userId, token;

    public void login() {
        String body = utils.getStringFromResource("/requestBody/registerBody.json")
                .replace("{userName}", userName)
                .replace("{password}", password);
        LoginResponseModel response = demoQaSteps.login(body, 200);

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", response.getUserId()));
        getWebDriver().manage().addCookie(new Cookie("expires", response.getExpires()));
        getWebDriver().manage().addCookie(new Cookie("token", response.getToken()));
    }

    @BeforeAll
    static void createUser() {
        DemoQaSteps demoQaSteps = new DemoQaSteps();
        ResourceUtils utils = new ResourceUtils();

        userName = getRandomName();
        password = getRandomPassword();
        String body = utils.getStringFromResource("/requestBody/registerBody.json")
                .replace("{userName}", userName)
                .replace("{password}", password);

        Response response = demoQaSteps.registerUser(body, 201);
        userId = response.jsonPath().get("userID").toString();

        TokenModel tokenResponse = demoQaSteps.getToken(body, 200);
        token = tokenResponse.getToken();
    }

    @AfterAll
    static void deleteUser() {
        DemoQaSteps demoQaSteps = new DemoQaSteps();
        demoQaSteps.deleteUser(userId, token, 204);
    }

    @DisplayName("Check the ability to add a book to user (api + ui)")
    @Test
    void deleteBookApiAndUiTest() {
        login();
        step("Open user's profile page", () -> {
            open("/profile");
            $("#userName-value").shouldHave(text(userName));
            $(".rt-noData").shouldBe(visible).shouldHave(text("No rows found"));
        });

        AllBooksResponseModel allBooks = demoQaSteps.getAllBooks(200);
        String isbn = allBooks.getBooks().get(1).getIsbn();
        String title = allBooks.getBooks().get(1).getTitle();

        String body = utils.getStringFromResource("/requestBody/addBookBody.json")
                .replace("{userId}", userId)
                .replace("{isbn}", isbn);

        demoQaSteps.addBookToUser(body, token, 201);

        step("Open user's profile page and check added book", () -> {
            Selenide.refresh();
            $(".rt-tbody").shouldHave(text(title));
        });

        demoQaSteps.deleteAllBooks(userId, token, 204);

        step("Open user's profile page and check added book", () -> {
            Selenide.refresh();
            $(".rt-noData").shouldBe(visible).shouldHave(text("No rows found"));
        });
    }
}