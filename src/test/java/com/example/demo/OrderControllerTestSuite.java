package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.criteria.Order;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@Transactional
@SpringBootTest(classes = SareetaApplication.class)
@RunWith( SpringRunner.class )
public class OrderControllerTestSuite {
    @Autowired
    private OrderController orderController;

    @Autowired
    private UserController userController;

    @Test
    public void testSubmitOrderOK() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("TestUser");
        request.setPassword("test1234");
        request.setConfirmPassword("test1234");
        ResponseEntity<User> response = this.userController.createUser(request);

        this.orderController.submit("TestUser");
        assertNotEquals(null, response.getBody());
    }

    @Test
    public void testSubmitOrderFailed() {
        ResponseEntity<UserOrder> response = this.orderController.submit("TestUser");

        assertEquals(null, response.getBody());
    }

    @Test
    public void testGetOrdersOK() {
        this.testSubmitOrderOK();

        ResponseEntity<List<UserOrder>> response = this.orderController.getOrdersForUser("TestUser");
        assertEquals(1,response.getBody().size());
    }

    @Test
    public void testGetOrdersFailed() {
        ResponseEntity<List<UserOrder>> response = this.orderController.getOrdersForUser("Testuser");
        assertEquals(null,response.getBody());
    }
}