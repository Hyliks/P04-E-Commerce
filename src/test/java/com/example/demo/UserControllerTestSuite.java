package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

@Transactional
@SpringBootTest(classes = SareetaApplication.class)
@RunWith( SpringRunner.class )
public class UserControllerTestSuite {

    @Autowired
    private UserController userController;

    @Test
    public void testFindByIdFails(){
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("TestUser");
        request.setPassword("test1234");
        request.setConfirmPassword("test1234");
        ResponseEntity<User> response = this.userController.createUser(request);

        ResponseEntity<User> response2 = this.userController.findById(response.getBody().getId());

        assertEquals(response.getBody().getId(), response2.getBody().getId());
        assertEquals(response.getBody().getUsername(), response2.getBody().getUsername());
        assertEquals(response.getBody().getPassword(), response2.getBody().getPassword());
    }

    @Test
    public void testFindByIdOK(){
        ResponseEntity<User> response = this.userController.findById(500L);
        assertEquals(null, response.getBody());
    }
    @Test
    public void testFindByUserNameFails(){
        ResponseEntity<User> response = this.userController.findByUserName("test");
        assertEquals(null, response.getBody());
    }

    @Test
    public void testFindByUserNameOK(){
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("TestUser");
        request.setPassword("test1234");
        request.setConfirmPassword("test1234");
        ResponseEntity<User> response = this.userController.createUser(request);

        ResponseEntity<User> response2 = this.userController.findByUserName("TestUser");

        assertEquals(response.getBody().getId(), response2.getBody().getId());
        assertEquals(response.getBody().getUsername(), response2.getBody().getUsername());
        assertEquals(response.getBody().getPassword(), response2.getBody().getPassword());
    }

    @Test
    public void testCreateUserOK(){
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("TestUser");
        request.setPassword("test1234");
        request.setConfirmPassword("test1234");
        ResponseEntity<User> response = this.userController.createUser(request);

        assertEquals("TestUser", response.getBody().getUsername());
    }

    @Test
    public void testCreateUserPasswordMismatch(){
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("TestUser");
        request.setPassword("test1234");
        request.setConfirmPassword("test4321");
        ResponseEntity<User> response = this.userController.createUser(request);

        assertEquals(null, response.getBody());
    }

    @Test
    public void testCreateUserPasswordLength(){
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("TestUser");
        request.setPassword("test");
        request.setConfirmPassword("test");
        ResponseEntity<User> response = this.userController.createUser(request);

        assertEquals(null, response.getBody());
    }
}
