package tests;

import clients.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import models.Courier;
import models.CourierCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.BaseTest;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

public class CourierLoginTest extends BaseTest {
    private CourierClient courierClient;
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();

        courier = new Courier("loginTest_" + System.currentTimeMillis(), "password123", "TestName");

        courierClient.createCourier(courier)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        CourierCredentials creds = new CourierCredentials(courier.getLogin(), courier.getPassword());

        courierId = courierClient.loginCourier(creds)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Курьер может авторизоваться с валидными данными")
    @Description("Проверка, что курьер может успешно авторизоваться с правильными логином и паролем и в ответе есть id")
    public void courierCanLoginWithValidCredentials() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());

        courierClient.loginCourier(credentials)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("id", isA(Integer.class));
    }

    @Test
    @DisplayName("Нельзя авторизоваться без логина")
    @Description("Проверка, что для авторизации нужно передать логин")
    public void loginWithoutLoginReturnsError() {
        CourierCredentials credentials = new CourierCredentials(null, courier.getPassword());

        courierClient.loginCourier(credentials)
                .then()
                .statusCode(400)
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Нельзя авторизоваться без пароля")
    @Description("Проверка, что для авторизации нужно передать пароль")
    public void loginWithoutPasswordReturnsError() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), null);
        courierClient.loginCourier(credentials)
                .then()
                .statusCode(400)
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Ошибка при неправильном логине")
    @Description("Проверка, что система вернёт ошибку при неверном логине")
    public void loginWithIncorrectLoginReturnsError() {
        CourierCredentials credentials = new CourierCredentials("wrongLogin_" + System.currentTimeMillis(), courier.getPassword());
        courierClient.loginCourier(credentials)
                .then()
                .statusCode(404)
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Ошибка при неправильном пароле")
    @Description("Проверка, что система вернёт ошибку при неверном пароле")
    public void loginWithIncorrectPasswordReturnsError() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), "wrongPassword");

        courierClient.loginCourier(credentials)
                .then()
                .statusCode(404)
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Авторизация под несуществующим пользователем возвращает ошибку")
    @Description("Проверка, что если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    public void loginWithNonExistentUserReturnsError() {
        CourierCredentials credentials = new CourierCredentials("nonExistentUser_" + System.currentTimeMillis(),
                "password123");
        courierClient.loginCourier(credentials)
                .then()
                .statusCode(404)
                .body("message", notNullValue());
    }

}
