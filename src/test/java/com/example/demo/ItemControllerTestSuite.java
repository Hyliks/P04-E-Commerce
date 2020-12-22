package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Transactional
@SpringBootTest(classes = SareetaApplication.class)
@RunWith( SpringRunner.class )
public class ItemControllerTestSuite {

    @Autowired
    private ItemController itemController;

    @Test
    public void testGetItems() {
        ResponseEntity<List<Item>> result = this.itemController.getItems();

        assertEquals(2, result.getBody().size());
    }

    @Test
    public void testGetItemByIdOK() {
        ResponseEntity<List<Item>> result = this.itemController.getItems();

        ResponseEntity<Item> result2 = this.itemController.getItemById(result.getBody().get(0).getId());

        assertEquals(result.getBody().get(0).getDescription(), result2.getBody().getDescription());
        assertEquals(result.getBody().get(0).getId(), result2.getBody().getId());
        assertEquals(result.getBody().get(0).getName(), result2.getBody().getName());
        assertEquals(result.getBody().get(0).getPrice(), result2.getBody().getPrice());
    }

    @Test
    public void testGetItemByIdNotOK() {
        ResponseEntity<Item> result2 = this.itemController.getItemById(5000L);
        assertEquals(null, result2.getBody());
    }

    @Test
    public void testGetItemsByNameOk() {
        ResponseEntity<List<Item>> result = this.itemController.getItemsByName("Round Widget");
        assertEquals(1, result.getBody().size());
    }

    @Test
    public void testGetItemsByNameNotOK() {
        ResponseEntity<List<Item>> result2 = this.itemController.getItemsByName("ABCDE");
        assertEquals(null, result2.getBody());
    }
}
