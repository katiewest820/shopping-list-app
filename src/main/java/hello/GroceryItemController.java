package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

//import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GroceryItemController {

    //private final AtomicLong counter = new AtomicLong();

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    GroceryListRepository groceryListRepository;

    @Autowired
    MongoOperations mongoOperations;

    //add one item
    @RequestMapping(value = "/addItem", method = POST)
    public ResponseEntity<Item> update(@RequestBody Item newItem){
        itemRepository.save(newItem);
        GroceryList groceryList = mongoOperations.findOne(new Query(Criteria.where("id").is(newItem.groceryListId)), GroceryList.class, "groceryList");
        long itemCount = groceryList.itemCount +1;
        System.out.println(itemCount);

        groceryList.setItemCount(itemCount++);
        mongoOperations.save(groceryList);
        //Optional <GroceryList> editedGroceryList = groceryListRepository.findById(newItem.groceryListId);
        System.out.println(groceryList);
        System.out.println(newItem.item + " successfully added");
        return ResponseEntity.ok(newItem);
    }

    //get all items
    @RequestMapping(value = "/allItems", method = RequestMethod.GET)
    public List<Item> getItems(){
        return itemRepository.findAll();
    }

    //get one by name
    @RequestMapping(value = "/item/{item}", method = RequestMethod.GET)
    public List<Item> findByItem(@PathVariable("item") String item){
        return itemRepository.findByItem(item);
    }

    //get one by id
    @RequestMapping(value = "/itemId/{id}", method = RequestMethod.GET)
    public Optional<Item> findById(@PathVariable("id") String id){
        return itemRepository.findById(id);
    }

    //delete all items
    @RequestMapping(value ="/deleteAll", method = RequestMethod.DELETE)
    public String deleteAll() {
        itemRepository.deleteAll();
        return "All Items have been deleted";
    }

    //delete one by id
    @RequestMapping(value = "/deleteOne/{id}", method = RequestMethod.DELETE)
    public List<Item> deleteOne(@PathVariable("id") String id){
        Item deletedItem = mongoOperations.findAndRemove(new Query(Criteria.where("id").is(id)), Item.class,"item");
        System.out.println(deletedItem);
        return itemRepository.findAll();
    }

    //delete one by name
    @RequestMapping(value = "/deleteOneByName/{item}", method = RequestMethod.DELETE)
    public List<Item> deleteOneByName(@PathVariable("item") String item){
        Item deletedItem = mongoOperations.findAndRemove(new Query(Criteria.where("item").is(item)), Item.class,"item");
        System.out.println(deletedItem);
        return itemRepository.findAll();
    }

    //edit item
    @RequestMapping(value = "/editOne/{id}", method = RequestMethod.PUT)
    public List<Item> editOne(@PathVariable("id") String id, @RequestBody Item editedItemDetails){
        Item editItem = mongoOperations.findOne(new Query(Criteria.where("id").is(id)), Item.class, "item");
        System.out.println(editedItemDetails);
        System.out.println(editItem);
        editItem.setActive(editedItemDetails.active);
        editItem.setId(id);
        editItem.setQuantity(editedItemDetails.quantity);
        editItem.setItem(editedItemDetails.item);
        editItem.setGroceryListId(editedItemDetails.groceryListId);
        mongoOperations.save(editItem);
        return itemRepository.findAll();
    }

}
