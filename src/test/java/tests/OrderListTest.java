package tests;

import clients.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import utils.BaseTest;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.isA;

public class OrderListTest extends BaseTest {
    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что в теле ответа возвращается список заказов")
    public void getOrdersListReturnsOrdersArray() {
        Response response = orderClient.getOrdersList();

        response.then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders", isA(List.class));
    }
}
