package clients;


import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private static final String COURIER = "/api/v1/courier";
    private static final String LOGIN = "/api/v1/courier/login";

    @Step("Создать курьера")
    public Response createCourier(Courier courier) {
     return given()
             .contentType("application/json")
             .body(courier)
             .when()
             .post(COURIER);
    }

    @Step("Авторизация курьера")
    public Response loginCourier(CourierCredentials credentials) {
        return given()
                .contentType("application/json")
                .body(credentials)
                .when()
                .post(LOGIN);

    }

    @Step("Удалить курьера c id = {courierId}")
    public Response deleteCourier(int courierId) {
        return given()
                .when()
                .delete(COURIER + "/" + courierId);
    }

}
