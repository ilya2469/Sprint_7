package tests;

import clients.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.BaseTest;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.isA;

@RunWith(Parameterized.class)
public class OrderCreateTest extends BaseTest {
    private OrderClient orderClient;
    private final List<String> color;

    public OrderCreateTest(List<String> color) {
        this.color = color;
    }
    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Object[][] getColorData() {
        return new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {null}
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Создание заказа с разными вариантами цвета")
    @Description("Проверка, что можно создать заказ с цветом BLACK, GREY, обоими цветами или без цвета")
    public void createOrderWithDifferentColorsReturnsTrack() {
        Order order = new Order("Вася",
                "Васильев",
                "Москва, ул. Вавилова, д.12,",
                "5",
                "+79057654332",
                1,
                "2025.11.30",
                "позвонить за 15 минут до приезда",
                color
        );

        Response response = orderClient.createOrder(order);

        response.then()
                .statusCode(201)
                .body("track", notNullValue())
                .body("track", isA(Integer.class));
    }
}
