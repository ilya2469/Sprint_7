package tests;

import clients.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.BaseTest;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierCreateTest extends BaseTest {
    private CourierClient courierClient;
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            courierClient.deleteCourier(courierId);
        }
    }
        private String generateLogin() {
        return "testLogin_" + UUID.randomUUID().toString().substring(0, 8);
        }
        private Integer getCourierIdByCredentials(Courier courier) {
            CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
            Response loginResponse = courierClient.loginCourier(credentials);
            return loginResponse.then()
                    .statusCode(200)
                    .body("id", notNullValue())
                    .extract()
                    .path("id");
        }

        @Test
        @DisplayName("Создание курьера с валидными данными")
        @Description("Проверка, что курьера можно создать с валидными данными и в ответе ok: true")
        public void createCourierWithValidDataReturnsOkTrue() {
        courier = new Courier(generateLogin(), "password123", "TestName");

        Response response = courierClient.createCourier(courier);
        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));
        courierId = getCourierIdByCredentials(courier);
        }

        @Test
        @DisplayName("Нельзя создать двух одинаковых курьеров")
        @Description("Проверка, что создание двух курьеров с одинаковым логином возвращает ошибку 409")
        public void createDuplicateCourierReturnsError() {
        courier = new Courier(generateLogin(), "password123", "TestName");

        courierClient.createCourier(courier)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        Response response = courierClient.createCourier(courier);
        response.then()
                .statusCode(409)
                .body("message", notNullValue());
            courierId = getCourierIdByCredentials(courier);
        }

        @Test
        @DisplayName("Создание курьера без логина возвращает ошибку")
        @Description("Проверка, что нельзя создать курьера без обязательного поля логин")
        public void createCourierWithoutLoginReturnsError() {
        courier = new Courier(null, "password123", "TestName");

        Response response = courierClient.createCourier(courier);

        response.then()
                .statusCode(400)
                .body("message", notNullValue());
        }

        @Test
        @DisplayName("Создание курьера без пароля возвращает ошибку")
        @Description("Проверка, что нельзя создать курьера без обязательного поля пароль")
        public void createCourierWithoutPasswordReturnsError() {
        courier = new Courier(generateLogin(), null, "TestName");
        Response response = courierClient.createCourier(courier);
        response.then()
                .statusCode(400)
                .body("message", notNullValue());
        }

    }


