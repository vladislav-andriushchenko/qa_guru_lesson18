package tests;

import extensions.WithLogin;
import models.AllBooksResponseModel;
import models.UserResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.DemoQaSteps;
import utils.ResourceUtils;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class DemoQaShopAuthorizedTests extends TestBase {

    DemoQaSteps demoQaSteps = new DemoQaSteps();
    ResourceUtils utils = new ResourceUtils();

    @DisplayName("Check the ability to add a book to user")
    @WithLogin
    @Test
    void deleteBookAuthorizedTest() {
        demoQaSteps.deleteAllBooks(204);

        UserResponseModel userData = demoQaSteps.getUserData(200);

        step("Assert that a user doesn't have any books", () ->
                assertThat(userData.getBooks())
                        .as("Book's collection must be empty")
                        .isNullOrEmpty());

        AllBooksResponseModel allBooks = demoQaSteps.getAllBooks(200);
        String isbn = allBooks.getBooks().get(0).getIsbn();

        String body = utils.getStringFromResource("/requestBody/addBookBody.json")
                .replace("{isbn}", isbn)
                .replace("{userId}", userData.getUserId());

        demoQaSteps.addBookToUser(body, 201);

        UserResponseModel userUpdatedData = demoQaSteps.getUserData(200);

        step("Assert that a user data is updated", () -> {
            assertThat(userUpdatedData.getBooks())
                    .as("User should have at least one book")
                    .isNotEmpty();

            assertThat(userUpdatedData.getBooks())
                    .as("User's books should contain the added book")
                    .extracting("isbn")
                    .contains(isbn);
        });

        demoQaSteps.deleteAllBooks(204);

        step("Assert that a user doesn't have any books", () ->
                assertThat(userData.getBooks())
                        .as("Book's collection must be empty")
                        .isNullOrEmpty());
    }
}

