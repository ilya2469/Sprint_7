package clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Order;

import static io.restassured.RestAssured.given;


public class OrderClient {
    private static final String ORDER_PATH = "/api/v1/orders";

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given()
                .contentType("application/json" )
                .body(order)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Получение списка заказов")
    public Response getOrdersList() {
        return given()
                .when()
                .get(ORDER_PATH);
    }
}
// temporary change for PR
