package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

@Transactional
@SpringBootTest(classes = SareetaApplication.class)
@RunWith( SpringRunner.class )
public class CartControllerTestSuite {

    @Autowired
    private CartController cartController;

    @Autowired
    private UserController userController;

    @Autowired
    private ItemController itemController;

    @Test
    public void testAddItemToCartOK() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("TestUser");
        request.setPassword("test1234");
        request.setConfirmPassword("test1234");
        ResponseEntity<User> response = this.userController.createUser(request);

        ResponseEntity<List<Item>> result = this.itemController.getItems();

        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(result.getBody().get(0).getId());
        cartRequest.setQuantity(1);
        cartRequest.setUsername("TestUser");

        ResponseEntity<Cart> cartResponse = this.cartController.addTocart(cartRequest);

        assertEquals(1,cartResponse.getBody().getItems().size());
        assertEquals(result.getBody().get(0).getId(),cartResponse.getBody().getItems().get(0).getId());
    }

    @Test
    public void testAddItemToCartNoUser() {
        ResponseEntity<List<Item>> result = this.itemController.getItems();

        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(result.getBody().get(0).getId());
        cartRequest.setQuantity(1);
        cartRequest.setUsername("TestUser");

        ResponseEntity<Cart> cartResponse = this.cartController.addTocart(cartRequest);

        assertEquals(null,cartResponse.getBody());
    }

    @Test
    public void testAddItemToCartNoItem() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("TestUser");
        request.setPassword("test1234");
        request.setConfirmPassword("test1234");
        ResponseEntity<User> response = this.userController.createUser(request);

        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(-1);
        cartRequest.setQuantity(1);
        cartRequest.setUsername("TestUser");

        ResponseEntity<Cart> cartResponse = this.cartController.addTocart(cartRequest);

        assertEquals(null,cartResponse.getBody());
    }

    @Test
    public void testRemoveItemToCartOK() {
        this.testAddItemToCartOK();

        ResponseEntity<List<Item>> result = this.itemController.getItems();

        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(result.getBody().get(0).getId());
        cartRequest.setQuantity(1);
        cartRequest.setUsername("TestUser");

        ResponseEntity<Cart> cartResponse = this.cartController.removeFromcart(cartRequest);

        assertEquals(0,cartResponse.getBody().getItems().size());
    }

    @Test
    public void testRemoveItemToCartNoUser() {
        this.testAddItemToCartOK();

        ResponseEntity<List<Item>> result = this.itemController.getItems();

        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(result.getBody().get(0).getId());
        cartRequest.setQuantity(1);
        cartRequest.setUsername("TestUser1");

        ResponseEntity<Cart> cartResponse = this.cartController.removeFromcart(cartRequest);

        assertEquals(null,cartResponse.getBody());
    }

    @Test
    public void testRemoveItemToCartNoItem() {
        this.testAddItemToCartOK();

        ResponseEntity<List<Item>> result = this.itemController.getItems();

        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(-1);
        cartRequest.setQuantity(1);
        cartRequest.setUsername("TestUser");

        ResponseEntity<Cart> cartResponse = this.cartController.removeFromcart(cartRequest);

        assertEquals(null,cartResponse.getBody());
    }
}
