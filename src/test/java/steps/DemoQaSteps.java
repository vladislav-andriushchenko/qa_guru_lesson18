package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.*;
import utils.TestData;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static specs.AuthorizedSpec.getAuthResponseWithStatusCode;
import static specs.AuthorizedSpec.requestAuthSpec;
import static specs.BaseSpec.getResponseWithStatusCode;
import static specs.BaseSpec.requestSpec;

public class DemoQaSteps {

    private final String USER_URL = "/Account/v1/User";
    private final String LOGIN_URL = "/Account/v1/Login";
    private final String TOKEN_URL = "/Account/v1/GenerateToken";
    private final String AUTHORIZE_URL = "/Account/v1/Authorized";
    private final String BOOKS_URL = "/BookStore/v1/Books";
    private final String BOOK_URL = "/BookStore/v1/Book";


    @Step("Create a user")
    public Response registerUser(String body, int statusCode) {
        return given(requestSpec)
                .body(body)
                .when()
                .post(USER_URL)
                .then()
                .spec(getResponseWithStatusCode(statusCode))
                .extract().response();
    }

    @Step("Sign in")
    public LoginResponseModel login(int statusCode) {
        LoginRequestModel request = new LoginRequestModel(TestData.USERNAME, TestData.PASSWORD);

        return given(requestSpec)
                .body(request)
                .when()
                .post(LOGIN_URL)
                .then()
                .spec(getResponseWithStatusCode(statusCode))
                .extract()
                .as(LoginResponseModel.class);
    }

    @Step("Sign in")
    public LoginResponseModel login(String body, int statusCode) {
        return given(requestSpec)
                .body(body)
                .when()
                .post(LOGIN_URL)
                .then()
                .spec(getResponseWithStatusCode(statusCode))
                .extract()
                .as(LoginResponseModel.class);
    }

    @Step("Generate a token")
    public TokenModel getToken(String body, int statusCode) {
        return given(requestSpec)
                .body(body)
                .when()
                .post(TOKEN_URL)
                .then()
                .spec(getResponseWithStatusCode(statusCode))
                .extract()
                .as(TokenModel.class);
    }

    @Step("Generate auth token")
    public Response authorize(String body, int statusCode) {
        return given(requestSpec)
                .body(body)
                .when()
                .post(AUTHORIZE_URL)
                .then()
                .spec(getResponseWithStatusCode(statusCode))
                .extract()
                .response();
    }

    @Step("Get user data")
    public UserResponseModel getUserData(String userId, String token, int statusCode) {
        return given(requestSpec)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(USER_URL + "/" + userId)
                .then()
                .spec(getResponseWithStatusCode(statusCode))
                .extract()
                .as(UserResponseModel.class);
    }

    public UserResponseModel getUserData(int statusCode) {
        String userId = getWebDriver().manage().getCookieNamed("userID").getValue();
        return given(requestAuthSpec)
                .when()
                .get(USER_URL + "/" + userId)
                .then()
                .spec(getAuthResponseWithStatusCode(statusCode))
                .extract()
                .as(UserResponseModel.class);
    }

    @Step("Delete a user")
    public Response deleteUser(String userId, String token, int statusCode) {
        return given(requestSpec)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(USER_URL + "/" + userId)
                .then()
                .spec(getResponseWithStatusCode(statusCode))
                .extract()
                .response();
    }

    @Step("Get all available books")
    public AllBooksResponseModel getAllBooks(int statusCode) {
        return given(requestSpec)
                .when()
                .get(BOOKS_URL)
                .then()
                .spec(getResponseWithStatusCode(statusCode))
                .extract()
                .as(AllBooksResponseModel.class);
    }

    @Step("Add book to a user")
    public Response addBookToUser(String body, String token, int statusCode) {
        return given(requestSpec)
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .post(BOOKS_URL)
                .then()
                .spec(getResponseWithStatusCode(statusCode))
                .extract()
                .response();
    }

    @Step("Add book to a user")
    public Response addBookToUser(String body, int statusCode) {
        return given(requestAuthSpec)
                .body(body)
                .when()
                .post(BOOKS_URL)
                .then()
                .spec(getAuthResponseWithStatusCode(statusCode))
                .extract()
                .response();
    }

    @Step("Replace a user's book")
    public UserResponseModel replaceUserBook(String body, String bookId, String token, int statusCode) {
        return given(requestSpec)
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .put(BOOKS_URL + "/" + bookId)
                .then()
                .spec(getResponseWithStatusCode(statusCode))
                .extract()
                .as(UserResponseModel.class);
    }

    @Step("Delete all user's books")
    public Response deleteAllBooks(String userId, String token, int statusCode) {
        return given(requestSpec)
                .header("Authorization", "Bearer " + token)
                .queryParam("UserId", userId)
                .when()
                .delete(BOOKS_URL)
                .then()
                .spec(getResponseWithStatusCode(statusCode))
                .extract()
                .response();
    }

    @Step("Delete all user's books")
    public Response deleteAllBooks(int statusCode) {
        String userId = getWebDriver().manage().getCookieNamed("userID").getValue();
        return given(requestAuthSpec)
                .queryParam("UserId", userId)
                .when()
                .delete(BOOKS_URL)
                .then()
                .spec(getAuthResponseWithStatusCode(statusCode))
                .extract()
                .response();
    }

    @Step("Delete single user's book")
    public Response deleteSingleBook(String body, String token, int statusCode) {
        return given(requestSpec)
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .delete(BOOK_URL)
                .then()
                .spec(getResponseWithStatusCode(statusCode))
                .extract()
                .response();
    }

    @Step("Delete single user's book")
    public Response deleteSingleBook(String body, int statusCode) {
        return given(requestAuthSpec)
                .body(body)
                .when()
                .delete(BOOK_URL)
                .then()
                .spec(getAuthResponseWithStatusCode(statusCode))
                .extract()
                .response();
    }
}
